package com.rabbiter.hotel.controller.user;

import com.rabbiter.hotel.common.CommonResult;
import com.rabbiter.hotel.common.StatusCode;
import com.rabbiter.hotel.domain.Type;
import com.rabbiter.hotel.service.TypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user") // 接口的根路径为 /user
public class TypeController {

    @Resource
    private TypeService typeService;

    @GetMapping("/listTypes") // 处理 GET 请求，访问路径为 /user/listTypes
    public CommonResult<List<Type>> listTypes() {
        CommonResult<List<Type>> commonResult = new CommonResult<>();
        // 创建一个 CommonResult 的实例

        List<Type> list = typeService.list();
        // 调用 TypeService 中的 list 方法获取房间类型列表

        commonResult.setData(list); // 将房间类型列表设置为接口返回的数据
        commonResult.setCode(StatusCode.COMMON_SUCCESS.getCode()); // 设置接口返回状态码为成功
        commonResult.setMessage(StatusCode.COMMON_SUCCESS.getMessage()); // 设置接口返回消息为成功

        return commonResult; // 返回 CommonResult 实例作为接口的返回值
    }
}
