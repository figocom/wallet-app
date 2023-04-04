package com.softex.figo.walletapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoneyCirculation extends Auditable{
    @Column(nullable = false)
    private Double amount;
    @ManyToOne
    private Category category;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    private SubCategory subCategory;
    @ManyToOne
    private Currency currency;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category.Type type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String note;
    @JsonIgnore
    @ManyToOne
    private AuthUser authUser;
}
