package com.rabbiter.hotel.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.RoomService;
import com.rabbiter.hotel.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController("adminOrderController")
@RequestMapping("/admin")
public class OrderController {

    @Resource
    private OrderService orderService;
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;

    /**
     * 获取订单列表接口
     *
     * @param flags 订单状态列表
     * @return CommonResult<List<Order>> 响应实体，包含响应状态码、响应消息和响应数据
     */
    @GetMapping("/listOrders")
    public CommonResult<List<Order>> listOrders(@RequestParam("orderFlags") List<Integer> flags) {
        CommonResult<List<Order>> commonResult = new CommonResult<>(); // 创建响应实体
        QueryWrapper queryWrapper = new QueryWrapper(); // 创建查询条件包装器

        queryWrapper.in("flag", flags); // 设置查询条件为订单状态在给定列表中的订单
        List<Order> orderList = orderService.list(queryWrapper); // 查询符合条件的订单列表

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置响应状态码为成功状态码
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置响应消息为成功消息
        commonResult.setData(orderList); // 将订单列表设置到响应数据中

        return commonResult; // 返回响应实体
    }

    /**
     * 取消订单接口
     *
     * @param orderId 订单ID
     * @return CommonResult<String> 响应实体，包含响应状态码、响应消息和响应数据
     */
    @PostMapping("/unsubscribe")
    public CommonResult<String> unsubscribe(@RequestParam("orderId") Integer orderId) {
        CommonResult<String> commonResult = new CommonResult<>(); // 创建响应实体

        Order order = orderService.getById(orderId); // 根据订单ID查询订单信息
        order.setFlag(2); // 将订单状态设置为取消状态
        boolean result = orderService.updateById(order); // 更新订单信息

        if (result) {
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置响应状态码为成功状态码
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置响应消息为成功消息
            commonResult.setData("退订成功"); // 设置响应数据为"退订成功"
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode()); // 设置响应状态码为失败状态码
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage()); // 设置响应消息为失败消息
            commonResult.setData("退订失败"); // 设置响应数据为"退订失败"
        }

        return commonResult; // 返回响应实体
    }

    /**
     * 办理入住接口
     *
     * @param orderId 订单ID
     * @return CommonResult<String> 响应实体，包含响应状态码、响应消息和响应数据
     */
    @PostMapping("/handle")
    public CommonResult<String> handle(@RequestParam("orderId") Integer orderId) {
        CommonResult<String> commonResult = new CommonResult<>(); // 创建响应实体

        Order order = orderService.getById(orderId); // 根据订单ID查询订单信息
        order.setFlag(1); // 将订单状态设置为已入住状态
        boolean result = orderService.updateById(order); // 更新订单信息

        if (result) {
            roomService.bookRoom(order.getRoomId()); // 标记房间为已被预定状态
            User user = userService.getById(order.getUserId()); // 根据订单中的用户ID查询用户信息
            int jifen = (int) (user.getJifen() + order.getRealPrice()); // 计算用户的积分
            user.setJifen(jifen); // 更新用户的积分
            userService.updateById(user); // 更新用户信息

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置响应状态码为成功状态码
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置响应消息为成功消息
            commonResult.setData("办理入住成功"); // 设置响应数据为"办理入住成功"
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode()); // 设置响应状态码为失败状态码
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage()); // 设置响应消息为失败消息
            commonResult.setData("办理入住失败"); // 设置响应数据为"办理入住失败"
        }

        return commonResult; // 返回响应实体
    }
}
