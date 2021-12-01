package com.project.api_server.domain.memo.service;

import com.project.api_server.domain.room.service.RoomStore;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.api_server.application.memo.dto.CreateMemoRequestDto;
import com.project.api_server.application.memo.dto.GetMemoResponseDto;
import com.project.api_server.application.memo.dto.MoveMemoRequestDto;
import com.project.api_server.application.memo.dto.UpdateMemoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoServiceImpl implements MemoService {
    private final MemoStore memoStore;
    private final RoomStore roomStore;

    // 유저가 방에 입장해있는지 검증 - 메모 생성
    // 메모 생성시 캐시 삭제
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void createMemo(CreateMemoRequestDto requestDto,User user,Long roomId){
        Room room = roomStore.findRoomEntered(user,roomId);
        memoStore.saveMemo(user,room,requestDto);
    }

    // 유저가 방에 입장해있는지 검증 - 메모 조회
    // 캐시 생성
    @Transactional
    @Cacheable(key = "#roomId",value="getMemo")
    public List<GetMemoResponseDto> getMemo(Long roomId,User user) {
        Room room = roomStore.findRoomEntered(user,roomId);
        return memoStore.findAllMemo(user,room);
    }

    // 유저가 방에 입장해있는지 검증 - 메모 수정
    // 캐시 삭제
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void updateMemo(UpdateMemoRequestDto requestDto, User user, Long roomId, Long memoId) {
        roomStore.findRoomEntered(user,roomId);
        memoStore.updateMemo(user,memoId,requestDto);
    }

    // 유저가 방에 입장해있는지 검증 - 메모 이동
    // 캐시 삭제
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void moveMemo(MoveMemoRequestDto requestDto, User user, Long roomId, Long memoId) {
        roomStore.findRoomEntered(user,roomId);
        memoStore.moveMemo(user,memoId,requestDto);
    }

    // 유저가 방에 입장해있는지 검증 - 메모 삭제
    // 캐시 삭제
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void deleteMemo(User user, Long roomId, Long memoId) {
        roomStore.findRoomEntered(user,roomId);
        memoStore.deleteMemo(user,memoId);
    }
}
