package com.airboot.common.component;

import com.airboot.common.core.config.ProjectConfig;
import com.airboot.common.core.config.ServerConfig;
import com.airboot.common.core.constant.Constants;
import com.airboot.common.core.utils.StringUtils;
import com.airboot.common.core.utils.file.FileUploadUtils;
import com.airboot.common.core.utils.file.FileUtils;
import com.airboot.common.core.utils.ip.AddressUtils;
import com.airboot.common.model.vo.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author airboot
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    
    @Resource
    private ServerConfig serverConfig;
    
    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.isValidFilename(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 " , fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = ProjectConfig.getDownloadPath() + fileName;
            
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition" ,
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败" , e);
        }
    }
    
    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = ProjectConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("fileName" , fileName);
            dataMap.put("url" , url);
            return AjaxResult.success(dataMap);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
    
    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 本地资源路径
        String localPath = ProjectConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition" ,
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
    
    /**
     * 获取IP真实地址
     */
    @GetMapping("/ip-location")
    public AjaxResult getIpLocation(String ip) {
        return AjaxResult.success(AddressUtils.getRealAddressByIp(ip));
    }
    
}
