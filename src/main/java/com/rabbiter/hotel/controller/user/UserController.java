package com.rabbiter.hotel.controller.user;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.User;
import com.rabbiter.hotel.dto.LoginDTO;
import com.rabbiter.hotel.dto.PasswordDTO;
import com.rabbiter.hotel.dto.RegisterDTO;
import com.rabbiter.hotel.dto.ReturnUserDTO;
import com.rabbiter.hotel.service.UserService;
import com.rabbiter.hotel.utils.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // 用户注册接口
    @PostMapping(value = "/register")
    public CommonResult<String> register(@RequestBody RegisterDTO registerDTO) {
        // 邮箱唯一验证
        long count = userService.count(new QueryWrapper<User>().eq("email", registerDTO.getEmail()));
        if(count > 0) {
            // 邮箱重复
            CommonResult<String> commonResult = new CommonResult<>();
            commonResult.setData("邮箱已存在");
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            return commonResult;
        }
        CommonResult<String> commonResult = new CommonResult<>();

        // 将注册信息拷贝到User对象中
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(SecureUtil.md5(registerDTO.getPassword()));

        // 保存用户信息到数据库
        userService.save(user);

        commonResult.setData("注册成功");
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        return commonResult;
    }

    // 用户登录接口
    @PostMapping(value = "/login")
    public CommonResult<String> login(@RequestBody LoginDTO loginDTO) {
        CommonResult<String> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", loginDTO.getEmail());
        String md5Password = SecureUtil.md5(loginDTO.getPassword());
        queryWrapper.eq("password", md5Password);
        User user = userService.getBaseMapper().selectOne(queryWrapper);

        if (null != user) {
            // 将用户信息存储到Session中，表示用户已登录
            WebUtils.getSession().setAttribute("loginUser", user);

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
            commonResult.setData("登录成功");
        } else {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("账号密码错误，请重试");
        }

        return commonResult;
    }

    // 用户登出接口
    @GetMapping("/logout")
    public CommonResult<String> logout(){
        CommonResult<String> commonResult = new CommonResult<>();

        // 从Session中移除用户信息，表示用户已登出
        WebUtils.getSession().removeAttribute("loginUser");

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData("登出成功!");

        return commonResult;
    }

    // 获取用户详细信息接口
    @GetMapping("/userDetail")
    public CommonResult<ReturnUserDTO> userDetail() {
        CommonResult<ReturnUserDTO> commonResult = new CommonResult();
        ReturnUserDTO returnUser = new ReturnUserDTO();

        User user2 = (User) WebUtils.getSession().getAttribute("loginUser");
        if(user2 == null) {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage("登录信息过期");

            return commonResult;
        }

        // 根据用户ID从数据库中获取用户信息
        User user = userService.getById(user2.getId());
        BeanUtils.copyProperties(user, returnUser);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData(returnUser);

        return commonResult;
    }

    // 修改用户密码接口
    @PostMapping("/updatePassword")
    public CommonResult<String> updatePassword(@RequestBody PasswordDTO passwordDTO) {
        CommonResult<String> commonResult = new CommonResult<>();
        QueryWrapper queryWrapper = new QueryWrapper();

        User user2 = (User) WebUtils.getSession().getAttribute("loginUser");
        User user = userService.getById(user2.getId());

        String md5OldPassword = SecureUtil.md5(passwordDTO.getOldPassword());

        if (!user.getPassword().equals(md5OldPassword)) {
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode());
            commonResult.setMessage(StatusCode.COMMON_FAIL.getMessage());
            commonResult.setData("密码错误");

            return commonResult;
        }

        String md5NewPassword = SecureUtil.md5(passwordDTO.getNewPassword());
        user.setPassword(md5NewPassword);
        userService.updateById(user);

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode());
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage());
        commonResult.setData("修改密码成功");

        return commonResult;
    }

}
