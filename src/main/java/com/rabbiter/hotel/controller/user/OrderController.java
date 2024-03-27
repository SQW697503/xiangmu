package com.rabbiter.hotel.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.ReturnOrderDTO;
import com.rabbiter.hotel.dto.ReturnRoomDTO;
import com.rabbiter.hotel.dto.ReturnUserDTO;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.UserService;
import com.rabbiter.hotel.utils.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;

    /**
     * 获取用户的历史订单列表
     */
    @GetMapping("/historyOrder")
    public CommonResult<List<ReturnOrderDTO>> historyOrder() {
        CommonResult<List<ReturnOrderDTO>> commonResult = new CommonResult<>();

        // 构造查询条件
        QueryWrapper queryWrapper = new QueryWrapper();

        // 从 session 中获取当前登录的用户信息
        User user = (User) WebUtils.getSession().getAttribute("loginUser");

        // 添加查询条件：user_id = 当前用户的 id
        queryWrapper.eq("user_id", user.getId());

        // 查询订单列表
        List<Order> list = orderService.list(queryWrapper);

        List<ReturnOrderDTO> orderDTOList = new ArrayList<>();
        if (0 != list.size()) {
            for (Order order : list) {
                ReturnOrderDTO orderDTO = new ReturnOrderDTO();

                // 根据订单的房间id获取房间信息
                ReturnRoomDTO roomDTO = roomService.roomDetail(order.getRoomId());

                orderDTO.setOrder(order);
                orderDTO.setRoom(roomDTO);

                orderDTOList.add(orderDTO);
            }
        }

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(orderDTOList);

        return commonResult;
    }

    /**
     * 获取订单详情
     * @param orderId 订单id
     */
    @PostMapping("/detailOrder")
    public CommonResult<ReturnOrderDTO> detailOrder(@RequestParam("orderId") Integer orderId) {
        CommonResult<ReturnOrderDTO> commonResult = new CommonResult<>();
        ReturnOrderDTO returnOrder = new ReturnOrderDTO();
        ReturnUserDTO userDTO = new ReturnUserDTO();

        // 根据订单id获取订单信息
        Order order = orderService.getById(orderId);

        // 根据用户id获取用户信息
        User user = userService.getById(order.getUserId());

        BeanUtils.copyProperties(user, userDTO);

        // 根据房间id获取房间信息
        ReturnRoomDTO returnRoomDTO = roomService.roomDetail(order.getRoomId());

        returnOrder.setOrder(order);
        returnOrder.setUser(userDTO);
        returnOrder.setRoom(returnRoomDTO);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(returnOrder);

        return commonResult;
    }

}
