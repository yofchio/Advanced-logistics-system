package com.yang.bi.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author YANG FUCHAO
 */
public interface OssService {
    /**
     * 上传头像到OSS
     *
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}
