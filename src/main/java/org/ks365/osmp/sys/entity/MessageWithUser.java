package org.ks365.osmp.sys.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 消息中间表
 *
 * @author tianslc
 */
@Data
@Entity
@IdClass(MessageWithUserKey.class)
@Table(name = "sys_message_user")
public class MessageWithUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "status", nullable = false, length = 4)
    private String status;

}
