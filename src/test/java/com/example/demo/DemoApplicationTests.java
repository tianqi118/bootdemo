package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.utils.FireBaseUtil;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    //设备的token值 宗辉提供的安卓token
    public static String token1 = "fV0EBOfmC_M:APA91bFDiNZiUooPpskuDIjJTI1dMHS1Odh7Jo930yIf08J6gQqDELRFCAUB3ZOFzSXHH6RPNxF6pm6vJDjmWg8tNrJ-H6XGkmbwkAuFv7VOGwCCsJhU9LIsedC261I5VvFIX55k6D14";
    public static String token2 = "cPtZpDTQ3Z0:APA91bElf8-6iHhpzSO2dhsaDwEhvF6j7mYcemLAtCvWOAkyS2SGNqdeurdGj6CKqR3grGf3hT0iYFSpnpaK81PEyDut1bHwle0vCx4-t31fR6m_4dRpaYbu56Xv8y3DS0RHyDDCXqPL";
    //C端测试
    public static String token3 = "e9VNmNcbAyg:APA91bHpmDDRCPoMxbBWv4PQH4CIvo0tnRGxXzWCJAH4R0vDeOs9QQxxjaOdRCnw75l65M8wGlQoYgGZF3gpsY61oPXWvK3FECIhLt-GNtTAjKAk0x0B0tpouvo5tEy65FFLQJLh_2sF";
    public static String token4 = "dzGoBMRd3BY:APA91bFBSeZbwvi2DS30jt8gZy8o4Y2tikSpC9_MRZ9KEo3Rm9mqt_mOJ0bY2STYhY89Qs3baVws3AiicXTIMAWgOoLxj81UNUkqNJM2Zp-SoXuPyjq0jghd0g0MUwhb6fwUMc0WRZRJ";
    //渠道名字，也是APP的名字
    public static String appName = "myAppName";
    //主题名字
    public static String topic = "China";
    //通知消息题目
    public static String title = "tip";
    //通知消息内容
    public static String body = "hello world to TEST5";

    @Test
    public void contextLoads() {
        try {
            //添加tokens
            List<String> tokens = new LinkedList();
            tokens.add(token1);
            tokens.add(token2);
            tokens.add(token3);
            tokens.add(token4);
            //设置Java代理,端口号是代理软件开放的端口，这个很重要。
//            System.setProperty("proxyHost", "localhost");
//            System.setProperty("proxyPort", "1080");
            //如果FirebaseApp没有初始化
            if (!FireBaseUtil.isInit(appName)) {
//                String jsonPath = "src/main/resources/fcm/aquiver-biz-test-firebase-adminsdk-bucpw-9364d6795e.json";
//                String dataUrl = "https://aquiver-biz-test.firebaseio.com";

                String jsonPath = "src/main/resources/fcm/aquiver-test-314db-firebase-adminsdk-znf1h-03482f6084.json";
                String dataUrl = "https://aquiver-test-314db.firebaseio.com";
                //初始化FirebaseApp
                FireBaseUtil.initSDK(jsonPath, dataUrl, appName);
            }

            Map data = new HashMap();
            data.put("name", "Jame");
            data.put("age", "18");
            data.put("date", "2020-5-19");
            String dataJson = JSON.toJSONString(data);
            String image = "https://cdn-images-1.medium.com/fit/c/120/120/1*8I-HPL0bfoIzGied-dzOvA.png";
            //单推
//            String messageId = FireBaseUtil.pushSingle(appName, token4, title, body, image, dataJson);
//            System.out.println("messageId:" + messageId);
            //多推
            BatchResponse batchResponse = FireBaseUtil.pushMulticast(appName, tokens, title, body, "", "");
            //打印所有的应答信息
            printResponse(batchResponse);
            //获取推送失败的messageId
            if (batchResponse.getFailureCount() > 0) {
                List<SendResponse> responses = batchResponse.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        // The order of responses corresponds to the order of the registration tokens.
                        failedTokens.add(tokens.get(i));
                    }
                }
                for (String token : failedTokens) {
                    System.out.println("failed id:" + token);

                }
            }

//            FireBaseUtil.registrationTopic(appName, tokens, topic);  //设置主题
//            FireBaseUtil.sendTopicMes(appName, topic, title, body);    //按主题推送
//            FireBaseUtil.cancelTopic(appName, tokens, topic);  //取消主题


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印响应信息
     *
     * @param batchResponse
     */
    private void printResponse(BatchResponse batchResponse) {
        System.out.println("success:" + batchResponse.getSuccessCount());
        List<SendResponse> sendResponses = batchResponse.getResponses();
        for (SendResponse response : sendResponses) {
            System.out.println("response id:" + response.getMessageId());
            System.out.println("response exception:" + response.getMessageId());
        }
    }

}
