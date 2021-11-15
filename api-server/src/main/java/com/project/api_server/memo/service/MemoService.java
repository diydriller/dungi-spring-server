package com.project.api_server.memo.service;

import com.project.common.error.BaseException;
import com.project.common.model.DeleteStatus;
import com.project.common.model.Memo;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.repository.UserRoomRepository;
import com.project.common.service.RedisService;
import com.project.common.util.StringUtil;
import com.project.api_server.memo.dto.CreateMemoRequestDto;
import com.project.api_server.memo.dto.GetMemoResponseDto;
import com.project.api_server.memo.dto.MoveMemoRequestDto;
import com.project.api_server.memo.dto.UpdateMemoRequestDto;
import com.project.common.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final RedisService redisService;
    private final UserRoomRepository userRoomRepository;

    // 메모 생성
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void createMemo(CreateMemoRequestDto memoRequestDto,Long userId,Long roomId) throws Exception {

        User user = redisService.getUser(StringUtil.redisLogin(userId));
        Room room = userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        Memo memo=Memo.createMemo(user,room,memoRequestDto.getMemo(), memoRequestDto.getX(),
                memoRequestDto.getY(), memoRequestDto.getMemoColor());
        memoRepository.save(memo);
    }

    //메모 조회
    @Transactional
    @Cacheable(key = "#roomId",value="getMemo")
    public List<GetMemoResponseDto> getMemo(Long roomId,Long userId) throws Exception {

        redisService.getUser(StringUtil.redisLogin(userId));
        Room room = userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

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

    // 메모 수정
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void updateMemo(UpdateMemoRequestDto memoRequestDto, Long userId, Long roomId, Long memoId)
            throws Exception {
        redisService.getUser(StringUtil.redisLogin(userId));
        userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        memoRepository.findMemoByIdAndDeleteStatus(memoId,DeleteStatus.NOT_DELETED)
                .ifPresentOrElse(
                        (m)->{
                            if(m.getUser().getId()!=userId){
                                throw new BaseException(NOT_EXIST_AUTHORIZATION);
                            }
                            m.setMemoColor(memoRequestDto.getMemoColor());
                            m.setMemoItem(memoRequestDto.getMemo());
                            memoRepository.save(m);
                        },
                        ()->{
                            throw new BaseException(NOT_EXIST_MEMO);
                        }
                );
    }

    // 메모 이동
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void moveMemo(MoveMemoRequestDto memoRequestDto, Long userId, Long roomId, Long memoId)
            throws Exception {
        redisService.getUser(StringUtil.redisLogin(userId));
        userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});
        memoRepository.findMemoByIdAndDeleteStatus(memoId,DeleteStatus.NOT_DELETED)
                .ifPresentOrElse(
                        (m)->{
                            m.setXPosition(memoRequestDto.getX());
                            m.setYPosition(memoRequestDto.getY());
                            memoRepository.save(m);
                        },
                        ()->{
                            throw new BaseException(NOT_EXIST_MEMO);
                        }
                );
    }

    // 메모 삭제
    @Transactional
    @CacheEvict(key = "#roomId",value="getMemo")
    public void deleteMemo(MoveMemoRequestDto memoRequestDto, Long userId, Long roomId, Long memoId) throws Exception {
        redisService.getUser(StringUtil.redisLogin(userId));
        userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});
        memoRepository.findMemoByIdAndDeleteStatus(memoId,DeleteStatus.NOT_DELETED)
                .ifPresentOrElse(
                        (m)->{
                            m.setDeleteStatus(DeleteStatus.DELETED);
                            memoRepository.save(m);
                        },
                        ()->{
                            throw new BaseException(NOT_EXIST_MEMO);
                        }
                );
    }
}
