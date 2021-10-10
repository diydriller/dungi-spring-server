package com.project.notice_vote_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
import com.project.notice_vote_server.dto.CreateNoticeRequestDto;
import com.project.notice_vote_server.dto.CreateVoteRequestDto;
import com.project.notice_vote_server.service.NoticeVoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.JSON_OBJECT_MAPPING_ERROR;
import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@AllArgsConstructor
public class NoticeVoteController {

    private final RedisService redisService;
    private final NoticeVoteService noticeVoteService;
    private final JwtService jwtService;

    // 투표 생성
    @PostMapping(value = "/room/{roomId}/vote")
    public BaseResponse createVote(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestBody @Valid CreateVoteRequestDto voteRequestDto
            ) throws BaseException {
        try {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            noticeVoteService.createVote(voteRequestDto,userId,roomId);

            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    // 공지 생성
    @PostMapping(value = "/room/{roomId}/notice")
    public BaseResponse createNotice(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestBody @Valid CreateNoticeRequestDto noticeRequestDto
    ) throws BaseException {
        try {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            noticeVoteService.createNotice(noticeRequestDto,userId,roomId);

            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    // 공지 투표 조회
    @GetMapping(value = "/room/{roomId}/noticeVote")
    public BaseResponse<?> getNoticeVote(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestParam("page") int page
    ) throws BaseException {
        try {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(noticeVoteService.getNoticeVote(roomId,userId,page));
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

}
