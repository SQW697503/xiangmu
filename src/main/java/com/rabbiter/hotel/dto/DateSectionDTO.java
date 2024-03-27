package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


public class DateSectionDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") // 使用 DateTimeFormat 进行日期格式化，指定格式为 yyyy-MM-dd HH:mm
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 使用 JsonFormat 进行序列化和反序列化时的日期格式化，指定格式为 yyyy-MM-dd HH:mm
    private Date inTime; // 入住时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") // 使用 DateTimeFormat 进行日期格式化，指定格式为 yyyy-MM-dd HH:mm
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm") // 使用 JsonFormat 进行序列化和反序列化时的日期格式化，指定格式为 yyyy-MM-dd HH:mm
    private Date leaveTime; // 离开时间

    public DateSectionDTO() {
    }

    public DateSectionDTO(Date inTime, Date leaveTime) {
        this.inTime = inTime;
        this.leaveTime = leaveTime;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    @Override
    public String toString() {
        return "DateSectionDTO{" +
                "inTime=" + inTime +
                ", leaveTime=" + leaveTime +
                '}';
    }
}
