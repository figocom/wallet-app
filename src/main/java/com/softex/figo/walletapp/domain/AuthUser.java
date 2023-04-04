package com.softex.figo.walletapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthUser extends Auditable {
    @NotBlank(message = "Name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false, unique = true)
    private String username;
    @JsonIgnore
    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    private String password;
    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Currency currency;
    @Column(nullable = false)
    @Enumerated( EnumType.STRING)
    @Builder.Default
    private Status status=Status.ACTIVE;
    @JsonIgnore
    @Column(nullable = false)
    @Builder.Default
    private Double balance=0.0;
    public enum Status {
        ACTIVE, BLOCKED
    }
}
