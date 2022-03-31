package com.project.api_server.application.notice_vote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetNoticeVoteResponseDto {

    private Long id;
    private String profileImg;
    private String notice;
    private Long userId;
    private String title;
    private String isOwner;
    private String isNotice;
    private String createdAt;
}
