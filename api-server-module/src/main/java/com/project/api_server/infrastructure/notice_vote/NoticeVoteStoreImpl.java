package com.project.api_server.infrastructure.notice_vote;

import com.project.api_server.application.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.application.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.application.notice_vote.dto.GetNoticeVoteResponseDto;
import com.project.api_server.application.notice_vote.dto.GetVoteItemResponseDto;
import com.project.api_server.domain.notice_vote.service.NoticeVoteStore;
import com.project.common.dto.GetNoticeVoteDto;
import com.project.common.error.BaseException;
import com.project.common.model.*;
import com.project.common.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.NOT_EXIST_VOTE;
import static com.project.common.response.BaseResponseStatus.NOT_EXIST_VOTEITEM;

@Repository
@RequiredArgsConstructor
public class NoticeVoteStoreImpl implements NoticeVoteStore {

    private final NoticeVoteRepository noticeVoteRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserVoteItemRepository userVoteItemRepository;

    @Override
    public void saveVote(CreateVoteRequestDto voteRequestDto, Room room, User user) {
        List<VoteItem> voteItemList=new ArrayList<>();
        for(String choice:voteRequestDto.getChoiceArr()){
            voteItemList.add(VoteItem.createVoteItem(choice));
        }
        Vote vote=Vote.createVote(room,user,voteRequestDto.getTitle(),voteItemList);
        noticeVoteRepository.save(vote);
    }

    @Override
    public void saveNotice(CreateNoticeRequestDto noticeRequestDto, Room room, User user) {
        Notice notice=Notice.createNotice(room,user,noticeRequestDto.getNotice());
        noticeVoteRepository.save(notice);
    }

    @Override
    public List<GetNoticeVoteResponseDto> getNoticeVote(Room room, User user, int page) {
        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "created_time");
        Page<GetNoticeVoteDto> noticeVoteList=noticeVoteRepository.findAllNoticeVoteByRoomAndDeleteStatus(room.getId(),
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
                    ,noticeVote.getTitle(),(noticeVote.getUserId()==user.getId()?"Y":"N")
                    ,(noticeVote.getType().equals("N")?"Y":"N"),time
            ));
        }
        return res;
    }

    @Override
    public Vote getVote(Long voteId) {
        return voteRepository.findByIdAndDeleteStatus(voteId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_VOTE);});
    }

    @Override
    public GetVoteItemResponseDto getVoteItemDto(Room room, Vote vote, User user) {

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
                        voteUser.getProfileImg(), voteUser.getNickname()));
                if(voteUser.getId()==user.getId()){
                    choiceList.add(voteItem.getId());
                }
            }
            voteChoiceDtoList.add(new GetVoteItemResponseDto.VoteChoiceDto(voteItem.getChoice(),voteUserDtoList));
        }
        responseDto.setChoice(voteChoiceDtoList);
        responseDto.setIsFinished(vote.getFinishStatus()==FinishStatus.FINISHED?true:false);
        responseDto.setIsOwner(vote.getUser().getId()==user.getId()?true:false);
        responseDto.setChoiceIdList(choiceList);

        int totalCnt=userRoomRepository.countRoomUserByDeleteStatus(room,DeleteStatus.NOT_DELETED);
        responseDto.setUnVotedMemberCnt(totalCnt-voteCntMap.size());

        return responseDto;
    }

    @Override
    public VoteItem getVoteItem(Long voteId, Long choiceId) {
        return voteItemRepository.findByVote(voteId,choiceId)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_VOTEITEM);});
    }

    @Override
    public void createVoteChoice(User user, VoteItem voteItem) {
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
