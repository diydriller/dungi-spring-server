package com.project.batch_server.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ScheduleConfig {
    @Autowired
    Job job;
    @Autowired
    JobLauncher jobLauncher;

    @Scheduled(cron = "30 0 0 * * 0")
    public void executeJob(){
        try{
            jobLauncher.run(
                    job,new JobParametersBuilder()
                            .addString("datatime", LocalDateTime.now().toString())
                            .toJobParameters()
            );

        }
        catch(JobExecutionException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
