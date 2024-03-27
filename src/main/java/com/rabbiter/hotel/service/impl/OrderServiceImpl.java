package com.rabbiter.hotel.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbiter.hotel.domain.Order;

import com.rabbiter.hotel.mapper.OrderMapper;
import com.rabbiter.hotel.service.OrderService;
import com.rabbiter.hotel.utils.WeBASEUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    WeBASEUtils weBASEUtils;

    //    合约部署者的地址
    private static final String OWNER_ADDRESS = "0x8dc117b87c0bf3cfb6a520f77a5273e8c2566654";


    //信息上链
    @Override
    public String AddChain(Order order) {

        Log Result = null;
        if (order == null || order.getUserId() == null || order.getRoomId() == null ||
                order.getInTime() == null || order.getLeaveTime() == null ||
                order.getRealPrice() == null || order.getRealPeople() == null) {
            String s = String.valueOf(Result);
            return (String.valueOf(Result));
        }

        else {
            System.out.println(order);
        }

        List<Serializable> funcParam = new ArrayList<>();

        funcParam.add(order.getId());
        funcParam.add(order.getUserId());
        funcParam.add(order.getRoomId());
        funcParam.add(order.getInTime().getTime() / 1000);
        funcParam.add(order.getLeaveTime().getTime() / 1000);
        funcParam.add(order.getCreateTime().getTime() / 1000);
        funcParam.add(order.getRealPrice());
        funcParam.add(order.getRealPeople());
        funcParam.add(order.getFapiao());
        funcParam.add(order.getFlag());


        System.out.println(funcParam);

        String funcName="addWork";
        String _result = weBASEUtils.funcPost(OWNER_ADDRESS,funcName,funcParam);
        JSONObject _resultJson = JSONUtil.parseObj(_result);
        if (!_resultJson.containsKey("statusOK") || !_resultJson.getBool("statusOK")){
            String s = String.valueOf(Result);
            String s1 = String.valueOf(Result);
            String s11 = s1;
            return s11;
        }else{
            // 创建一个 Map 对象，将数据放入其中
            Map<String, Object> data = new HashMap<>();

            String transactionHash = _resultJson.getStr("transactionHash");
            String transactionIndex = _resultJson.getStr("transactionIndex");
            String blockNumber = _resultJson.getStr("blockNumber");
            String blockHash = _resultJson.getStr("blockHash");
            String from = _resultJson.getStr("from");
            String to = _resultJson.getStr("to");
            String message = _resultJson.getStr("message");
            String statusOK = _resultJson.getStr("statusOK");

            data.put("transactionHash", transactionHash);
            data.put("transactionIndex", transactionIndex);
            data.put("blockNumber", blockNumber);
            data.put("blockHash", blockHash);
            data.put("from", from);
            data.put("to", to);
            data.put("message", message);
            data.put("statusOK", statusOK);

            // 将 Map 对象转换为 JSON 字符串
            String jsonData = JSONUtil.toJsonStr(data);

            // 将 JSON 字符串放入一个对象中并返回给前端
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("msg", "success");
            result.put("data", jsonData);

            Map<String, Object> result1 = result;
            return (result).toString();
        }
    }

}

