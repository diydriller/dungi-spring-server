package com.project.api_server.todo.service;

import com.project.common.aop.BaseExceptionResponseAnnotation;
import com.project.common.error.BaseException;
import com.project.common.model.*;
import com.project.common.repository.UserRoomRepository;
import com.project.common.service.RedisService;
import com.project.common.util.StringUtil;
import com.project.api_server.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.todo.dto.GetRepeatTodoResponseDto;
import com.project.api_server.todo.dto.GetTodayTodoResponseDto;
import com.project.common.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.NOT_EXIST_USER_ROOM;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRoomRepository userRoomRepository;
    private final RedisService redisService;

    //하루 할일 생성
    @BaseExceptionResponseAnnotation
    @Transactional
    public void createTodayTodo(CreateTodayTodoRequestDto todoRequestDto, Long userId, Long roomId)
            throws Exception {

        User user=redisService.getUser(StringUtil.redisLogin(userId));
        Room room = userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        String[]time=todoRequestDto.getTime().split("/");
        int year=Integer.parseInt(time[0]); int month=Integer.parseInt(time[1]);
        int day=Integer.parseInt(time[2]); int hour=Integer.parseInt(time[3]);
        int minutes=Integer.parseInt(time[4]);

        LocalDateTime deadline=LocalDateTime.of(year,month,day,hour,minutes);

        TodayTodo todayTodo=TodayTodo.createTodo(room,user, FinishStatus.UNFINISHED,deadline,todoRequestDto.getTodo());
        todoRepository.save(todayTodo);
    }

    //반복할일 생성
    @Transactional
    public void createRepeatTodo(CreateRepeatTodoRequestDto todoRequestDto, Long userId, Long roomId)
            throws Exception {
        User user=redisService.getUser(StringUtil.redisLogin(userId));
        Room room = userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

        String[]time=todoRequestDto.getTime().split("/");
        int hour=Integer.parseInt(time[0]); int minutes=Integer.parseInt(time[1]);

        LocalDateTime deadline=LocalDateTime.of(LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth(),
                hour,minutes);

        List<RepeatDay> repeatDayList=new ArrayList<>();
        for(int i=0;i<7;i++){
            String day=todoRequestDto.getDays();
            if(day.charAt(i)=='1'){
                RepeatDay repeatDay=RepeatDay.createRepeatDay(i);
                repeatDayList.add(repeatDay);
            }
        }

        RepeatTodo repeatTodo=RepeatTodo.createRepeatTodo(
                room,user,deadline,todoRequestDto.getTodo(),repeatDayList);
        todoRepository.save(repeatTodo);
    }

    // 하루 할일 조회
    @Transactional
    public List<GetTodayTodoResponseDto> getTodayTodo(Long userId,Long roomId, int page)
            throws Exception {

        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "createdTime");
        redisService.getUser(StringUtil.redisLogin(userId));
        Room room = userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

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
                    (todayTodo.getUser().getId()==userId?true:false)
            );
            res.add(todoResponseDto);
        }

        return res;
    }

    // 반복 할일 조회
    public List<GetRepeatTodoResponseDto> getRepeatTodo(Long userId, Long roomId, int page)
            throws Exception {
        PageRequest pageRequest = PageRequest.of(page,10, Sort.Direction.DESC, "createdTime");

        User user=redisService.getUser(StringUtil.redisLogin(userId));
        Room room = userRoomRepository.findUserRoomByUserAndRoomAndDeleteStatus(userId,roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});

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
                    (todayTodo.getUser().getId()==userId?true:false),sb.toString()
            );
            res.add(todoResponseDto);
        }

        return res;

    }
}
