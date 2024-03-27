package com.rabbiter.hotel.controller.user;

import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Comment;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.CommentDTO;
import com.rabbiter.hotel.service.CommentService;
import com.rabbiter.hotel.utils.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
public class CommentController {

    // 注入 CommentService 服务接口
    @Resource
    private CommentService commentService;

    // 发表评论
    @PostMapping("/publishComment")
    public CommonResult<String> publishComment(@RequestBody CommentDTO commentDTO) {
        CommonResult<String> commonResult = new CommonResult<>();

        // 从 session 中获取当前登录的用户信息
        User user = (User) WebUtils.getSession().getAttribute("loginUser");

        // 判断用户是否在该店消费过
        if (1 != user.getState()) {
            // 如果用户没有在该店消费过，则返回错误信息给前端
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("评价失败，您还没在该店消费过!");
        }
        else {
            // 如果用户在该店消费过，则将 CommentDTO 对象转换为 Comment 对象，并设置用户id
            Comment comment = new Comment();
            BeanUtils.copyProperties(commentDTO, comment);
            comment.setUserId(user.getId());

            // 调用 CommentService 的 save 方法保存评论
            commentService.save(comment);

            // 构造 CommonResult 对象并返回给前端
            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("评价成功!");
        }

        return commonResult;
    }

}
