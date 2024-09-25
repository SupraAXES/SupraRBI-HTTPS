package com.supra.rbi.service;

import java.util.List;

import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.ResourceInfo;
import com.supra.rbi.domain.SimpleResourceInfo;

public interface ResourceService {

    ResourceInfo getResource(String id);

    CommonResult<List<SimpleResourceInfo>> listResource();
    
}
