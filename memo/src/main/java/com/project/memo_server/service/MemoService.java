package com.project.memo_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.common.error.BaseException;
import com.project.common.model.DeleteStatus;
import com.project.common.model.Memo;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.memo_server.repository.MemoRepository;
import com.project.common.repository.RoomRepository;
import com.project.common.service.RedisService;
import com.project.memo_server.dto.CreateMemoRequestDto;
import com.project.memo_server.dto.GetMemoResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.common.response.BaseResponseStatus.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final RoomRepository roomRepository;
    private final RedisService redisService;

    // 메모 생성
    @Transactional
    public void createMemo(CreateMemoRequestDto memoRequestDto,Long userId,Long roomId) throws JsonProcessingException {

        User user=redisService.getUser("login_"+userId);

        Room room=roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_ROOM);});

        Memo memo=Memo.createMemo(user,room,memoRequestDto.getMemo(), memoRequestDto.getX(),
                memoRequestDto.getY(), memoRequestDto.getMemoColor());
        memoRepository.save(memo);
    }

    //메모 조회
    @Transactional
    public List<GetMemoResponseDto> getMemo(Long roomId,Long userId) throws JsonProcessingException {

        redisService.getUser("login_"+userId);
        Room room=roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_ROOM);});

        List<Memo> memoList=memoRepository.findAllMemoByRoomAndDeleteStatus(room, DeleteStatus.NOT_DELETED);
        List<GetMemoResponseDto> res=new ArrayList<>();

        for(Memo memo:memoList){
            LocalDateTime time=memo.getCreatedTime();
            String createdAt=time.getMonthValue()+"/"+time.getDayOfMonth()+"/"
            +time.getHour()+"/"+time.getSecond();
            res.add(new GetMemoResponseDto(memo.getId(),memo.getUser().getProfileImg(),
                    memo.getMemoItem(),memo.getMemoColor(),
                    (memo.getUser().getId()==userId?true:false),createdAt,
                    memo.getXPosition(),memo.getYPosition()));
        }

        return res;

    }


}
