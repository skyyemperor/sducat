package com.sducat.common.util;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sducat.common.core.exception.common.PicUploadException;
import com.sducat.common.util.uuid.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

@Component
@Slf4j
public class QiNiuYunPicUtil {

    @Autowired
    private TaskExecutorUtil<?> taskExecutorUtil;

    private final Auth auth;
    private final BucketManager bucketManager;
    private final UploadManager uploadManager;
    private final String bucket;
    private final String domain;

    private static final long EXPIRE_TIME = 5 * 24 * 3600 * 1000; //5天刷新一次

    private String token;
    private long refreshDate;

    public QiNiuYunPicUtil(@Value("${qiniuyun.access_key}") String accessKey,
                           @Value("${qiniuyun.secret_key}") String secretKey,
                           @Value("${qiniuyun.bucket}") String bucket,
                           @Value("${qiniuyun.domain}") String domain) {
        Configuration cfg = new Configuration(Region.huadong()); //华东的机房
        auth = Auth.create(accessKey, secretKey);
        bucketManager = new BucketManager(auth, cfg);
        uploadManager = new UploadManager(cfg);
        this.bucket = bucket;
        this.domain = domain;
    }

    private synchronized void refreshToken() {
        if (System.currentTimeMillis() - refreshDate > EXPIRE_TIME) {
            log.info("重新获取token中...");
            try {
                //有效时长10天
                token = auth.uploadToken(bucket, null, 3600 * 24 * 10, null, true);
                log.info("new token: " + token);
                refreshDate = System.currentTimeMillis();
            } catch (Exception e) {
                log.error("获取新的token失败");
            }
        }
    }

    public String uploadByMultipartFile(MultipartFile file) {
        try {
            return uploadByByte(file.getBytes());
        } catch (Exception e) {
            throw new PicUploadException();
        }
    }

    public List<String> uploadMultipartFiles(MultipartFile[] files) {
        List<String> picURLs = new ArrayList<>();
        if (files != null && files.length > 0) {
            List<FutureTask<String>> tasks = new ArrayList<>();
            for (MultipartFile file : files) {
                FutureTask<String> task = new FutureTask<>(() -> uploadByMultipartFile(file));
                task.run();
                tasks.add(task);
            }
            for (FutureTask<String> task : tasks) {
                try {
                    picURLs.add(task.get());
                } catch (Exception e) {
                    throw new PicUploadException();
                }
            }
        }
        return picURLs;
    }

    public String uploadByByte(byte[] uploadBytes) {
        //刷新token
        refreshToken();

        //云端文件的名称
        String key = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-")) + IdUtil.fastUUID();
        String resURL = domain;

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);
        try {
            Response response = uploadManager.put(byteInputStream, key, token, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            resURL += putRet.key;
        } catch (QiniuException ex) {
            //token过期
            Response r = ex.response;
            log.error(r.toString());
            throw new PicUploadException();
        }
        return resURL;
    }

    public String uploadByPath(String localFilePath) {
        //刷新token
        refreshToken();

        //云端文件的名称
        String key = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-")) + IdUtil.fastUUID();
        String resURL = domain;

        try {
            Response response = uploadManager.put(localFilePath, key, token);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            resURL += putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error(r.toString());
            return "";
        }

        return resURL;
    }

    public String uploadByBase64(String pic) {
        return uploadByByte(Base64Util.decodeStringToByte(pic));
    }

    public List<String> uploadByBase64(String[] pics) {
        List<String> picURLs = new ArrayList<>();
        if (pics != null && pics.length > 0) {
            List<FutureTask<String>> tasks = new ArrayList<>();
            for (String pic : pics) {
                FutureTask<String> task = new FutureTask<>(() -> uploadByBase64(pic));
                task.run();
                tasks.add(task);
            }
            for (FutureTask<String> task : tasks) {
                try {
                    picURLs.add(task.get());
                } catch (Exception e) {
                    throw new PicUploadException();
                }
            }
        }
        return picURLs;
    }


    public void delete(String url) {
        if (url == null || "".equals(url)) return;
        taskExecutorUtil.run(() -> {
            String tmpUrl = url;
            int lastSlashIndex = tmpUrl.lastIndexOf("/");
            if (lastSlashIndex != -1) {
                tmpUrl = tmpUrl.substring(lastSlashIndex + 1);
            }
            try {
                bucketManager.delete(bucket, tmpUrl);
            } catch (QiniuException ex) {
                log.error(ex.response.toString());
            }
        });
    }

    public boolean checkPicDomain(String... picUrls) {
        if (picUrls == null) return true;
        for (String u : picUrls) {
            if(u == null) continue;
            if (!u.startsWith(domain))
                return false;
        }
        return true;
    }
}
