package com.project.notice_vote_server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.common.dto.GetNoticeVoteDto;
import com.project.common.error.BaseException;
import com.project.common.model.*;
import com.project.common.repository.MemoRepository;
import com.project.notice_vote_server.repository.NoticeVoteRepository;
import com.project.common.repository.RoomRepository;
import com.project.common.service.RedisService;
import com.project.notice_vote_server.dto.CreateNoticeRequestDto;
import com.project.notice_vote_server.dto.CreateVoteRequestDto;
import com.project.notice_vote_server.dto.GetNoticeVoteResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.NOT_EXIST_ROOM;


@Service
@AllArgsConstructor
public class NoticeVoteService {


    private final NoticeVoteRepository noticeVoteRepository;
    private final RoomRepository roomRepository;
    private final RedisService redisService;

    // 투표생성
    @Transactional
    public void createVote(CreateVoteRequestDto voteRequestDto,Long userId,Long roomId) throws JsonProcessingException {

        User user=redisService.getUser("login_"+userId);

        Room room=roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_ROOM);});

        List<VoteItem> voteItemList=new ArrayList<>();
        for(String choice:voteRequestDto.getChoiceArr()){
            voteItemList.add(VoteItem.createVoteItem(choice));
        }
        Vote vote=Vote.createVote(room,user,voteRequestDto.getTitle(),voteItemList);
        noticeVoteRepository.save(vote);
    }

    // 공지생성
    @Transactional
    public void createNotice(CreateNoticeRequestDto noticeRequestDto,Long userId,Long roomId) throws JsonProcessingException {

        User user=redisService.getUser("login_"+userId);
        Room room=roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_ROOM);});

        Notice notice=Notice.createNotice(room,user,noticeRequestDto.getNotice());
        noticeVoteRepository.save(notice);
    }

    // 공지 투표 조회
    public List<GetNoticeVoteResponseDto> getNoticeVote(Long roomId, Long userId,int page) throws JsonProcessingException {

        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "created_time");

        redisService.getUser("login_"+userId);

        roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_ROOM);});

        Page<GetNoticeVoteDto> noticeVoteList=noticeVoteRepository.findAllNoticeVoteByRoomAndDeleteStatus(roomId,
                DeleteStatus.NOT_DELETED,
                pageRequest);

        List<GetNoticeVoteResponseDto> res=new ArrayList<>();

        for(GetNoticeVoteDto noticeVote:noticeVoteList){

            LocalDateTime createdAt=noticeVote.getCreatedAt();
            String time=createdAt.getMonthValue()+"/"+createdAt.getDayOfMonth()+"/"
                    +createdAt.getHour()+"/"+createdAt.getMinute();

            res.add(new GetNoticeVoteResponseDto(
                    noticeVote.getId(),noticeVote.getProfileImg()
                    ,noticeVote.getNotice(),noticeVote.getUserId()
                    ,noticeVote.getTitle(),(noticeVote.getUserId()==userId?"Y":"N")
                    ,(noticeVote.getType().equals("N")?"Y":"N"),time
            ));
        }
        return res;
    }
}
