package org.ks365.osmp.sys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息中间表KEY
 *
 * @author tianslc
 */
@Data
public class MessageWithUserKey implements Serializable {
    private MessageEntity message;
    private UserEntity user;
}
