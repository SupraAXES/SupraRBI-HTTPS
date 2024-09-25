package com.supra.rbi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.ResourceInfo;
import com.supra.rbi.domain.SimpleSessionInfo;
import com.supra.rbi.service.SessionService;
import com.supra.rbi.util.EmptyUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/connect")
public class ConnectController {
    
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/c", method = RequestMethod.GET)
    public String connect(
        @RequestParam(value = "url") String url, 
        @RequestParam(value = "policy", required = false, defaultValue = "default") String policy,
        @RequestParam(value = "token", required = false) String token,
        @RequestParam(value = "autofill", required = false) String autofill,
        @RequestHeader(value = "Raw-URL", required = false) String headerUrl,
        @RequestHeader(value = "User-Policy", required = false) String headerPolicy,
        @RequestHeader(value = "Service-Token", required = false) String headerToken,
        @RequestHeader(value = "Autofill", required = false) String headerAutofill,
        HttpServletRequest request, HttpServletResponse response) {
        if (EmptyUtils.isNotEmpty(headerUrl)) {
            url = headerUrl;
        }
        if (EmptyUtils.isNotEmpty(headerPolicy)) {
            policy = headerPolicy;
        }
        if (EmptyUtils.isNotEmpty(headerToken)) {
            token = headerToken;
        }
        if (EmptyUtils.isNotEmpty(headerAutofill)) {
            autofill = headerAutofill;
        }
        CommonResult<String> result = sessionService.openConnect(url, policy, token, autofill);
        if (request != null) {
            String scheme = request.getHeader("X-Forwarded-Proto");
            String host = request.getHeader("Host");
            return "redirect:" + scheme + "://" + host + "/connect?id=" + result.getData();
        }

        return "redirect:/connect?id=" + result.getData();
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> open(@RequestBody ResourceInfo info) {
        return sessionService.openConnect(info);
    }

    @RequestMapping(value = "/open/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<SimpleSessionInfo> openInfo(@RequestParam(value = "id") String id) {
        return sessionService.openConnectInfo(id);
    }

    @RequestMapping(value = "/open/check", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Integer> openConnectCheck(@RequestParam(value = "id") String id) {
        return sessionService.openConnectCheck(id);
    }

    @RequestMapping(value = "/open/alive", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Integer> openConnectAlive(@RequestParam(value = "id") String id) {
        return sessionService.openConnectAlive(id);
    }

    @RequestMapping(value = "/open/updateResolution", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Integer> updateResolution(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "width") int width,
            @RequestParam(value = "height") int height) {
        return sessionService.updateResolution(id, width, height);
    }
}
