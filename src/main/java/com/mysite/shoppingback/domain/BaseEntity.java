package com.mysite.shoppingback.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {

    @CreatedDate
    @Column( name = "regdate", updatable = false)
    private LocalDateTime regDate;  //등록날짜 (데이터 생성 날짜)

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;  //수정날짜(마지막 업데이트 날짜)
}
