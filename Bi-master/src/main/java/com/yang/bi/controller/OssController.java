package com.yang.bi.controller;

import com.yang.bi.common.BaseResponse;
import com.yang.bi.common.ErrorCode;
import com.yang.bi.common.ResultUtils;
import com.yang.bi.exception.BusinessException;
import com.yang.bi.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author YANG FUCHAO
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/bi/oss")
//@CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
//@CrossOrigin(origins = "http://bi.kongshier.top", allowCredentials = "true")
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public BaseResponse<String> uploadOssFile(@RequestPart("file") MultipartFile file) {
        //获取上传的文件
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "上传文件为空");
        }
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        //返回r对象
        return ResultUtils.success(url);
    }
}
