package com.project.api_server.perfomance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Setter
@Getter
@ToString
@RequestScope
@Component
public class PerformanceLog {
    private String apiPath;
    private String apiMethod;
    private long requestTime;
    private long requestProcessTime;
    private long queryCount;
    private long queryProcessTime;

    public void addQueryCount(){
        queryCount++;
    }
    public void addQueryProcessTime(Long time){
        queryProcessTime+=time;
    }
}
