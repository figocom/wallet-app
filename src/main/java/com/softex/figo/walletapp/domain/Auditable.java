package com.softex.figo.walletapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;
    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime updatedAt;
    @JsonIgnore
    @LastModifiedBy
    private Long updatedBy;
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @JsonIgnore
    @Column(name = "deleted_by")
    private Long deletedBy;
    @Column(nullable = false , columnDefinition = "boolean default false")
    private boolean deleted;
}
