package com.springbootutils.util;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestTemplateUtils {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T postForObjectAboutJson(String url, Object obj, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(obj), httpHeaders);
        return restTemplate.postForObject(url, httpEntity, clazz);
    }

    public <T> T postForObjectAboutUrlencoded(String url, Map<String, Object> obj, Class<T> clazz) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(getParams(obj), headers);
        return restTemplate.postForObject(url, httpEntity, clazz);
    }

    private MultiValueMap getParams(Map<String, Object> obj) {
        MultiValueMap returnMap = new LinkedMultiValueMap(obj.size());
        for (Map.Entry<String, Object> entrySet : obj.entrySet()) {
            returnMap.add(entrySet.getKey(), entrySet.getValue());
        }
        return returnMap;
    }

}
