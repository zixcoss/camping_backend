package com.camp.camping_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "create_at", columnDefinition = "timestamp", updatable = false, nullable = false)
    @CreatedDate
    private Instant createAt;

    @Column(name = "update_at", columnDefinition = "timestamp", insertable = false)
    @LastModifiedDate
    private Instant updateAt;
}
