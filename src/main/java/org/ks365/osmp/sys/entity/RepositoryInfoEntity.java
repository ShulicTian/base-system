package org.ks365.osmp.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 知识库
 *
 * @author tianslc
 */
@Data
@Entity
@Table(name = "repository_info")
@EqualsAndHashCode(callSuper = true)
public class RepositoryInfoEntity extends BaseEntity {

    @Column(name = "parent_id", nullable = false, length = 11)
    private Integer parentId;

    /**
     * 0文件夹 1文件 2文本内容 3网址
     */
    @Column(name = "type", nullable = false, length = 4)
    private String type;

    /**
     * 标题
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 内容
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 排序
     */
    @Column(name = "sort", nullable = false, length = 11)
    private Integer sort;

    /**
     * 是否展示 0不展示 1展示
     */
    @Column(name = "status", nullable = false, length = 4)
    private String status;

}
