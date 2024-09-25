package com.supra.rbi.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supra.rbi.api.CommonResult;
import com.supra.rbi.domain.FileInfo;

public interface FileService {
    
    CommonResult<List<FileInfo>> listFile(String uuid, String dir);

    CommonResult<List<FileInfo>> upload(String uuid, String dir, String fileName, String path);

    CommonResult<FileInfo> mkdirs(String uuid, String dir);

    CommonResult<FileInfo> delete(String uuid, String path);

    CommonResult<FileInfo> move(String uuid, String oldPath, String newPath);

    CommonResult<String> prepareDownload(String uuid, String path);

    void download(File file, HttpServletRequest request, HttpServletResponse response);

    void download(String uuid, String path, HttpServletRequest request, HttpServletResponse response);


}
