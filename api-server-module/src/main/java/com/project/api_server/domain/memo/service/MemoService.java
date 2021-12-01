package com.project.api_server.domain.memo.service;

import com.project.api_server.application.memo.dto.CreateMemoRequestDto;
import com.project.api_server.application.memo.dto.GetMemoResponseDto;
import com.project.api_server.application.memo.dto.MoveMemoRequestDto;
import com.project.api_server.application.memo.dto.UpdateMemoRequestDto;
import com.project.common.model.User;
import java.util.List;

public interface MemoService {
    void createMemo(CreateMemoRequestDto memoRequestDto, User user, Long roomId) throws Exception;
    List<GetMemoResponseDto> getMemo(Long roomId, User user) throws Exception;
    void updateMemo(UpdateMemoRequestDto memoRequestDto, User user, Long roomId, Long memoId);
    void moveMemo(MoveMemoRequestDto memoRequestDto, User user, Long roomId, Long memoId);
    void deleteMemo(User user, Long roomId, Long memoId);
}
