package com.project.api_server.notice_vote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CreateVoteRequestDto {
    @NotEmpty(message = "title is empty")
    private String title;

    @Size(min=1,message="choiceArr is empty")
    private List<@NotEmpty(message = "choice is empty") String> choiceArr=new ArrayList<>();
}
