package com.server.Service;

import com.qiniu.common.QiniuException;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface OssService {

    String uploadFile(File file, String fileName) throws QiniuException;

    String uploadFile(InputStream inputStream, String fileName) throws QiniuException;

    String delete(String key) throws QiniuException;
    String downloadFile(String id) throws UnsupportedEncodingException;

    public boolean deletePhoto(String fileName);
}
