package com.softex.figo.walletapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Category extends Auditable {

    @NonNull
    @Column(name = "name", nullable = false)

    private String name;
    @NonNull
    @Column(nullable = false)
    private String icon;
    @NonNull
    @Column(nullable = false , name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;
    public enum Type {
        INCOME, EXPENSE
    }

}
