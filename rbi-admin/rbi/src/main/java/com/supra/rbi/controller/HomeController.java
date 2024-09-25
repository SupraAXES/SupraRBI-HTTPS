package com.supra.rbi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.SimpleResourceInfo;
import com.supra.rbi.service.ResourceService;

@Controller
@RequestMapping("/api/home")
public class HomeController {
    
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/resource/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SimpleResourceInfo>> resourceList() {
        return resourceService.listResource();
    }

}
