package com.rabbiter.hotel.controller.admin;

import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Comment;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.ReturnCommentDTO;
import com.rabbiter.hotel.dto.ReturnUserDTO;
import com.rabbiter.hotel.service.CommentService;
import com.rabbiter.hotel.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController("adminCommentController")
@RequestMapping("/admin")
public class CommentController {

    @Resource
    private CommentService commentService;
    @Resource
    private UserService userService;

    /**
     * 获取评论列表接口
     *
     * @return CommonResult<List<ReturnCommentDTO>> 响应实体，包含响应状态码、响应消息和响应数据
     */
    @GetMapping("/listComment")
    public CommonResult<List<ReturnCommentDTO>> listComment() {
        CommonResult<List<ReturnCommentDTO>> commonResult = new CommonResult<>(); // 创建响应实体
        List<ReturnCommentDTO> returnCommentList = new ArrayList<>(); // 创建返回的评论列表

        List<Comment> commentList = commentService.list(); // 查询所有评论列表

        for (Comment comment : commentList) { // 遍历评论列表
            ReturnCommentDTO commentDTO = new ReturnCommentDTO(); // 创建返回的评论 DTO 对象
            User user = userService.getById(comment.getUserId()); // 根据评论中的用户ID查询用户信息
            ReturnUserDTO returnUserDTO = new ReturnUserDTO(); // 创建返回的用户 DTO 对象
            BeanUtils.copyProperties(user, returnUserDTO); // 将用户信息拷贝到返回的用户 DTO 对象

            commentDTO.setComment(comment); // 将评论信息设置到返回的评论 DTO 对象中
            commentDTO.setUser(returnUserDTO); // 将用户信息设置到返回的评论 DTO 对象中

            returnCommentList.add(commentDTO); // 将返回的评论 DTO 对象添加到评论列表中
        }

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置响应状态码为成功状态码
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置响应消息为成功消息
        commonResult.setData(returnCommentList); // 将评论列表设置到响应数据中

        return commonResult; // 返回响应实体
    }
}
