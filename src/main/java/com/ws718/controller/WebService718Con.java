package com.ws718.controller;

import com.alibaba.fastjson.JSON;
import com.ws718.bean.ResultBean;
import com.ws718.client.WebServiceClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * * @author ycc
 */
@RestController
@RequestMapping()
public class WebService718Con {
    /**
     * root.html交互接口
     *
     * @param body 前端queryxml json体
     * @return json格式字符结果
     */
    @RequestMapping(value = "queryData", method = RequestMethod.POST)
    public String queryData(@RequestBody String body) {
        ResultBean webServer = WebServiceClient.getWebServer(body);
        return JSON.toJSONString(webServer);
    }

}
