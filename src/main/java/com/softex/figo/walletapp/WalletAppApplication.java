package com.softex.figo.walletapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softex.figo.walletapp.domain.Category;
import com.softex.figo.walletapp.domain.Currency;
import com.softex.figo.walletapp.dto.CurrencyDTO;
import com.softex.figo.walletapp.mapper.CurrencyMapper;
import com.softex.figo.walletapp.repository.CategoryRepository;
import com.softex.figo.walletapp.repository.CurrencyRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.List;

@SpringBootApplication

@OpenAPIDefinition(
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class WalletAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletAppApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(CurrencyRepository currencyRepository, CategoryRepository categoryRepository) {
        return args -> {
            List<Currency> all = currencyRepository.findAll();
            if (all.size() == 0) {
                ObjectMapper objectMapper = new ObjectMapper();
                File file = new File("data/currencies.json");
                JsonNode jsonArray = objectMapper.readValue(file, JsonNode.class);
                String jsonArrayAsString = objectMapper.writeValueAsString(jsonArray);
                List<CurrencyDTO> currencyDTOS = objectMapper.readValue(jsonArrayAsString, new TypeReference<List<CurrencyDTO>>() {});
                CurrencyMapper currencyMapper = new CurrencyMapper();
                currencyRepository.saveAll(
                        currencyMapper.fromCreateDTOs(currencyDTOS)
                );
            }
            List<Category> categories = categoryRepository.findAll();
            if (categories.size() == 0) {
                categoryRepository.saveAll(
                        List.of(
                                new Category("Food & Drink", "some_icon", Category.Type.EXPENSE),
                                new Category("Transport", "some icon", Category.Type.EXPENSE),
                                new Category("Travel", "some icon", Category.Type.EXPENSE),
                                new Category("Work", "some icon", Category.Type.EXPENSE),
                                new Category("Family & Personal", "some icon", Category.Type.EXPENSE),
                                new Category("Car", "some icon", Category.Type.EXPENSE),
                                new Category("Bills & Fees", "some icon", Category.Type.EXPENSE),
                                new Category("Entertainment", "some icon", Category.Type.EXPENSE),
                                new Category("Healthcare", "some icon", Category.Type.EXPENSE),
                                new Category("Shopping", "some icon", Category.Type.EXPENSE),
                                new Category("Education", "some icon", Category.Type.EXPENSE),
                                new Category("Gifts", "some icon", Category.Type.EXPENSE),
                                new Category("Home", "some icon", Category.Type.EXPENSE),
                                new Category("Other", "some icon", Category.Type.EXPENSE),
                                new Category("Salary", "some icon", Category.Type.INCOME),
                                new Category("Gifts", "some icon", Category.Type.INCOME),
                                new Category("Business", "some icon", Category.Type.INCOME),
                                new Category("Extra Income", "some icon", Category.Type.INCOME),
                                new Category("Loan", "some icon", Category.Type.INCOME),
                                new Category("Other", "some icon", Category.Type.INCOME)
                        )
                );
            }
        };
    }
}
