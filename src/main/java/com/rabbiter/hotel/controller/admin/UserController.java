package com.rabbiter.hotel.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Comment;
import com.rabbiter.hotel.domain.Order;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.service.CommentService;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController("adminUserController")
@RequestMapping("/admin")
public class UserController {

    // 注入 UserService、OrderService 和 CommentService 服务接口
    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    @Resource
    private CommentService commentService;

    // 获取所有用户列表
    @GetMapping("/listUsers")
    public CommonResult<List<User>> listUsers() {
        CommonResult<List<User>> commonResult = new CommonResult<>();

        // 调用 UserService 的 list 方法获取所有用户列表
        List<User> userList = userService.list();

        // 构造 CommonResult 对象并返回给前端
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(userList);

        return commonResult;
    }

    // 删除用户及其关联的订单和评论
    @PostMapping("/deleteUser")
    public CommonResult<String> deleteUser(@RequestParam("userId") Integer userId) {
        CommonResult<String> commonResult = new CommonResult<>();

        // 移除关联订单
        orderService.remove(
                new QueryWrapper<Order>().eq("user_id", userId)
        );
        // 移除关联评论
        commentService.remove(
                new QueryWrapper<Comment>().eq("user_id", userId)
        );

        // 调用 UserService 的 removeById 方法删除用户
        boolean result = userService.removeById(userId);

        if (result) {
            // 如果删除成功，则返回成功信息给前端
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("删除成功");
        } else {
            // 如果删除失败，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("删除失败");
        }

        return commonResult;
    }

    // 修改用户信息
    @PostMapping("/updateUser")
    public CommonResult<String> updateUser(@RequestBody User user) {
        CommonResult<String> commonResult = new CommonResult<>();

        // 调用 UserService 的 updateById 方法更新用户信息
        boolean result = userService.updateById(user);

        if (result) {
            // 如果更新成功，则返回成功信息给前端
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("修改成功");
        } else {
            // 如果更新失败，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("修改失败");
        }

        return commonResult;
    }

    // 根据用户id获取用户信息
    @PostMapping("/getUserById")
    public CommonResult<User> getUserById(@RequestParam("userId") Integer userId) {
        CommonResult<User> commonResult = new CommonResult<>();

        // 调用 UserService 的 getById 方法根据用户id获取用户信息
        User user = userService.getById(userId);

        // 构造 CommonResult 对象并返回给前端
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(user);

        return commonResult;
    }

}
