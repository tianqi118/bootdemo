package com.example.controller;

import com.example.dto.FCMSendMessageDto;
import com.example.utils.FireBaseUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * FireBase push controller
 *
 * @author tianqi
 * @date 2020-05-13
 */
@Slf4j
@RestController
@RequestMapping("/")
public class FireBaseController {
    /**
     * 渠道名字，也是APP的名字
     */
    public static String appName = "FCM后台新增的项目名称";


    @PostMapping(value = "/pushFireBaseAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void pushFireBaseAll(@RequestBody FCMSendMessageDto fcmSendMessageDto) {
        log.info("进入批量FireBase推送 pushFireBaseAll:[{}]", fcmSendMessageDto.toString());
        //添加tokens
        List<String> tokens = Arrays.asList(fcmSendMessageDto.getTokens().split(","));
        //设置Java代理,端口号是代理软件开放的端口，这个很重要。
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxyPort", "8081");
        //如果FirebaseApp没有初始化
        if (!FireBaseUtil.isInit(appName)) {
            String jsonPath = "fcm/google-services.json";
            String dataUrl = "https://xxxx.firebaseio.com/";
            //初始化FirebaseApp
            try {
                FireBaseUtil.initSDK(jsonPath, dataUrl, appName);
                //设置主题
                FireBaseUtil.registrationTopic(appName, tokens, fcmSendMessageDto.getTopic());
                //按主题推送
                FireBaseUtil.sendTopicMes(appName, fcmSendMessageDto.getTopic(), fcmSendMessageDto.getTitle(), fcmSendMessageDto.getBody());
            } catch (Exception e) {
                log.error("推送失败Exception：{}", e);
            }
        } else {
            log.info("如果FirebaseApp已经初始化");
            try {
                //设置主题
                FireBaseUtil.registrationTopic(appName, tokens, fcmSendMessageDto.getTopic());
                //按主题推送
                FireBaseUtil.sendTopicMes(appName, fcmSendMessageDto.getTopic(), fcmSendMessageDto.getTitle(), fcmSendMessageDto.getBody());
            } catch (IOException e) {
                log.error("推送失败IOException：{}", e);
            } catch (FirebaseMessagingException e) {
                log.error("推送失败FirebaseMessagingException：{}", e);
            }
        }
    }


    @PostMapping(value = "/pushFireBase", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void pushFireBase(@RequestBody FCMSendMessageDto fcmSendMessageDto) {
        log.info("进入批量FireBase推送 pushFireBaseAll:[{}]", fcmSendMessageDto.toString());
        //添加tokens
        List<String> tokens = Arrays.asList(fcmSendMessageDto.getTokens().split(","));
        //设置Java代理,端口号是代理软件开放的端口，这个很重要。
        System.setProperty("proxyHost", "127.0.0.1");
        System.setProperty("proxyPort", "8081");
        //如果FirebaseApp没有初始化
        if (!FireBaseUtil.isInit(appName)) {
            String jsonPath = "fcm/xxxx-firebase-adminsdk.json";
            String dataUrl = "https://xxxx.firebaseio.com/";
            //初始化FirebaseApp
            try {
                FireBaseUtil.initSDK(jsonPath, dataUrl, appName);
                //单推
                FireBaseUtil.pushSingle(appName, tokens.get(0), fcmSendMessageDto.getTitle(), fcmSendMessageDto.getBody(),fcmSendMessageDto.getImage(),"");
            } catch (Exception e) {
                log.error("Exception：{}", e);
            }
        } else {
            log.info("如果FirebaseApp已经初始化");
            try {
                //单推
                FireBaseUtil.pushSingle(appName, tokens.get(0), fcmSendMessageDto.getTitle(), fcmSendMessageDto.getBody(),fcmSendMessageDto.getImage(),"");
            }  catch (FirebaseMessagingException e) {
                log.error("FirebaseMessagingException：{}", e);
            }
        }
    }
}