package com.project.api_server.application.notice_vote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetVoteItemResponseDto {
    private String title;
    private List<VoteChoiceDto> choice;
    private List<Long> choiceIdList;
    private Boolean isOwner;
    private Boolean isFinished;
    private Integer unVotedMemberCnt;

    @Data
    @AllArgsConstructor
    public static class VoteChoiceDto{
        private String choice;
        private List<VoteUserDto> voteUser;
    }

    @Data
    @AllArgsConstructor
    public static class VoteUserDto{
        private String profileImg;
        private String nickname;
    }
}
