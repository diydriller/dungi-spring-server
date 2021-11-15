package com.project.api_server.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMemoResponseDto {
    private Long memoId;
    private String profileImg;
    private String memo;
    private String memoColor;
    private boolean isOwner;
    private String createdAt;
    private double x;
    private double y;
}
