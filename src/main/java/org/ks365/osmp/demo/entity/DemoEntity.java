package org.ks365.osmp.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ks365.osmp.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "system_demo")
@EqualsAndHashCode(callSuper = true)
public class DemoEntity extends BaseEntity {
    private String name;
    private String age;

    public DemoEntity() {
    }

    public DemoEntity(Integer id) {
        super(id);
    }
}
