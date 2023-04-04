package com.softex.figo.walletapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan extends Auditable{
    @Column(nullable = false)
    private Double amount;
    @ManyToOne
    private Category category;
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private AuthUser authUser;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;
    public enum PlanType {
        Daily, Weekly, Yearly
    }
}
