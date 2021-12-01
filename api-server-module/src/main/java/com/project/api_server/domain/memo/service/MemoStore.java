package com.project.api_server.domain.memo.service;

import com.project.api_server.application.memo.dto.CreateMemoRequestDto;
import com.project.api_server.application.memo.dto.GetMemoResponseDto;
import com.project.api_server.application.memo.dto.MoveMemoRequestDto;
import com.project.api_server.application.memo.dto.UpdateMemoRequestDto;
import com.project.common.model.Room;
import com.project.common.model.User;

import java.util.List;

public interface MemoStore {

    void saveMemo(User user, Room room, CreateMemoRequestDto requestDto);
    List<GetMemoResponseDto> findAllMemo(User user,Room room);
    void updateMemo(User user , Long memoId , UpdateMemoRequestDto requestDto);
    void moveMemo(User user, Long memoId , MoveMemoRequestDto requestDto);
    void deleteMemo(User user,Long memoId);

}
