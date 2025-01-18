package com.backend.ServerPrac.global.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass 
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

// @MappedSuperclass : 이 클래스를 상속시켰을 때, JPA가 자식 클래스의 칼럼에 부모의 칼럼이 들어가도록함.
// @EntityListeners(AuditingEntityListener.class) : 엔티티 생성 및 수정 시점을 자동으로 기록함.
// abstract : 추상 클래스. 인스턴스화할 수 없다.