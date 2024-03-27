package com.rabbiter.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.dto.AdminReturnRoomDTO;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnRoomDTO;

import java.util.List;


public interface RoomService extends IService<Room> {

    /**
     * 预订房间
     * @param roomId 房间ID
     * @return 预订结果
     */
    Boolean bookRoom(Integer roomId);

    /**
     * 完成房间
     * @param roomId 房间ID
     * @return 完成结果
     */
    Boolean finishRoom(Integer roomId);

    /**
     * 列出可供预订的房间列表
     * @param dateSectionDTO 日期区间参数
     * @return 可供预订的房间列表
     */
    List<ReturnRoomDTO> listRooms(DateSectionDTO dateSectionDTO);

    /**
     * 获取房间详情
     * @param roomId 房间ID
     * @return 房间详情
     */
    ReturnRoomDTO roomDetail(Integer roomId);

    /**
     * 管理员获取房间详情
     * @param roomId 房间ID
     * @return 房间详情
     */
    AdminReturnRoomDTO adminRoomDetail(Integer roomId);
}
