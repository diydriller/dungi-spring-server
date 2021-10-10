package com.project.memo_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
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
