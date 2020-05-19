package com.example.dto;

import lombok.Data;

/**
 * FCM推送消息请求实体
 *
 * @author tianqi
 * @date 2020-05-13
 */
@Data
public class FCMSendMessageDto {
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String body;
    /**
     * 用户token集合
     */
    private String tokens;
    /**
     * 主题
     */
    private String topic;
    /**
     * 图标
     */
    private String image;
}
