package com.rabbiter.hotel.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class WeBASEUtils {

    @Value("${project.webase-url}")
    String webaseUrl;

    @Value("${system.contract.UChainAddress1}")
    String contractAddress1;


    public static final String ABI = com.rabbiter.hotel.utils.IOUtil.readResourceAsString("abi/XChain.abi");

    public String funcPost(String userAddress, String funcName, List funcParam) {

        JSONArray abiJSON = JSONUtil.parseArray(ABI);
        JSONObject data = JSONUtil.createObj();
        data.set("groupId", "1");
        data.set("contractPath", "/");
        data.set("contractAbi", abiJSON);
        data.set("useAes", false);
        data.set("useCns", false);
        data.set("cnsName", "");

        data.set("user", userAddress);
        data.set("contractAddress", contractAddress1);
        data.set("funcName", funcName);
        data.set("funcParam", funcParam);

        String dataString = JSONUtil.toJsonStr(data);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(webaseUrl);
        httpPost.setHeader("Content-type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(dataString, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse httpResponse;
        String result = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
}
