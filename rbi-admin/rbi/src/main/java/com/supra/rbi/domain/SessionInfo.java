package com.supra.rbi.domain;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SessionInfo {
    
    private String uuid;

    private String resourceName;

    private String path;

    private String protocol;

    private String address;

    private String siteName;

    private JSONObject autofill;

    private ResourceInfo resource;

    private ResourceRule rule;

    private int status;

    private Date updateTime;

}
