package com.supra.rbi.service;

import java.util.List;

import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.ResourceInfo;
import com.supra.rbi.domain.SessionInfo;
import com.supra.rbi.domain.SimpleSessionInfo;

public interface SessionService {

    void updateSiteMap();

    SessionInfo getSession(String id);

    List<SessionInfo> getSessions();

    void removeSessions(List<SessionInfo> list);
    
    CommonResult<String> openConnect(String url, String policy, String token, String autofill);

    CommonResult<String> openConnect(ResourceInfo resource);

    CommonResult<SimpleSessionInfo> openConnectInfo(String id);

    CommonResult<Integer> openConnectCheck(String id);

    CommonResult<Integer> openConnectAlive(String id);

    CommonResult<Integer> updateResolution(String id, int width, int height);
    
}
