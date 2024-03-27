package com.rabbiter.hotel.dto;

// 管理员登录DTO
public class AdminLoginDTO {

    private String userName;    // 管理员用户名
    private String password;    // 管理员密码

    public AdminLoginDTO() {
    }

    public AdminLoginDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // 获取管理员用户名
    public String getUserName() {
        return userName;
    }

    // 设置管理员用户名
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 获取管理员密码
    public String getPassword() {
        return password;
    }

    // 设置管理员密码
    public void setPassword(String password) {
        this.password = password;
    }

    // 重写toString()方法，方便调试打印
    @Override
    public String toString() {
        return "AdminLoginDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
