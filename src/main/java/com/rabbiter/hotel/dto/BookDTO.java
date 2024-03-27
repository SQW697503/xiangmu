package com.rabbiter.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

// 预订DTO
public class BookDTO {

    private Integer roomId;    // 房间ID

    // 入住时间，接收前端传来的字符串格式时间，转换为Date类型
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date inTime;

    // 离店时间，接收前端传来的字符串格式时间，转换为Date类型
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date leaveTime;

    private Integer realPeople;    // 实际入住人数
    private Integer fapiao;    // 是否需要发票，1表示需要，0表示不需要

    public BookDTO() {
    }

    public BookDTO(Integer roomId, Date inTime, Date leaveTime, Integer realPeople, Integer fapiao) {
        this.roomId = roomId;
        this.inTime = inTime;
        this.leaveTime = leaveTime;
        this.realPeople = realPeople;
        this.fapiao = fapiao;
    }

    // 获取房间ID
    public Integer getRoomId() {
        return roomId;
    }

    // 设置房间ID
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    // 获取入住时间
    public Date getInTime() {
        return inTime;
    }

    // 设置入住时间
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    // 获取离店时间
    public Date getLeaveTime() {
        return leaveTime;
    }

    // 设置离店时间
    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    // 获取实际入住人数
    public Integer getRealPeople() {
        return realPeople;
    }

    // 设置实际入住人数
    public void setRealPeople(Integer realPeople) {
        this.realPeople = realPeople;
    }

    // 获取是否需要发票
    public Integer getFapiao() {
        return fapiao;
    }

    // 设置是否需要发票
    public void setFapiao(Integer fapiao) {
        this.fapiao = fapiao;
    }

    // 重写toString()方法，方便调试打印
    @Override
    public String toString() {
        return "BookDTO{" +
                "roomId=" + roomId +
                ", inTime=" + inTime +
                ", leaveTime=" + leaveTime +
                ", realPeople=" + realPeople +
                ", fapiao=" + fapiao +
                '}';
    }
}
