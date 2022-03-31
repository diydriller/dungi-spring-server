package com.project.api_server.domain.notice_vote.service;

import com.project.api_server.application.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.application.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.application.notice_vote.dto.GetNoticeVoteResponseDto;
import com.project.api_server.application.notice_vote.dto.GetVoteItemResponseDto;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.model.Vote;
import com.project.common.model.VoteItem;

import java.util.List;

public interface NoticeVoteStore {
    void saveVote(CreateVoteRequestDto voteRequestDto, Room room, User user);

    void saveNotice(CreateNoticeRequestDto noticeRequestDto, Room room, User user);

    List<GetNoticeVoteResponseDto> getNoticeVote(Room room, User user, int page);

    Vote getVote(Long voteId);

    GetVoteItemResponseDto getVoteItemDto(Room room, Vote vote, User user);

    VoteItem getVoteItem(Long voteId, Long choiceId);

    void createVoteChoice( User user, VoteItem voteItem);
}
