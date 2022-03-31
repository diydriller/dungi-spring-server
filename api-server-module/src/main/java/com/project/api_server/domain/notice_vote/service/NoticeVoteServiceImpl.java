package com.project.api_server.domain.notice_vote.service;

import com.project.api_server.application.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.application.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.application.notice_vote.dto.GetNoticeVoteResponseDto;
import com.project.api_server.application.notice_vote.dto.GetVoteItemResponseDto;
import com.project.api_server.domain.room.service.RoomStore;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.model.Vote;
import com.project.common.model.VoteItem;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeVoteServiceImpl implements NoticeVoteService{

    private final RoomStore roomStore;
    private final NoticeVoteStore noticeVoteStore;
    @Override
    @Transactional
    @CacheEvict(value="getNotiveVote",allEntries = true)
    public void createVote(CreateVoteRequestDto voteRequestDto, User user, Long roomId) {
        Room room = roomStore.findRoomEntered(user,roomId);
        noticeVoteStore.saveVote(voteRequestDto,room,user);
    }

    @Override
    @Transactional
    @CacheEvict(value="getNotiveVote",allEntries = true)
    public void createNotice(CreateNoticeRequestDto noticeRequestDto, User user, Long roomId) {
        Room room = roomStore.findRoomEntered(user,roomId);
        noticeVoteStore.saveNotice(noticeRequestDto,room,user);
    }

    @Override
    @Cacheable(key = "{#roomId,#page}",value="getNotiveVote")
    public List<GetNoticeVoteResponseDto> getNoticeVote(Long roomId, User user, int page) {
        Room room = roomStore.findRoomEntered(user,roomId);
        return noticeVoteStore.getNoticeVote(room,user,page);
    }

    @Override
    public GetVoteItemResponseDto getVote(Long roomId, User user, Long voteId) {
        Room room = roomStore.findRoomEntered(user,roomId);
        Vote vote = noticeVoteStore.getVote(voteId);
        return noticeVoteStore.getVoteItemDto(room,vote,user);
    }

    @Override
    public void createVoteChoice(Long roomId, User user, Long voteId, Long choiceId) {
        roomStore.findRoomEntered(user,roomId);
        VoteItem voteItem=noticeVoteStore.getVoteItem(voteId,choiceId);
        noticeVoteStore.createVoteChoice(user,voteItem);
    }
}
