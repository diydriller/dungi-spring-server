package com.project.common.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@DiscriminatorValue(value = "N")
public class Notice extends NoticeVote {

    @Column(name="notice_item")
    private String noticeItem;

    public static Notice createNotice(Room room,User user,String noticeItem){
        Notice notice=new Notice();
        notice.setRoom(room);
        notice.setUser(user);
        notice.setNoticeItem(noticeItem);
        return notice;
    }
}
