package com.softex.figo.walletapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softex.figo.walletapp.config.WebClientConfig;
import com.softex.figo.walletapp.domain.Currency;
import com.softex.figo.walletapp.dto.ConvertCurrencyDto;
import com.softex.figo.walletapp.repository.CurrencyRepository;
import com.softex.figo.walletapp.response.DataDTO;
import com.softex.figo.walletapp.response.ErrorDTO;
import com.softex.figo.walletapp.response.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ValuateConvertService {
    private final CurrencyRepository currencyRepository;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ValuateConvertService(CurrencyRepository currencyRepository, WebClientConfig webClientConfig) {
        this.currencyRepository = currencyRepository;
        this.webClientConfig = webClientConfig;
    }

    public WebResponse<?> convert(String from, String to, String amount) {
        if (from.equals(to)) {
            return new WebResponse<>(new DataDTO<>(Double.parseDouble(amount)));
        }
        if (!StringUtils.isNumeric(amount)) {
            return new WebResponse<>(new ErrorDTO("Bad request", 400));
        }
        if (Double.parseDouble(amount) <= 0) {
            return new WebResponse<>(new ErrorDTO("Bad request", 400));
        }
        Optional<Currency> byCcy = currencyRepository.findByCcy(from);
        Optional<Currency> byCcy1 = currencyRepository.findByCcy(to);
        if (byCcy.isEmpty() || byCcy1.isEmpty()) {
            return new WebResponse<>(new ErrorDTO("Bad request", 400));
        }
        String fromVal = "1";
        String toVal = "1";
        var webClient = webClientConfig.webClientWithTimeout();
        if (!from.equals("UZS")) {
            List<ConvertCurrencyDto> resultFROM = getConvertCurrencyDtos(from, webClient);
            if (Objects.isNull(resultFROM) || resultFROM.isEmpty()) {
                return new WebResponse<>(new ErrorDTO("Bad request", 400));
            }
            fromVal = Objects.requireNonNull(resultFROM.get(0)).getRate();
        }
        if (!to.equals("UZS")) {
               List<ConvertCurrencyDto> resultTO = getConvertCurrencyDtos(to, webClient);
            if (Objects.isNull(resultTO) || resultTO.isEmpty()) {
                return new WebResponse<>(new ErrorDTO("Bad request", 400));
            }
            toVal = Objects.requireNonNull(resultTO.get(0)).getRate();
        }
        double result = Double.parseDouble(fromVal) / Double.parseDouble(toVal) * Double.parseDouble(amount);
        return new WebResponse<>(new DataDTO<>(result));

    }

    private List<ConvertCurrencyDto> getConvertCurrencyDtos(String from, WebClient webClient) {
        String fromUrl = "/" + from + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String resultStr = webClient.get()
                .uri(String.join("", fromUrl))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
        JsonNode jsonArray;
        List<ConvertCurrencyDto> resultFROM;
        try {
            jsonArray = objectMapper.readValue(resultStr, JsonNode.class);
            String jsonArrayAsString = objectMapper.writeValueAsString(jsonArray);
            resultFROM = objectMapper.readValue(jsonArrayAsString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return resultFROM;
    }
}
