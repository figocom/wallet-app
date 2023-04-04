package com.softex.figo.walletapp.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.softex.figo.walletapp.domain.MoneyCirculation;
import com.softex.figo.walletapp.domain.Plan;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlanResponse implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Plan> data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String totalAmount;
}
