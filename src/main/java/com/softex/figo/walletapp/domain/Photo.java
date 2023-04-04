package com.softex.figo.walletapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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
public class Photo extends Auditable{
   @ManyToOne
    private AuthUser user;
    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;
}
