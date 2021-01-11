/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package org.ks365.osmp.sys.service;

import org.ks365.osmp.sys.dao.LogDao;
import org.ks365.osmp.sys.entity.LogEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 日志Service
 *
 * @author tianslc
 */
@Service
@Transactional(readOnly = true)
public class LogService {
    private final LogDao logDao;

    public LogService(LogDao logDao) {
        this.logDao = logDao;
    }

    public List<LogEntity> findAllList() {
        return logDao.findAll();
    }

    public Page<LogEntity> getPageByColunm(LogEntity logEntity) {
        Specification<LogEntity> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (logEntity.getCreateDate() != null) {
                predicate.getExpressions().add(criteriaBuilder.between(root.get("createDate"), logEntity.getCreateDate(), new Date(logEntity.getCreateDate().getTime() + 24 * 60 * 60 * 1000)));
            }
            if (logEntity.getCreateBy() != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("createBy"), logEntity.getCreateBy()));
            }
            return predicate;
        };
        return logDao.findAll(specification, PageRequest.of(logEntity.getPage() - 1, logEntity.getSize(), Sort.by("createDate").descending()));
    }

    public List<LogEntity> getListByColunm(LogEntity logEntity) {
        Example<LogEntity> example = Example.of(logEntity);
        return logDao.findAll(example);
    }

    public LogEntity getOneByColunm(LogEntity logEntity) {
        Example<LogEntity> example = Example.of(logEntity);
        Optional<LogEntity> optional = logDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Transactional(readOnly = false)
    public LogEntity save(LogEntity logEntity) {
        logEntity = logDao.saveAndFlush(logEntity);
        return logEntity;
    }

    public LogEntity getById(Integer id) {
        return logDao.getOne(id);
    }

}
