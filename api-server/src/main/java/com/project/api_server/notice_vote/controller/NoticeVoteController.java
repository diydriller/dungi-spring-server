package com.project.api_server.notice_vote.controller;

import com.project.common.aop.BaseExceptionResponseAnnotation;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.api_server.notice_vote.dto.CreateNoticeRequestDto;
import com.project.api_server.notice_vote.dto.CreateVoteRequestDto;
import com.project.api_server.notice_vote.service.NoticeVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class NoticeVoteController {

    private final NoticeVoteService noticeVoteService;
    private final JwtService jwtService;

    // 투표 생성
    @BaseExceptionResponseAnnotation
    @PostMapping(value = "/room/{roomId}/vote")
    public BaseResponse createVote(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestBody @Valid CreateVoteRequestDto voteRequestDto
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            noticeVoteService.createVote(voteRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 공지 생성
    @BaseExceptionResponseAnnotation
    @PostMapping(value = "/room/{roomId}/notice")
    public BaseResponse createNotice(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestBody @Valid CreateNoticeRequestDto noticeRequestDto
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            noticeVoteService.createNotice(noticeRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 공지 투표 조회
    @BaseExceptionResponseAnnotation
    @GetMapping(value = "/room/{roomId}/noticeVote")
    public BaseResponse<?> getNoticeVote(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestParam("page") int page
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(noticeVoteService.getNoticeVote(roomId,userId,page));
    }

    // 투표 조회
    @BaseExceptionResponseAnnotation
    @GetMapping(value = "/room/{roomId}/vote/{voteId}")
    public BaseResponse<?> getVote(
        @PathVariable Long roomId,
        @RequestHeader(value = "access_token") String token,
        @PathVariable Long voteId
    ) throws Exception {
        Long userId=jwtService.verifyTokenAndGetUserId(token);
        return new BaseResponse(noticeVoteService.getVote(roomId,userId,voteId));
    }

    @BaseExceptionResponseAnnotation
    @PatchMapping("/room/{roomId}/vote/{voteId}/choice/{choiceId}")
    public BaseResponse<?> createVoteChoice(
        @PathVariable  Long roomId,
        @RequestHeader(value = "access_token") String token,
        @PathVariable Long voteId,
        @PathVariable Long choiceId
    ) throws Exception {
        Long userId=jwtService.verifyTokenAndGetUserId(token);
        noticeVoteService.createVoteChoice(roomId,userId,voteId,choiceId);
        return new BaseResponse(SUCCESS);
    }


}
