package com.softex.figo.walletapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Note extends Auditable{

    private String title;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    private AuthUser user;
}
