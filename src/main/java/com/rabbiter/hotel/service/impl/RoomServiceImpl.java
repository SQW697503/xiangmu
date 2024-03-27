package com.rabbiter.hotel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.domain.Type;
import com.rabbiter.hotel.dto.AdminReturnRoomDTO;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.dto.ReturnRoomDTO;
import com.rabbiter.hotel.mapper.OrderMapper;
import com.rabbiter.hotel.mapper.RoomMapper;
import com.rabbiter.hotel.mapper.TypeMapper;
import com.rabbiter.hotel.service.RoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    // 注入订单Mapper和房间类型Mapper
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private TypeMapper typeMapper;

    @Override
    public Boolean bookRoom(Integer roomId) {
        return this.getBaseMapper().bookRoom(roomId);
    }

    @Override
    public Boolean finishRoom(Integer roomId) {
        return this.getBaseMapper().finishRoom(roomId);
    }

    /**
     * 列出可供预订的房间列表
     *
     * @param dateSectionDTO 日期区间参数
     * @return 可供预订的房间列表
     */
    @Override
    public List<ReturnRoomDTO> listRooms(DateSectionDTO dateSectionDTO) {

        // 创建查询条件
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        // 查询空闲房间
        List<Room> roomList = this.baseMapper.selectList(roomQueryWrapper.eq("state", 0));
        Map<Integer, Room> roomMap = roomList.stream().collect(Collectors.toMap(Room::getId, a -> a,(k1, k2)->k1));

        // 创建订单查询条件
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<Order>()
                .eq("flag", 1)
                .lt("in_time", dateSectionDTO.getInTime())
                .lt("in_time", dateSectionDTO.getLeaveTime())
                .gt("leave_time", dateSectionDTO.getInTime())
                .lt("leave_time", dateSectionDTO.getLeaveTime())
                .or()
                .eq("flag", 1)
                .gt("in_time", dateSectionDTO.getInTime())
                .lt("in_time", dateSectionDTO.getLeaveTime())
                .gt("leave_time", dateSectionDTO.getInTime())
                .gt("leave_time", dateSectionDTO.getLeaveTime())
                .or()
                .eq("flag", 1)
                .gt("in_time", dateSectionDTO.getInTime())
                .lt("in_time", dateSectionDTO.getLeaveTime())
                .gt("leave_time", dateSectionDTO.getInTime())
                .lt("leave_time", dateSectionDTO.getLeaveTime())
                .or()
                .eq("flag", 1)
                .lt("in_time", dateSectionDTO.getInTime())
                .lt("in_time", dateSectionDTO.getLeaveTime())
                .gt("leave_time", dateSectionDTO.getInTime())
                .gt("leave_time", dateSectionDTO.getLeaveTime());

        // 查询符合条件的订单列表
        List<Order> orders = orderMapper.selectList(orderQueryWrapper);

        // 移除已预订的房间
        for (Order order : orders) {
            roomMap.remove(order.getRoomId());
        }

        // 获取剩余的房间列表
        List<Room> rooms = new ArrayList<>(roomMap.values());

        List<ReturnRoomDTO> returnRoomDTOList = new ArrayList<>();

        if (rooms.size() != 0) {
            for (Room room : rooms) {
                ReturnRoomDTO returnRoomDTO = split(room);
                BeanUtils.copyProperties(room, returnRoomDTO);
                Type type = typeMapper.selectById(room.getType());
                returnRoomDTO.setType(type);
                returnRoomDTOList.add(returnRoomDTO);
            }
        }

        return returnRoomDTOList;
    }

    /**
     * 获取房间详情
     *
     * @param roomId 房间ID
     * @return 房间详情
     */
    @Override
    public ReturnRoomDTO roomDetail(Integer roomId) {
        Room room = this.getBaseMapper().selectById(roomId);
        ReturnRoomDTO returnRoomDTO = split(room);
        Type type = typeMapper.selectById(room.getType());
        returnRoomDTO.setType(type);
        return returnRoomDTO;
    }

    /**
     * 管理员获取房间详情
     *
     * @param roomId 房间ID
     * @return 房间详情
     */
    @Override
    public AdminReturnRoomDTO adminRoomDetail(Integer roomId) {
        Room room = this.getBaseMapper().selectById(roomId);
        if(room == null) {
            return null;
        }
        AdminReturnRoomDTO roomDTO = new AdminReturnRoomDTO();
        roomDTO.setRoom(room);

        Type type = typeMapper.selectById(room.getType());
        roomDTO.setType(type);

        return roomDTO;
    }

    // 将介绍字段拆分为map类型
    private ReturnRoomDTO split(Room room) {
        ReturnRoomDTO returnRoomDTO = new ReturnRoomDTO();
        BeanUtils.copyProperties(room, returnRoomDTO);

        Map<String, String> introduces = new HashMap<>();
        String[] items = room.getIntroduce().split(",");
        for(String str : items) {
            String[] strs = str.split(":");
            introduces.put(strs[0], strs[1]);
        }

        returnRoomDTO.setIntroduces(introduces);

        return returnRoomDTO;
    }
}
