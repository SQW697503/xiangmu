package com.rabbiter.hotel.dto;

import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.domain.Type;

// 管理员返回房间DTO
public class AdminReturnRoomDTO {

    private Room room;    // 房间信息
    private Type type;    // 房间类型

    public AdminReturnRoomDTO() {
    }

    public AdminReturnRoomDTO(Room room, Type type) {
        this.room = room;
        this.type = type;
    }

    // 获取房间信息
    public Room getRoom() {
        return room;
    }

    // 设置房间信息
    public void setRoom(Room room) {
        this.room = room;
    }

    // 获取房间类型
    public Type getType() {
        return type;
    }

    // 设置房间类型
    public void setType(Type type) {
        this.type = type;
    }

    // 重写toString()方法，方便调试打印
    @Override
    public String toString() {
        return "AdminReturnRoomDTO{" +
                "room=" + room +
                ", type=" + type +
                '}';
    }
}
