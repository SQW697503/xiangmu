package com.rabbiter.hotel.controller.admin;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Admin;
import com.rabbiter.hotel.dto.AdminLoginDTO;
import com.rabbiter.hotel.service.AdminService;
import com.rabbiter.hotel.utils.WebUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("adminAdminController")
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录接口
     *
     * @param adminLoginDTO 管理员登录信息 DTO 对象
     * @return CommonResult<Admin> 响应实体，包含响应状态码、响应消息和响应数据
     */
    @PostMapping(value = "/login")
    public CommonResult<Admin> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        CommonResult<Admin> commonResult = new CommonResult<>(); // 创建响应实体
        QueryWrapper queryWrapper = new QueryWrapper(); // 创建查询对象

        // 通过用户名和密码查询管理员信息
        queryWrapper.eq("admin_name", adminLoginDTO.getUserName());
        String md5Password = SecureUtil.md5(adminLoginDTO.getPassword()); // 将明文密码加密成 MD5 密码
        queryWrapper.eq("password", md5Password);
        Admin admin = adminService.getOne(queryWrapper);

        if (null != admin) { // 如果查询到了管理员信息
            admin.setAdmin_name(adminLoginDTO.getUserName()); // 将用户名设置到管理员信息中
            WebUtils.getSession().setAttribute("loginAdmin", admin); // 将管理员信息存储在 Session 中

            commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置响应状态码为成功状态码
            commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置响应消息为成功消息
            commonResult.setData(admin); // 将管理员信息设置到响应数据中
        } else { // 如果未查询到管理员信息
            commonResult.setCode(StatusCode.COMMON_FAIL.getCode()); // 设置响应状态码为失败状态码
            commonResult.setMessage("账号或密码错误"); // 设置响应消息为错误提示信息
            commonResult.setData(null); // 将响应数据设置为 null
        }

        return commonResult; // 返回响应实体
    }

    /**
     * 管理员登出接口
     *
     * @return CommonResult<String> 响应实体，包含响应状态码、响应消息和响应数据
     */
    @GetMapping("/logout")
    public CommonResult<String> logout(){
        CommonResult<String> commonResult = new CommonResult<>(); // 创建响应实体

        WebUtils.getSession().removeAttribute("loginAdmin"); // 从 Session 中移除管理员信息

        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置响应状态码为成功状态码
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置响应消息为成功消息
        commonResult.setData("登出成功!"); // 设置响应数据为登出成功提示信息

        return commonResult; // 返回响应实体
    }
}
