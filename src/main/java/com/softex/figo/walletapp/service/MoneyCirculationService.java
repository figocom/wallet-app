package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.*;
import com.softex.figo.walletapp.dto.DownloadStatisticRequestDto;
import com.softex.figo.walletapp.dto.MoneyCirculationDto;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.repository.AuthUserRepository;
import com.softex.figo.walletapp.repository.CategoryRepository;
import com.softex.figo.walletapp.repository.MoneyCirculationRepository;
import com.softex.figo.walletapp.repository.SubCategoryRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.MoneyCirculationResponse;
import com.softex.figo.walletapp.response.WebResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MoneyCirculationService {
    private final MoneyCirculationRepository moneyCirculationRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final AuthUserRepository authUserRepository;
    private final UserService userService;

    public MoneyCirculationService(MoneyCirculationRepository moneyCirculationRepository, CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, AuthUserRepository authUserRepository, UserService userService) {
        this.moneyCirculationRepository = moneyCirculationRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.authUserRepository = authUserRepository;
        this.userService = userService;
    }

    public WebResponse<?> createCirculation(MoneyCirculationDto moneyCirculationDto) {
        if (moneyCirculationDto.amount() <= 0) {
            return new WebResponse<>(new ErrorDTO("Amount is null or less than 0", 400));
        }
        if (Objects.isNull(moneyCirculationDto.categoryId()) || moneyCirculationDto.categoryId() <= 0) {
            return new WebResponse<>(new ErrorDTO("Category id is null or less than 0", 400));
        }
        Category category;
        try {
            category = categoryRepository.findById(moneyCirculationDto.categoryId()).orElseThrow(() -> new ItemNotFoundException("Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        SubCategory subCategory = null;
        if (Objects.nonNull(moneyCirculationDto.subCategoryId())) {
            if (moneyCirculationDto.subCategoryId() <= 0) {
                return new WebResponse<>(new ErrorDTO("Sub category id is null or less than 0", 400));
            }
            try {
                subCategory = subCategoryRepository.findById(moneyCirculationDto.subCategoryId()).orElseThrow(() -> new ItemNotFoundException("Sub category not found"));
            } catch (ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        AuthUser authUser = userService.getCurrentUser();
        Category.Type type = category.getType();
        if (type == Category.Type.EXPENSE) {
            if (authUser.getBalance() < moneyCirculationDto.amount()) {
                return new WebResponse<>(new ErrorDTO("Not enough money", 400));
            }
            authUser.setBalance(authUser.getBalance() - moneyCirculationDto.amount());
        } else if (type == Category.Type.INCOME) {
            authUser.setBalance(authUser.getBalance() + moneyCirculationDto.amount());
        }
        MoneyCirculation moneyCirculation = new MoneyCirculation(moneyCirculationDto.amount(), category, subCategory, authUser.getCurrency(), type, moneyCirculationDto.note(), authUser);
        moneyCirculationRepository.save(moneyCirculation);
        authUserRepository.save(authUser);
        return new WebResponse<>(new DataDTO<>(moneyCirculation));
    }

    public WebResponse<?> getCirculation(Long id) {
        if (id <= 0) {
            return new WebResponse<>(new ErrorDTO("Id is null or less than 0", 400));
        }
        MoneyCirculation moneyCirculation = null;
        try {
            moneyCirculation = moneyCirculationRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Money circulation not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!moneyCirculation.getAuthUser().getUsername().equals(userService.getCurrentUser().getUsername())) {
            return new WebResponse<>(new ErrorDTO("Permission denied!", 403));
        }
        return new WebResponse<>(new DataDTO<>(moneyCirculation));
    }

    public WebResponse<?> getCirculationsByType(String type) {
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        AuthUser authUser = userService.getCurrentUser();
        if (type.equals("expense"))
            return new WebResponse<>(new DataDTO<>(moneyCirculationRepository.findAllByTypeAndAuthUser(Category.Type.EXPENSE, authUser)));
        if (type.equals("income"))
            return new WebResponse<>(new DataDTO<>(moneyCirculationRepository.findAllByTypeAndAuthUser(Category.Type.INCOME, authUser)));
        return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
    }

    public WebResponse<?> getCirculations() {
        return new WebResponse<>(new DataDTO<>(moneyCirculationRepository.findAllByAuthUser(userService.getCurrentUser())));
    }

    public WebResponse<?> getCirculationsByCategoryAndType(Long id, String type) {
        if (id <= 0) {
            return new WebResponse<>(new ErrorDTO("Id is null or less than 0", 400));
        }
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        Category category;
        try {
            category = categoryRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<MoneyCirculation> allByCategoryAndAuthUserAndType = moneyCirculationRepository.findAllByCategoryAndAuthUserAndType(category, userService.getCurrentUser(), categoryType);
        return getTotalResponse(allByCategoryAndAuthUserAndType);
    }

    public WebResponse<?> getCirculationsBySubCategoryAndType(Long id, String type) {
        if (id <= 0) {
            return new WebResponse<>(new ErrorDTO("Id is null or less than 0", 400));
        }
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        SubCategory subCategory = null;
        try {
            subCategoryRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Sub category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<MoneyCirculation> allBySubCategoryAndAuthUserAndType = moneyCirculationRepository.findAllBySubCategoryAndAuthUserAndType(subCategory, userService.getCurrentUser(), categoryType);
        return getTotalResponse(allBySubCategoryAndAuthUserAndType);
    }

    private WebResponse<?> getTotalResponse(List<MoneyCirculation> allBySubCategoryAndAuthUserAndType) {
        String totalAmount = String.valueOf(allBySubCategoryAndAuthUserAndType.stream().map(MoneyCirculation::getAmount).reduce(0.0, Double::sum));
        MoneyCirculationResponse moneyCirculationResponse = new MoneyCirculationResponse(allBySubCategoryAndAuthUserAndType, allBySubCategoryAndAuthUserAndType.size(), totalAmount);
        return new WebResponse<>(new DataDTO<>(moneyCirculationResponse));
    }
    public WebResponse<?> getDailyCirculations(String type) {
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        List<MoneyCirculation> allByAuthUserAndType = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
        List<MoneyCirculation> collect = allByAuthUserAndType.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().equals(LocalDate.now())).collect(Collectors.toList());
        return getTotalResponse(collect);
    }
    public WebResponse<?> getWeeklyCirculations(String type) {

        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        List<MoneyCirculation> allByAuthUserAndType = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
        List<MoneyCirculation> collect = allByAuthUserAndType.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(7))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }
    public WebResponse<?> getMonthlyCirculations(String type) {
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        List<MoneyCirculation> allByAuthUserAndType = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
        List<MoneyCirculation> collect = allByAuthUserAndType.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(30))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }
    public WebResponse<?> getYearlyCirculations(String type) {
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        List<MoneyCirculation> allByAuthUserAndType = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
        List<MoneyCirculation> collect = allByAuthUserAndType.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(365))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }
    public WebResponse<?> getDailyCirculationsByDay(String type, String day) {
        if (Objects.isNull(type)) {
            return new WebResponse<>(new ErrorDTO("Type is null", 400));
        }
        if (Objects.isNull(day)) {
            return new WebResponse<>(new ErrorDTO("Day is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        if (!day.matches("\\d{4}-\\d{2}-\\d{2}") || timeValidator(day)) {

            return new WebResponse<>(new ErrorDTO("Day is not valid", 400));
        }

        List<MoneyCirculation> allByAuthUserAndType = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
        List<MoneyCirculation> collect = allByAuthUserAndType.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().equals(LocalDate.parse(day))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }
    private static boolean timeValidator(String connectionTime ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(connectionTime);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
    private static boolean timeValidatorYearMonth(String connectionTime ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            format.setLenient(false);
            format.parse(connectionTime);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    public WebResponse<?> getCirculationsByMonth(String day, String type ) {
        if (Objects.isNull(day)) {
            return new WebResponse<>(new ErrorDTO("Day is null", 400));
        }
        Category.Type categoryType = null;
        if (type.equalsIgnoreCase("expense")) {
            categoryType = Category.Type.EXPENSE;
        } else if (type.equalsIgnoreCase("income")) {
            categoryType = Category.Type.INCOME;
        } else {
            return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
        }
        if (!day.matches("\\d{4}-\\d{2}") || timeValidatorYearMonth(day)) {

            return new WebResponse<>(new ErrorDTO("Day is not valid", 400));
        }
        int monthNum= Integer.parseInt(day.substring(5));
        Month month= Month.of(monthNum);
        List<MoneyCirculation> allByAuthUserAndType = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
        List<MoneyCirculation> collect= allByAuthUserAndType.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().getMonth().equals(month)).toList();
        return getTotalResponse(collect);
    }
    public WebResponse<?> getStatisticsFilter(DownloadStatisticRequestDto downloadStatisticRequestDto) {
        List<MoneyCirculation> allByAuthUser = null;
        allByAuthUser = moneyCirculationRepository.findAllByAuthUser(userService.getCurrentUser());
        if (Objects.nonNull(downloadStatisticRequestDto)) {
            Category.Type categoryType = null;
            if (downloadStatisticRequestDto.Type() != null) {
                if (downloadStatisticRequestDto.Type().equalsIgnoreCase("expense")) {
                    categoryType = Category.Type.EXPENSE;
                } else if (downloadStatisticRequestDto.Type().equalsIgnoreCase("income")) {
                    categoryType = Category.Type.INCOME;
                } else {
                    return new WebResponse<>(new ErrorDTO("Type is not valid", 400));
                }
            }
            Category category = null;
            if (downloadStatisticRequestDto.CategoryId() != null) {
                try {
                    category = categoryRepository.findById(downloadStatisticRequestDto.CategoryId()).orElseThrow(() -> new ItemNotFoundException("Category not found"));
                } catch (ItemNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            LocalDate date = null;
            if (downloadStatisticRequestDto.Date() != null) {
                if (!downloadStatisticRequestDto.Date().matches("\\d{4}-\\d{2}-\\d{2}")) {
                    return new WebResponse<>(new ErrorDTO("Start date is not valid", 400));
                }
                try {
                    date = LocalDate.parse(downloadStatisticRequestDto.Date());
                } catch (Exception e) {
                    return new WebResponse<>(new ErrorDTO("Start date is not valid", 400));
                }
            }


            if (categoryType != null && category == null && date == null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndType(userService.getCurrentUser(), categoryType);
            }
            if (categoryType == null && category != null && date == null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndCategory(userService.getCurrentUser(), category);
            }
            if (categoryType == null && category == null && date != null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndCreatedAt(userService.getCurrentUser(), LocalDateTime.from(date));
            }
            if (categoryType != null && category != null && date == null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndTypeAndCategory(userService.getCurrentUser(), categoryType, category);
            }
            if (categoryType != null && category == null && date != null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndTypeAndCreatedAt(userService.getCurrentUser(), categoryType, LocalDateTime.from(date));
            }
            if (categoryType == null && category != null && date != null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndCategoryAndCreatedAt(userService.getCurrentUser(), category, LocalDateTime.from(date));
            }
            if (categoryType != null && category != null && date != null) {
                allByAuthUser = moneyCirculationRepository.findAllByAuthUserAndTypeAndCategoryAndCreatedAt(userService.getCurrentUser(), categoryType, category, LocalDateTime.from(date));
            }
            if (allByAuthUser == null)
                return new WebResponse<>(new ErrorDTO("You have not any transaction", 400));
        }
        return new WebResponse<>(new DataDTO<>(allByAuthUser));
    }
    public WebResponse<?> updateCirculation(Long id, MoneyCirculationDto moneyCirculationDto) {
        if (Objects.isNull(id) || id < 1) {
            return new WebResponse<>(new ErrorDTO("Id is null or 0", 400));
        }
        if (Objects.isNull(moneyCirculationDto)) {
            return new WebResponse<>(new ErrorDTO("Request is null", 400));
        }
        if (Objects.isNull(moneyCirculationDto.amount()) || moneyCirculationDto.amount() <= 0) {
            return new WebResponse<>(new ErrorDTO("Amount is null or 0 please delete this circulation or replace amount", 400));
        }
        if (Objects.isNull(moneyCirculationDto.categoryId()) || moneyCirculationDto.categoryId() <= 0) {
            return new WebResponse<>(new ErrorDTO("Category id is null or 0", 400));
        }
        MoneyCirculation moneyCirculation = null;
        try {
            moneyCirculation = moneyCirculationRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Money circulation not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!moneyCirculation.getAuthUser().getUsername().equals(userService.getCurrentUser().getUsername())) {
            return new WebResponse<>(new ErrorDTO("Permission denied!", 403));
        }
        Category category = null;
        try {
            category = categoryRepository.findById(moneyCirculationDto.categoryId()).orElseThrow(() -> new ItemNotFoundException("Category not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        Category.Type type = moneyCirculation.getType();
        AuthUser currentUser = userService.getCurrentUser();
        if (type.equals(Category.Type.EXPENSE)) {
            if (moneyCirculationDto.amount() > moneyCirculation.getAmount()) {
                double amount = moneyCirculationDto.amount() - moneyCirculation.getAmount();
                double balance = currentUser.getBalance() - amount;
                currentUser.setBalance(balance);
            } else if (moneyCirculationDto.amount() < moneyCirculation.getAmount()) {
                double amount = moneyCirculation.getAmount() - moneyCirculationDto.amount();
                double balance = currentUser.getBalance() + amount;
                currentUser.setBalance(balance);
            }
        } else if (type.equals(Category.Type.INCOME)) {
            if (moneyCirculationDto.amount() > moneyCirculation.getAmount()) {
                double amount = moneyCirculationDto.amount() - moneyCirculation.getAmount();
                double balance = currentUser.getBalance() + amount;
                currentUser.setBalance(balance);
            } else if (moneyCirculationDto.amount() < moneyCirculation.getAmount()) {
                double amount = moneyCirculation.getAmount() - moneyCirculationDto.amount();
                double balance = currentUser.getBalance() - amount;
                currentUser.setBalance(balance);
            }
        }
        if (moneyCirculationDto.subCategoryId() != null && moneyCirculationDto.subCategoryId() > 0) {
            SubCategory subCategory = null;
            try {
                subCategory = subCategoryRepository.findById(moneyCirculationDto.subCategoryId()).orElseThrow(() -> new ItemNotFoundException("Sub category not found"));
            } catch (ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
            moneyCirculation.setSubCategory(subCategory);

        }
        moneyCirculation.setAmount(moneyCirculationDto.amount());
        moneyCirculation.setCategory(category);
        moneyCirculation.setNote(moneyCirculationDto.note());
        moneyCirculation.setType(type);
        moneyCirculationRepository.save(moneyCirculation);
        authUserRepository.save(currentUser);
        return new WebResponse<>(new DataDTO<>(moneyCirculation));

    }

    public WebResponse<?> deleteCirculation(Long id) {
        if (Objects.isNull(id) || id < 1) {
            return new WebResponse<>(new ErrorDTO("Id is null or 0", 400));
        }
        MoneyCirculation moneyCirculation;
        try {
            moneyCirculation = moneyCirculationRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Money circulation not found"));
        } catch (ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!moneyCirculation.getAuthUser().getUsername().equals(userService.getCurrentUser().getUsername())) {
            return new WebResponse<>(new ErrorDTO("Permission denied!", 403));
        }
        Category.Type type = moneyCirculation.getType();
        AuthUser currentUser = userService.getCurrentUser();
        if (type.equals(Category.Type.EXPENSE)) {
            double balance = currentUser.getBalance() + moneyCirculation.getAmount();
            currentUser.setBalance(balance);
        } else if (type.equals(Category.Type.INCOME)) {
            double balance = currentUser.getBalance() - moneyCirculation.getAmount();
            currentUser.setBalance(balance);
        }
        moneyCirculationRepository.delete(moneyCirculation);
        authUserRepository.save(currentUser);
        return new WebResponse<>(new DataDTO<>("Money circulation deleted successfully"));
    }
}
