package com.supra.rbi.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.LocalPolicy;
import com.supra.rbi.domain.ResourceInfo;
import com.supra.rbi.domain.SessionInfo;
import com.supra.rbi.domain.SimpleSessionInfo;
import com.supra.rbi.service.SessionService;
import com.supra.rbi.util.EmptyUtils;
import com.supra.rbi.util.FileUtil;
import com.supra.rbi.util.IdUtil;
import com.supra.rbi.service.ResourceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {
    
    private static SessionService instance = null;

    public SessionServiceImpl() {
        instance = this;
    }

    public static SessionService getInstance() {
        return instance;
    }

    @Value("${settings.path.files}")
    private String filesPath;

    @Value("${settings.path.config}")
    private String configPath;

    @Autowired
    private ResourceService resourceService;

    private static ConcurrentMap<String, SessionInfo> sessionMap = new ConcurrentHashMap<>();

    private Map<String, String> siteNameMap = new ConcurrentHashMap<>();

    @Override
    public void updateSiteMap() {
        File dir = new File(configPath + "/autofill");
        dir.listFiles((file) -> {
            if (file.getName().endsWith(".json")) {
                String content = FileUtil.loadFileContent(file.getPath());
                if (content != null) {
                    String name = file.getName().substring(0, file.getName().length() - 5);
                    JSONObject json = JSON.parseObject(content);
                    siteNameMap.put(json.getString("url"), name);
                }
            }
            return false;
        });
    }

    @Override
    public SessionInfo getSession(String id) {
        return sessionMap.get(id);
    }

    @Override
    public List<SessionInfo> getSessions() {
        return new ArrayList<>(sessionMap.values());
    }

    @Override
    public void removeSessions(List<SessionInfo> list) {
        for (SessionInfo info : list) {
            sessionMap.remove(info.getUuid());
        }
    }

    @Override
    public CommonResult<String> openConnect(String url, String policy, String token, String autofill) {
        LocalPolicy localPolicy = getLocalPolicy(policy, token);
        if (localPolicy == null) {
            log.info("Policy not found: " + policy);
            return CommonResult.failed("Policy not found");
        }

        ResourceInfo resource = new ResourceInfo();
        resource.setName("");
        resource.setUrl(url);
        if (EmptyUtils.isNotEmpty(autofill)) {
            resource.setAutofill(JSONObject.parseObject(autofill));
        }

        SessionInfo info = new SessionInfo();
        info.setUuid(IdUtil.generateUuid());
        info.setResourceName("SupraRBI");
        info.setResource(resource);
        info.setAddress(url);
        info.setAutofill(resource.getAutofill());
        info.setProtocol("vnc");
        info.setSiteName(getSiteName(url));
        info.setRule(localPolicy.getRule());
        info.setUpdateTime(Calendar.getInstance().getTime());

        File file = new File(filesPath, "norm-" + info.getUuid());
        if (! file.exists()) {
            file.mkdirs();
        }
        info.setPath(file.getPath());

        sessionMap.put(info.getUuid(), info);

        log.info("Open Connect: " + info.getUuid());

        return CommonResult.success(info.getUuid());
    }

    @Override
    public CommonResult<String> openConnect(ResourceInfo resource) {
        log.info("Open Connect: " + JSON.toJSONString(resource));
        if (EmptyUtils.isEmpty(resource.getUrl()) && EmptyUtils.isNotEmpty(resource.getName())) {
            resource = resourceService.getResource(resource.getName());
            if (resource == null) {
                return CommonResult.failed();
            }
        }

        SessionInfo info = new SessionInfo();
        info.setUuid(IdUtil.generateUuid());
        info.setResourceName(resource.getName());
        info.setResource(resource);
        info.setAddress(resource.getUrl());
        info.setAutofill(resource.getAutofill());
        info.setProtocol("vnc");
        info.setSiteName(getSiteName(resource.getUrl()));
        info.setUpdateTime(Calendar.getInstance().getTime());

        String policy = resource.getPolicy();
        if (EmptyUtils.isEmpty(policy)) {
            policy = "default";
        }
        LocalPolicy localPolicy = getPolicy(policy);
        if (localPolicy == null) {
            log.info("Policy not found: " + policy);
            return CommonResult.failed("Policy not found");
        }
        info.setRule(localPolicy.getRule());

        File file = new File(filesPath, "norm-" + info.getUuid());
        if (! file.exists()) {
            file.mkdirs();
        }
        info.setPath(file.getPath());

        sessionMap.put(info.getUuid(), info);

        log.info("Open Connect: " + info.getUuid());

        return CommonResult.success(info.getUuid());
    }

    @Override
    public CommonResult<SimpleSessionInfo> openConnectInfo(String id) {
        SessionInfo session = getSession(id);
        if (session != null) {
            return CommonResult.success(new SimpleSessionInfo(session));
        }

        return CommonResult.failed();
    }

    @Override
    public CommonResult<Integer> openConnectCheck(String id) {
        return CommonResult.success(0);
    }

    @Override
    public CommonResult<Integer> openConnectAlive(String id) {
        SessionInfo session = sessionMap.get(id);
        if (session != null) {
            session.setUpdateTime(Calendar.getInstance().getTime());
        }

        return CommonResult.success(0);
    }

    @Override
    public CommonResult<Integer> updateResolution(String id, int width, int height) {
        return CommonResult.success(0);
    }

    private String getSiteName(String url) {
        if (siteNameMap.isEmpty()) {
            updateSiteMap();
        }
        return siteNameMap.get(url);
    }

    private LocalPolicy getLocalPolicy(String policy, String token) {
        if (EmptyUtils.isEmpty(policy) 
            || EmptyUtils.isEmpty(token)) {
            log.info("Policy or token is empty");
            return null;
        }
        String content = FileUtil.loadFileContent(
            configPath + "/token.json");
        if (content == null) {
            log.info("Token file not found： " + configPath + "/token.json");
            return null;
        }

        JSONArray array = JSONArray.parseArray(content);
        if (! array.contains(token)) {
            log.info("Token not found: " + token);
            return null;
        }

        return getPolicy(policy);
    }

    private LocalPolicy getPolicy(String policy) {
        if (EmptyUtils.isEmpty(policy)) {
            log.info("Policy is empty");
            return null;
        }

        String content = FileUtil.loadFileContent(
            configPath + "/policy/" + policy + ".json");
        if (content == null) {
            log.info("Policy file not found: " + policy);
            return null;
        }

        return JSON.parseObject(content, LocalPolicy.class);
    }

}
