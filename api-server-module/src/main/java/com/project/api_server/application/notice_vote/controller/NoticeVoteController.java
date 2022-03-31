package com.project.api_server.application.notice_vote.controller;

import com.project.api_server.application.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.application.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.domain.notice_vote.service.NoticeVoteService;
import com.project.common.error.AuthenticationException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Optional;

import static com.project.common.response.BaseResponseStatus.AUTHENTICATION_ERROR;
import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class NoticeVoteController {

    private final NoticeVoteService noticeVoteService;
    private String LOGIN_USER="login_user";
    // 투표 생성
    @PostMapping(value = "/room/{roomId}/vote")
    public BaseResponse createVote(
            @PathVariable Long roomId,
            @RequestBody @Valid CreateVoteRequestDto voteRequestDto, HttpSession session
    ){
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            noticeVoteService.createVote(voteRequestDto,user,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 공지 생성
    @PostMapping(value = "/room/{roomId}/notice")
    public BaseResponse createNotice(
            @PathVariable Long roomId,
            @RequestBody @Valid CreateNoticeRequestDto noticeRequestDto,HttpSession session
    ) {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            noticeVoteService.createNotice(noticeRequestDto,user,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 공지 투표 조회
    @GetMapping(value = "/room/{roomId}/noticeVote")
    public BaseResponse<?> getNoticeVote(
            @PathVariable Long roomId,
            @RequestParam("page") int page,HttpSession session
    ) {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            return new BaseResponse(noticeVoteService.getNoticeVote(roomId,user,page));
    }

    // 투표 조회
    @GetMapping(value = "/room/{roomId}/vote/{voteId}")
    public BaseResponse<?> getVote(
        @PathVariable Long roomId,
        @PathVariable Long voteId,HttpSession session
    )  {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        return new BaseResponse(noticeVoteService.getVote(roomId,user,voteId));
    }

    @PatchMapping("/room/{roomId}/vote/{voteId}/choice/{choiceId}")
    public BaseResponse<?> createVoteChoice(
        @PathVariable  Long roomId,
        @PathVariable Long voteId,
        @PathVariable Long choiceId,HttpSession session
    ) {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        noticeVoteService.createVoteChoice(roomId,user,voteId,choiceId);
        return new BaseResponse(SUCCESS);
    }


}
