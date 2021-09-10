package org.ks365.osmp.sys.entity;

import lombok.Data;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 消息
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "sys_message")
public class MessageEntity extends BaseEntity {

    @Column(name = "type", nullable = false, length = 4)
    private String type;

    @Column(name = "content_type", nullable = false, length = 4)
    private String contentType;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "message")
    private List<MessageWithUser> userList;

}
