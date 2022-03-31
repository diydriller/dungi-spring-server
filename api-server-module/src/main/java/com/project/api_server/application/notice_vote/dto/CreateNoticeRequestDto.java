package com.project.api_server.application.notice_vote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNoticeRequestDto {
    @NotEmpty(message = "notice is empty")
    private String notice;
}
