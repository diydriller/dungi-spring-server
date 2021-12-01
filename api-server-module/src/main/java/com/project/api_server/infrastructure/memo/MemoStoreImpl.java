package com.project.api_server.infrastructure.memo;

import com.project.api_server.application.memo.dto.CreateMemoRequestDto;
import com.project.api_server.application.memo.dto.GetMemoResponseDto;
import com.project.api_server.application.memo.dto.MoveMemoRequestDto;
import com.project.api_server.application.memo.dto.UpdateMemoRequestDto;
import com.project.api_server.domain.memo.service.MemoStore;
import com.project.common.error.BaseException;
import com.project.common.model.DeleteStatus;
import com.project.common.model.Memo;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.AUTHORIZATION_ERROR;
import static com.project.common.response.BaseResponseStatus.NOT_EXIST_MEMO;

@Component
@RequiredArgsConstructor
public class MemoStoreImpl implements MemoStore {

    private final MemoRepository memoRepository;


    @Override
    public void saveMemo(User user, Room room, CreateMemoRequestDto requestDto) {
        Memo memo = Memo.createMemo(user,room,requestDto.getMemo(), requestDto.getX(),
                requestDto.getY(), requestDto.getMemoColor());
        memoRepository.save(memo);
    }

    @Override
    public List<GetMemoResponseDto> findAllMemo(User user, Room room) {
        List<Memo> memoList = memoRepository.findAllMemoByRoom(room, DeleteStatus.NOT_DELETED);
        List<GetMemoResponseDto> res = new ArrayList<>();

        for(Memo memo:memoList){
            LocalDateTime time=memo.getCreatedTime();
            String createdAt=time.getMonthValue()+"/"+time.getDayOfMonth()+"/"
                    +time.getHour()+"/"+time.getSecond();
            res.add(new GetMemoResponseDto(memo.getId(),memo.getUser().getProfileImg(),
                    memo.getMemoItem(),memo.getMemoColor(),
                    (memo.getUser().getId()==user.getId()?true:false),createdAt,
                    memo.getXPosition(),memo.getYPosition()));
        }
        return res;
    }

    @Override
    public void updateMemo(User user, Long memoId, UpdateMemoRequestDto requestDto) {

        memoRepository.findMemoById(memoId,DeleteStatus.NOT_DELETED)
                .ifPresentOrElse(
                        (m)->{
                            if(m.getUser().getId()!=user.getId()){
                                throw new BaseException(AUTHORIZATION_ERROR);
                            }
                            m.setMemoColor(requestDto.getMemoColor());
                            m.setMemoItem(requestDto.getMemo());
                            memoRepository.save(m);
                        },
                        ()->{
                            throw new BaseException(NOT_EXIST_MEMO);
                        }
                );
    }

    @Override
    public void moveMemo(User user, Long memoId, MoveMemoRequestDto requestDto) {

        memoRepository.findMemoById(memoId,DeleteStatus.NOT_DELETED)
                .ifPresentOrElse(
                        (m)->{
                            m.setXPosition(requestDto.getX());
                            m.setYPosition(requestDto.getY());
                            memoRepository.save(m);
                        },
                        ()->{
                            throw new BaseException(NOT_EXIST_MEMO);
                        }
                );
    }

    @Override
    public void deleteMemo(User user, Long memoId) {
        memoRepository.findMemoById(memoId,DeleteStatus.NOT_DELETED)
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
