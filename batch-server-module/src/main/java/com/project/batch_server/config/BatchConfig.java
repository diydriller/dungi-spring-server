package com.project.batch_server.config;

import com.project.batch_server.writer.ConsoleItemWriter;
import com.project.common.model.FinishStatus;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobs;
    private final StepBuilderFactory steps;
    private final DataSource dataSource;
    private final RoomRepository roomRepository;
    private final ConsoleItemWriter writer;
    private final int chunkSize = 10;

    @Bean
    public Step step1(){
        return steps.get("step1")
                .<Room,User>chunk(chunkSize)
                .reader(mateUpdatePagingReader())
                .processor(mateUpdatePagingProcessor())
                .writer(mateUpdateBatchItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Room> mateUpdatePagingReader() {
        return new JdbcCursorItemReaderBuilder<Room>()
                .sql("SELECT r.room_id AS id  FROM Room r")
                .rowMapper(new BeanPropertyRowMapper<>(Room.class))
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .name("mateUpdatePagingReader")
                .build();
    }

    @Bean
    public ItemProcessor<Room,User> mateUpdatePagingProcessor() {
        return item -> {
            LocalDateTime startDate=LocalDateTime.now().minusDays(7);
            LocalDateTime endDate=LocalDateTime.now().plusDays(1);
            User users=new User(Long.parseLong(String.valueOf(roomRepository.findBestMate(item.getId(),startDate,endDate, FinishStatus.FINISHED)[0])));
            System.out.println(">>> "+users.getId());
            return users;
        };
    }

    @Bean
    public JdbcBatchItemWriter<User> mateUpdateBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("UPDATE Users u SET u.best_mate_count=u.best_mate_count+1 WHERE u.users_id=:id")
                .beanMapped()
                .build();
    }

    @Bean
    public Job mateUpdateJob(){
        return jobs.get("mateUpdateJob")
                .start(step1())
                .build();
    }
}
