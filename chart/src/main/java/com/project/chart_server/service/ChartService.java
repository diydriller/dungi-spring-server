package com.project.chart_server.service;

import com.project.common.model.DeleteStatus;
import com.project.common.model.FinishStatus;
import com.project.common.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ChartService {
    private final RoomRepository roomRepository;

    @Transactional
    void chartUpdate(){
        LocalDateTime endDate=LocalDateTime.now();
        LocalDateTime startDate=LocalDateTime.now().minusDays(7);

        roomRepository.updateChart(startDate,endDate,
                DeleteStatus.NOT_DELETED, FinishStatus.FINISHED);
    }

}
