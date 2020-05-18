package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.utils.FireBaseUtil;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    //设备的token值 宗辉提供的安卓token
    public static String token1 = "fV0EBOfmC_M:APA91bFDiNZiUooPpskuDIjJTI1dMHS1Odh7Jo930yIf08J6gQqDELRFCAUB3ZOFzSXHH6RPNxF6pm6vJDjmWg8tNrJ-H6XGkmbwkAuFv7VOGwCCsJhU9LIsedC261I5VvFIX55k6D14";
    public static String token2 = "cPtZpDTQ3Z0:APA91bElf8-6iHhpzSO2dhsaDwEhvF6j7mYcemLAtCvWOAkyS2SGNqdeurdGj6CKqR3grGf3hT0iYFSpnpaK81PEyDut1bHwle0vCx4-t31fR6m_4dRpaYbu56Xv8y3DS0RHyDDCXqPL";
    //渠道名字，也是APP的名字
    public static String appName = "myAppName";
    //主题名字
    public static String topic = "China";
    //通知消息题目
    public static String title = "tip";
    //通知消息内容
    public static String body = "hello world to TEST JSON5";

    @Test
    public void contextLoads() {
        try {
            //添加tokens
            List<String> tokens = new LinkedList();
            tokens.add(token1);
            tokens.add(token2);
            //设置Java代理,端口号是代理软件开放的端口，这个很重要。
//            System.setProperty("proxyHost", "localhost");
//            System.setProperty("proxyPort", "1080");
            //如果FirebaseApp没有初始化
            if (!FireBaseUtil.isInit(appName)) {
                String jsonPath = "src/main/resources/fcm/aquiver-biz-test-firebase-adminsdk-bucpw-9364d6795e.json";
                String dataUrl = "https://aquiver-biz-test.firebaseio.com";
                //初始化FirebaseApp
                FireBaseUtil.initSDK(jsonPath, dataUrl, appName);
            }
            //单推
            String messageId = FireBaseUtil.pushSingle(appName, token2, title, body, "", null);
            System.out.println("messageId:" + messageId);
            //多推
//            BatchResponse batchResponse = FireBaseUtil.pushMulticast(appName, tokens, title, body, "");
            //推所有
//            BatchResponse batchResponse = FireBaseUtil.pushAll(appName, token2, title, body, "");
//            System.out.println("success:" + batchResponse.getSuccessCount());
//            List<SendResponse> sendResponses = batchResponse.getResponses();
//            for (SendResponse response : sendResponses) {
//                System.out.println("response id:" + response.getMessageId());
//                System.out.println("response exception:" + response.getMessageId());
//            }
//            FireBaseUtil.registrationTopic(appName, tokens, topic);  //设置主题
//            FireBaseUtil.sendTopicMes(appName, topic, title, body);    //按主题推送
//            FireBaseUtil.cancelTopic(appName, tokens, topic);  //取消主题


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
