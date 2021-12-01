package com.project.api_server.notice_vote.service;

import com.project.common.dto.GetNoticeVoteDto;
import com.project.common.error.BaseException;
import com.project.common.model.*;
import com.project.common.repository.UserRoomRepository;
import com.project.redis.infrastrure.redis.RedisServiceImpl;
import com.project.common.infrastructure.cipher.Aes128;
import com.project.common.util.StringUtil;
import com.project.api_server.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.notice_vote.dto.GetNoticeVoteResponseDto;
import com.project.api_server.notice_vote.dto.GetVoteItemResponseDto;
import com.project.common.repository.NoticeVoteRepository;
import com.project.common.repository.UserVoteItemRepository;
import com.project.common.repository.VoteItemRepository;
import com.project.common.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.*;


@Service
@RequiredArgsConstructor
public class NoticeVoteService {

    private final UserRoomRepository userRoomRepository;
    private final NoticeVoteRepository noticeVoteRepository;
    private final RedisServiceImpl redisService;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final UserVoteItemRepository userVoteItemRepository;
    private final Aes128 aes128;

    // 레디스 조회해서 로그인여부 검증
    // 사용자가 방에 있는지와 존재하는 투표선택지인지 검증
    // 투표생성
    @Transactional
    @CacheEvict(value="getNotiveVote",allEntries = true)
    public void createVote(CreateVoteRequestDto voteRequestDto,Long userId,Long roomId) throws Exception {

        User user=redisService.getUser(StringUtil.redisLogin(userId));
        Room room=userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});


        List<VoteItem> voteItemList=new ArrayList<>();
        for(String choice:voteRequestDto.getChoiceArr()){
            voteItemList.add(VoteItem.createVoteItem(choice));
        }
        Vote vote=Vote.createVote(room,user,voteRequestDto.getTitle(),voteItemList);
        noticeVoteRepository.save(vote);
    }

    // 레디스 조회해서 로그인여부 검증
    // 사용자가 방에 있는지와 존재하는 투표선택지인지 검증
    // 공지생성
    @Transactional
    @CacheEvict(value="getNotiveVote",allEntries = true)
    public void createNotice(CreateNoticeRequestDto noticeRequestDto,Long userId,Long roomId) throws Exception {

        User user=redisService.getUser(StringUtil.redisLogin(userId));
        Room room=userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        Notice notice=Notice.createNotice(room,user,noticeRequestDto.getNotice());
        noticeVoteRepository.save(notice);
    }

    // 레디스 조회해서 로그인여부 검증
    // 사용자가 방에 있는지와 존재하는 투표선택지인지 검증
    // 공지/투표 페이징 조회
    @Cacheable(key = "{#roomId,#page}",value="getNotiveVote")
    public List<GetNoticeVoteResponseDto> getNoticeVote(Long roomId, Long userId,int page) throws Exception {

        redisService.getUser(StringUtil.redisLogin(userId));
        userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "created_time");
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

    // 레디스 조회해서 로그인여부 검증
    // 사용자가 방에 있는지와 존재하는 투표선택지인지 검증
    // 투표 , 투표 선택지 , 투표유저 조회
    public GetVoteItemResponseDto getVote(Long roomId, Long userId, Long voteId) throws Exception {

        redisService.getUser(StringUtil.redisLogin(userId));
        Room room=userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        Vote vote=voteRepository.findByIdAndDeleteStatus(voteId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_VOTE);});

        GetVoteItemResponseDto responseDto=new GetVoteItemResponseDto();
        responseDto.setTitle(vote.getTitle());
        List<Long> choiceList=new ArrayList();
        HashMap<Long,Integer> voteCntMap=new HashMap();

        List<GetVoteItemResponseDto.VoteChoiceDto> voteChoiceDtoList = new ArrayList();
        for(VoteItem voteItem:voteItemRepository.findVoteItemByVote(vote)){
            List<User> voteUserList=voteItemRepository.findVoteUserAndDeleteStatus(voteItem,DeleteStatus.NOT_DELETED);
            List<GetVoteItemResponseDto.VoteUserDto> voteUserDtoList=new ArrayList();
            for(User voteUser:voteUserList){
                voteCntMap.put(voteUser.getId(),1);
                voteUserDtoList.add(new GetVoteItemResponseDto.VoteUserDto(
                        aes128.decrypt(voteUser.getProfileImg()), aes128.decrypt(voteUser.getNickname())));
                if(voteUser.getId()==userId){
                    choiceList.add(voteItem.getId());
                }
            }
            voteChoiceDtoList.add(new GetVoteItemResponseDto.VoteChoiceDto(voteItem.getChoice(),voteUserDtoList));
        }
        responseDto.setChoice(voteChoiceDtoList);
        responseDto.setIsFinished(vote.getFinishStatus()==FinishStatus.FINISHED?true:false);
        responseDto.setIsOwner(vote.getUser().getId()==userId?true:false);
        responseDto.setChoiceIdList(choiceList);

        int totalCnt=userRoomRepository.countRoomUserByDeleteStatus(room,DeleteStatus.NOT_DELETED);
        responseDto.setUnVotedMemberCnt(totalCnt-voteCntMap.size());

        return responseDto;
    }

    // 레디스 조회해서 로그인여부 검증
    // 사용자가 방에 있는지와 존재하는 투표선택지인지 검증
    // 선택을 안한경우 투표 선택 생성 / 선택을 한경우 선택과 선택취소를 토글로 구현
    public void createVoteChoice(Long roomId, Long userId, Long voteId, Long choiceId) throws Exception {

        User user=redisService.getUser(StringUtil.redisLogin(userId));
        userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});
        VoteItem voteItem = voteItemRepository.findByVote(voteId,choiceId)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_VOTEITEM);});

        userVoteItemRepository.findByUserAndVoteItem(user,voteItem)
                .ifPresentOrElse(
                        (uvi)->{
                            if(uvi.getDeleteStatus()==DeleteStatus.DELETED){
                                uvi.setDeleteStatus(DeleteStatus.NOT_DELETED);
                            }
                            else{
                                uvi.setDeleteStatus(DeleteStatus.DELETED);
                            }
                            userVoteItemRepository.save(uvi);
                        },()->{
                            UserVoteItem userVoteItem=UserVoteItem.createUserVoteItem(user,voteItem);
                            userVoteItemRepository.save(userVoteItem);
                        }
                );
    }
}
