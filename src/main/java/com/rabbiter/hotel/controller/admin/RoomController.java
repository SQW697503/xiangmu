package com.rabbiter.hotel.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.Room;
import com.rabbiter.hotel.dto.AdminReturnRoomDTO;
import com.rabbiter.hotel.dto.DateSectionDTO;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController("adminRoomController")
@RequestMapping("/admin")
public class RoomController {

    // 注入 RoomService 和 OrderService 服务接口
    @Resource
    private RoomService roomService;
    @Resource
    private OrderService orderService;

    // 获取所有房间列表
    @GetMapping("/listRooms")
    public CommonResult<List<Room>> listRooms() {
        CommonResult<List<Room>> commonResult = new CommonResult<>();

        // 调用 RoomService 的 list 方法获取所有房间列表
        List<Room> roomList = roomService.list();

        // 构造 CommonResult 对象并返回给前端
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(roomList);

        return commonResult;
    }

    // 获取特定房间的详细信息
    @PostMapping(value = "/roomDetail")
    public CommonResult<AdminReturnRoomDTO> roomDetail(@RequestParam("roomId") Integer roomId) {
        CommonResult<AdminReturnRoomDTO> commonResult = new CommonResult<>();

        // 调用 RoomService 的 adminRoomDetail 方法获取特定房间的详细信息
        AdminReturnRoomDTO returnRoomDTO = roomService.adminRoomDetail(roomId);
        if(returnRoomDTO == null) {
            // 如果返回值为空，则说明该房间不存在，返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage("房间id不存在");
            return commonResult;
        }

        // 构造 CommonResult 对象并返回给前端
        commonResult.setData(returnRoomDTO);
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

    // 添加房间
    @PostMapping("/addRoom")
    public CommonResult<String> addRoom(@RequestBody Room room) {
        CommonResult<String> commonResult = new CommonResult<>();

        // 检查房间号是否已存在
        Room roomByNumber = roomService.getOne(new QueryWrapper<Room>().eq("number", room.getNumber()));
        if(roomByNumber != null) {
            // 如果已存在，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("房间号已存在");
            return commonResult;
        }

        // 调用 RoomService 的 save 方法保存房间信息
        boolean result = roomService.save(room);

        if (result) {
            // 如果保存成功，则返回成功信息给前端
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("添加房间成功");
        } else {
            // 如果保存失败，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("添加房间失败");
        }

        return commonResult;
    }

    // 修改房间信息
    @PostMapping("/updateRoom")
    public CommonResult<String> updateRoom(@RequestBody Room room) {
        CommonResult<String> commonResult = new CommonResult<>();

        // 检查房间号是否已存在
        Room roomByNumber = roomService.getOne(new QueryWrapper<Room>().eq("number", room.getNumber()).ne("id", room.getId()));
        if(roomByNumber != null) {
            // 如果已存在，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("房间号已存在");
            return commonResult;
        }

        // 调用 RoomService 的 updateById 方法更新房间信息
        boolean result = roomService.updateById(room);

        if (result) {
            // 如果更新成功，则返回成功信息给前端
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("修改房间信息成功");
        } else {
            // 如果更新失败，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("修改房间信息失败");
        }

        return commonResult;
    }

    // 删除房间及其关联的订单
    @PostMapping("/deleteRoom")
    public CommonResult<String> deleteRoom(@RequestParam("roomId") Integer roomId) {
        CommonResult<String> commonResult = new CommonResult<>();

        // 移除关联订单
        orderService.remove(
                new QueryWrapper<Order>().eq("room_id", roomId)
        );

        // 调用 RoomService 的 removeById 方法删除房间
        boolean result = roomService.removeById(roomId);

        if (result) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("room_id", roomId);
            orderService.remove(queryWrapper);

            // 如果删除成功，则返回成功信息给前端
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("删除房间成功");
        } else {
            // 如果删除失败，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("删除房间失败");
        }

        return commonResult;
    }
}
