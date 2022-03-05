package com.project.api_server.perfomance.extractor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RequestApi {
    private String path;
    private String method;
}
