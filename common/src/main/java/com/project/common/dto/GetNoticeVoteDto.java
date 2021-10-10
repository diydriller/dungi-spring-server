package com.project.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public interface GetNoticeVoteDto {
    String getType();
    Long getId();
    String getProfileImg();
    String getNotice();
    Long getUserId();
    String getTitle();
    LocalDateTime getCreatedAt();
}
