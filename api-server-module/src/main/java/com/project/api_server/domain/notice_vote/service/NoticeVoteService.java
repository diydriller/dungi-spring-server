package com.project.api_server.domain.notice_vote.service;

import com.project.api_server.application.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.application.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.application.notice_vote.dto.GetNoticeVoteResponseDto;
import com.project.api_server.application.notice_vote.dto.GetVoteItemResponseDto;
import com.project.common.model.User;

import java.util.List;

public interface NoticeVoteService {
    void createVote(CreateVoteRequestDto voteRequestDto, User user, Long roomId);
    void createNotice(CreateNoticeRequestDto noticeRequestDto, User user, Long roomId);

    List<GetNoticeVoteResponseDto> getNoticeVote(Long roomId, User user, int page);

    GetVoteItemResponseDto getVote(Long roomId, User user, Long voteId);

    void createVoteChoice(Long roomId, User user, Long voteId, Long choiceId);
}
