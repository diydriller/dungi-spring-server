package com.project.notice_vote_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
