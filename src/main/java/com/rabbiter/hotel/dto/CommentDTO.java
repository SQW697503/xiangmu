package com.rabbiter.hotel.dto;

// 评论DTO
public class CommentDTO {

    private String information;    // 评论内容
    private Integer type;    // 评论类型，1表示好评，0表示差评

    public CommentDTO() {
    }

    public CommentDTO(String information, Integer type) {
        this.information = information;
        this.type = type;
    }

    // 获取评论内容
    public String getInformation() {
        return information;
    }

    // 设置评论内容
    public void setInformation(String information) {
        this.information = information;
    }

    // 获取评论类型
    public Integer getType() {
        return type;
    }

    // 设置评论类型
    public void setType(Integer type) {
        this.type = type;
    }

    // 重写toString()方法，方便调试打印
    @Override
    public String toString() {
        return "CommentDTO{" +
                "information='" + information + '\'' +
                ", type=" + type +
                '}';
    }
}
