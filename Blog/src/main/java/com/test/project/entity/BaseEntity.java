package com.test.project.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //생성 되었을 때 시간을 만들어줌
    @Column(updatable = false) //updatable 수정시 관여 안함
    private LocalDateTime createdTime;

    @UpdateTimestamp //업데이트 되었을 때 시간
    @Column(insertable = false) //insertable 입력시 관여 안함
    private LocalDateTime updatedTime;
}
