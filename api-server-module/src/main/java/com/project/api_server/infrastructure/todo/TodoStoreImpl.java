package com.project.api_server.infrastructure.todo;

import com.project.api_server.application.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.application.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.application.todo.dto.GetRepeatTodoResponseDto;
import com.project.api_server.application.todo.dto.GetTodayTodoResponseDto;
import com.project.api_server.domain.todo.service.TodoStore;
import com.project.common.model.*;
import com.project.common.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoStoreImpl implements TodoStore {

    private final TodoRepository todoRepository;

    @Override
    public void saveTodayTodo(CreateTodayTodoRequestDto requestDto, User user, Room room) {

        String[]time=requestDto.getTime().split("/");
        int year=Integer.parseInt(time[0]); int month=Integer.parseInt(time[1]);
        int day=Integer.parseInt(time[2]); int hour=Integer.parseInt(time[3]);
        int minutes=Integer.parseInt(time[4]);

        LocalDateTime deadline=LocalDateTime.of(year,month,day,hour,minutes);

        TodayTodo todayTodo=TodayTodo.createTodo(room,user, FinishStatus.UNFINISHED,deadline,requestDto.getTodo());
        todoRepository.save(todayTodo);
    }

    @Override
    public void saveRepeatTodo(CreateRepeatTodoRequestDto requestDto, User user, Room room) {
        String[]time = requestDto.getTime().split("/");
        int hour=Integer.parseInt(time[0]); int minutes=Integer.parseInt(time[1]);

        LocalDateTime deadline=LocalDateTime.of(LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth(),
                hour,minutes);

        List<RepeatDay> repeatDayList=new ArrayList<>();
        for(int i=0;i<7;i++){
            String day=requestDto.getDays();
            if(day.charAt(i)=='1'){
                RepeatDay repeatDay=RepeatDay.createRepeatDay(i);
                repeatDayList.add(repeatDay);
            }
        }

        RepeatTodo repeatTodo=RepeatTodo.createRepeatTodo(
                room,user,deadline,requestDto.getTodo(),repeatDayList);
        todoRepository.save(repeatTodo);
    }

    @Override
    public List<GetTodayTodoResponseDto> findTodayTodo(Room room,User user, int page) {

        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "createdTime");

        LocalDateTime currentTime=LocalDateTime.now();
        Page<TodayTodo> todayTodoList=todoRepository.findAllTodayTodoByRoomAndDeleteStatusAndTodoStatus(room,
                DeleteStatus.NOT_DELETED, FinishStatus.UNFINISHED,currentTime,pageRequest);

        List<GetTodayTodoResponseDto> res=new ArrayList<>();
        for(TodayTodo todayTodo:todayTodoList){

            LocalDateTime dealine=todayTodo.getDealine();
            String time=dealine.getMonthValue()+"/"+dealine.getDayOfMonth()+"/"
                    +dealine.getHour()+"/"+dealine.getMinute();

            GetTodayTodoResponseDto todoResponseDto=new GetTodayTodoResponseDto(
                    todayTodo.getId(),todayTodo.getTodoItem(),time,
                    (todayTodo.getUser().getId()==user.getId()?true:false)
            );
            res.add(todoResponseDto);
        }

        return res;
    }

    @Override
    public List<GetRepeatTodoResponseDto> findRepeatTodo(Room room, User user, int page) {

        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "createdTime");

        Page<RepeatTodo> repeatTodoList=todoRepository.findAllRepeatTodoByRoomAndDeleteStatus(
                room,DeleteStatus.NOT_DELETED,pageRequest);

        List<GetRepeatTodoResponseDto> res=new ArrayList<>();

        for(RepeatTodo todayTodo:repeatTodoList){

            LocalDateTime dealine=todayTodo.getDealine();
            String time=dealine.getHour()+"/"+dealine.getMinute();
            StringBuilder sb=new StringBuilder("0000000");
            for(RepeatDay repeatDay:todayTodo.getRepeatDayList()){
                sb.setCharAt(repeatDay.getDay(),'1');
            }
            GetRepeatTodoResponseDto todoResponseDto=new GetRepeatTodoResponseDto(
                    todayTodo.getId(),todayTodo.getTodoItem(),time,
                    (todayTodo.getUser().getId()==user.getId()?true:false),sb.toString()
            );
            res.add(todoResponseDto);
        }

        return res;
    }
}
