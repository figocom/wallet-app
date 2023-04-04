package com.softex.figo.walletapp.service;

import com.softex.figo.walletapp.domain.AuthUser;
import com.softex.figo.walletapp.domain.Category;
import com.softex.figo.walletapp.domain.Plan;
import com.softex.figo.walletapp.dto.PlanDto;
import com.softex.figo.walletapp.exception.ItemNotFoundException;
import com.softex.figo.walletapp.repository.CategoryRepository;
import com.softex.figo.walletapp.repository.PlanRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.PlanResponse;
import com.softex.figo.walletapp.response.WebResponse;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public PlanService(PlanRepository planRepository, CategoryRepository categoryRepository, UserService userService) {
        this.planRepository = planRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public WebResponse<?> createPlan(PlanDto planDto) {
        Category category = null;
        if (Objects.nonNull(planDto.getCategoryId())) {
            try {
                category = categoryRepository.findByIdAndDeleted(planDto.getCategoryId(), false).orElseThrow(() -> new ItemNotFoundException("Sub Category not found"));
            } catch (ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        AuthUser currentUser = userService.getCurrentUser();
        Plan plan = new Plan(Double.valueOf(planDto.getAmount()), category, currentUser, Plan.PlanType.valueOf(planDto.getPlanType()));
        Plan save = planRepository.save(plan);
        return new WebResponse<>(new DataDTO<>(save));
    }

    public WebResponse<?> getAll() {
        List<Plan> all = planRepository.findAllByAuthUser(userService.getCurrentUser());
        return new WebResponse<>(new DataDTO<>(all));
    }

    private WebResponse<?> getTotalResponse(List<Plan> allBySubCategoryAndAuthUserAndType) {
        String totalAmount = String.valueOf(allBySubCategoryAndAuthUserAndType.stream().map(Plan::getAmount).reduce(0.0, Double::sum));
        PlanResponse planResponse = new PlanResponse(allBySubCategoryAndAuthUserAndType, allBySubCategoryAndAuthUserAndType.size(), totalAmount);
        return new WebResponse<>(new DataDTO<>(planResponse));
    }

    public WebResponse<?> getDailyPlans() {
        List<Plan> allByAuthUser = planRepository.findAllByAuthUser(userService.getCurrentUser());
        List<Plan> collect = allByAuthUser.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().equals(LocalDate.now())).collect(Collectors.toList());
        return getTotalResponse(collect);
    }

    public WebResponse<?> getWeeklyPlans() {
        List<Plan> allByAuthUser = planRepository.findAllByAuthUser(userService.getCurrentUser());
        List<Plan> collect = allByAuthUser.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(7))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }

    public WebResponse<?> getMonthlyPlans() {
        List<Plan> allByAuthUser = planRepository.findAllByAuthUser(userService.getCurrentUser());
        List<Plan> collect = allByAuthUser.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(30))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }

    public WebResponse<?> getYearlyPlans() {
        List<Plan> allByAuthUser = planRepository.findAllByAuthUser(userService.getCurrentUser());
        List<Plan> collect = allByAuthUser.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().isAfter(LocalDate.now().minusDays(365))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }

    public WebResponse<?> getDailyPlansByDay(String day) {

        if (Objects.isNull(day)) {
            return new WebResponse<>(new ErrorDTO("Day is null", 400));
        }

        if (!day.matches("\\d{4}-\\d{2}-\\d{2}") || timeValidator(day)) {

            return new WebResponse<>(new ErrorDTO("Day is not valid", 400));
        }
        List<Plan> allByAuthUser = planRepository.findAllByAuthUser(userService.getCurrentUser());
        List<Plan> collect = allByAuthUser.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().equals(LocalDate.parse(day))).collect(Collectors.toList());
        return getTotalResponse(collect);
    }

    public WebResponse<?> getPlansByMonth(String month) {
        if (Objects.isNull(month)) {
            return new WebResponse<>(new ErrorDTO("Day is null", 400));
        }

        if (!month.matches("\\d{4}-\\d{2}") || timeValidatorYearMonth(month)) {

            return new WebResponse<>(new ErrorDTO("Day is not valid", 400));
        }
        int monthNum = Integer.parseInt(month.substring(5));
        Month monthVal = Month.of(monthNum);
        List<Plan> allByAuthUser = planRepository.findAllByAuthUser(userService.getCurrentUser());
        List<Plan> collect = allByAuthUser.stream().filter(moneyCirculation -> moneyCirculation.getCreatedAt().toLocalDate().getMonth().equals(monthVal)).toList();
        return getTotalResponse(collect);
    }

    private static boolean timeValidator(String connectionTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(connectionTime);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private static boolean timeValidatorYearMonth(String connectionTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            format.setLenient(false);
            format.parse(connectionTime);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}
