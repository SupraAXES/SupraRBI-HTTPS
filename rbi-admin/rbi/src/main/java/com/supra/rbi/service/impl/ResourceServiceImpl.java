package com.supra.rbi.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.ResourceInfo;
import com.supra.rbi.domain.SimpleResourceInfo;
import com.supra.rbi.service.ResourceService;
import com.supra.rbi.util.EmptyUtils;
import com.supra.rbi.util.FileUtil;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Service
public class ResourceServiceImpl implements ResourceService {
    
    @Value("${settings.path.config}")
    private String configPath;

    @Override
    public ResourceInfo getResource(String id) {
        List<ResourceInfo> list = getResources();
        for (ResourceInfo resource : list) {
            if (resource.getName().equals(id)) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public CommonResult<List<SimpleResourceInfo>> listResource() {
        List<SimpleResourceInfo> list = new ArrayList<>();
        
        List<ResourceInfo> resources = getResources();
        for (ResourceInfo resource : resources) {
            list.add(new SimpleResourceInfo(resource));
        }
        return CommonResult.success(list);
    }

    private List<ResourceInfo> getResources() {
        String content = FileUtil.loadFileContent(
            new File(configPath, "resource.json").getAbsolutePath());

        List<ResourceInfo> list = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(content)) {
            list = JSONObject.parseArray(content, ResourceInfo.class);
        }
        return list;
    }

}
