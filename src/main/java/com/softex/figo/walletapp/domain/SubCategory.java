package com.softex.figo.walletapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class SubCategory extends Auditable {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String icon;
    @ManyToOne
    private Category category;
}
