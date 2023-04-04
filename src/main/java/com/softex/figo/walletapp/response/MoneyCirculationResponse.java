package com.softex.figo.walletapp.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.softex.figo.walletapp.domain.MoneyCirculation;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MoneyCirculationResponse implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MoneyCirculation> data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String totalAmount;
}
