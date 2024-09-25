package com.supra.rbi.domain;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ResourceInfo {
    
    private String name;
    
    private String url;

    private String icon;

    private String group;

    private String policy;

    private JSONObject autofill; // auto login account
 
    private WindowSize windowSize; // Connect Open Window Size

    public void update(ResourceInfo info) {
        BeanUtils.copyProperties(info, this);
    }

}
