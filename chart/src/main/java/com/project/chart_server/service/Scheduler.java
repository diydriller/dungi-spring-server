package com.project.chart_server.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class Scheduler {

    private ChartService chartService;

    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "20 * * * * *")
    public void chartUpdate(){
        chartService.chartUpdate();
    }

}


