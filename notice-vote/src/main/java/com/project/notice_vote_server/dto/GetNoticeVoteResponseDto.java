package com.project.notice_vote_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
