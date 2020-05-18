package com.example.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FireBaseUtil
 *
 * @author tianqi
 * @date 2020-05-14
 */
public class FireBaseUtil {

    /**
     * 跟app约定的业务数据存放key，value为JSON格式
     */
    public static final String DATA = "data";
    /**
     * 存放多个实例的Map
     */
    private static Map<String, FirebaseApp> firebaseAppMap = new ConcurrentHashMap<>();

    /**
     * 判断SDK是否初始化
     *
     * @param appName
     * @return
     */
    public static boolean isInit(String appName) {
        return firebaseAppMap.get(appName) != null;
    }

    /**
     * 初始化SDK
     *
     * @param jsonPath JSON路径
     * @param dataUrl  firebase数据库
     * @param appName  APP名字
     * @throws IOException
     */
    public static void initSDK(String jsonPath, String dataUrl, String appName) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(jsonPath);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(dataUrl).build();
        //初始化firebaseApp
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        //存放
        firebaseAppMap.put(appName, firebaseApp);
    }

    /**
     * 单设备推送
     *
     * @param appName 应用的名字
     * @param token   注册token
     * @param title   推送题目
     * @param body    推送内容
     * @param image   推送图标
     * @param data    附加数据（JSON格式）
     * @return
     * @throws FirebaseMessagingException
     */
    public static String pushSingle(String appName, String token, String title, String body, String image, String data) throws FirebaseMessagingException {
        //获取实例
        FirebaseApp firebaseApp = getFirebaseApp(appName);
        //实例为空的情况
        if (firebaseApp == null) {
            return null;
        }
        //构建消息内容
        Message message = Message.builder().setNotification(Notification.builder().setImage(image).setTitle(title).setBody(body).build())
                .putData(DATA, data)
                .setToken(token)
                .build();
        //返回messageID
        return FirebaseMessaging.getInstance(firebaseApp).send(message);
    }

    /**
     * 多设备推送（注：api底层最多支持一次500个token）
     * 引用：https://firebase.google.com/docs/cloud-messaging/send-message
     *
     * @param appName 应用的名字
     * @param tokens  注册tokens
     * @param title   推送题目
     * @param body    推送内容
     * @param image   推送图标
     * @param data    附件数据（JSON格式）
     * @return
     * @throws FirebaseMessagingException
     */
    public static BatchResponse pushMulticast(String appName, List<String> tokens, String title, String body, String image, String data) throws FirebaseMessagingException {
        //获取实例
        FirebaseApp firebaseApp = getFirebaseApp(appName);
        //实例为空的情况
        if (firebaseApp == null) {
            return null;
        }
        //构建Multi消息
        MulticastMessage messages = MulticastMessage.builder().setNotification(Notification.builder().setImage(image).setTitle(title).setBody(body).build())
                .putData(DATA, data)
                .addAllTokens(tokens)
                .build();
        //返回BatchResponse
        return FirebaseMessaging.getInstance(firebaseApp).sendMulticast(messages);
    }


    /**
     * 给设备订阅主题
     *
     * @param appName 应用的名字
     * @param tokens  设备的token,最大1000个
     * @param topic   要添加的主题
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    public static void registrationTopic(String appName, List<String> tokens, String topic) throws FirebaseMessagingException, IOException {
        //获取实例
        FirebaseApp firebaseApp = firebaseAppMap.get(appName);
        //实例不存在的情况
        if (firebaseApp == null) {
            return;
        }
        //订阅，返回主题管理结果对象。
        TopicManagementResponse response = FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(tokens, topic);
        System.out.println("添加设备主题，成功：" + response.getSuccessCount() + ",失败：" + response.getFailureCount());
    }

    /**
     * 取消设备的订阅主题
     *
     * @param appName 应用的名字
     * @param tokens  设备的token,最大1000个
     * @param topic   取消的主题
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    public static void cancelTopic(String appName, List<String> tokens, String topic) throws FirebaseMessagingException, IOException {
        //获取实例
        FirebaseApp firebaseApp = firebaseAppMap.get(appName);
        //实例不存在的情况
        if (firebaseApp == null) {
            return;
        }
        //取消订阅，返回主题管理结果对象。
        TopicManagementResponse response = FirebaseMessaging.getInstance(firebaseApp).unsubscribeFromTopic(tokens, topic);
        System.out.println("取消设备主题，成功：" + response.getSuccessCount() + ",失败：" + response.getFailureCount());
    }

    /**
     * 按主题推送
     *
     * @param appName 应用的名字
     * @param topic   主题的名字
     * @param title   消息题目
     * @param body    消息体
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    public static void sendTopicMes(String appName, String topic, String title, String body) throws FirebaseMessagingException, IOException {
        //获取实例
        FirebaseApp firebaseApp = firebaseAppMap.get(appName);
        //实例不存在的情况
        if (firebaseApp == null) {
            return;
        }
        //构建消息
        Message message = Message.builder()
                .setNotification(new Notification(title, body))
                .setTopic(topic)
                .build();
        //发送后，返回messageID
        String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
        System.out.println("主题推送成功: " + response);
    }

    /**
     * 获取缓存的App配置
     *
     * @param appName
     * @return
     */
    private static FirebaseApp getFirebaseApp(String appName) {
        return firebaseAppMap.get(appName);
    }

}