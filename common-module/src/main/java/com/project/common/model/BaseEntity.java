package com.project.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @JsonIgnore
    @CreatedDate
    @Column(name="created_time")
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedDate
    @Column(name="modifed_time")
    private LocalDateTime modifiedTime;
}
