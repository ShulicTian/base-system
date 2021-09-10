package org.ks365.osmp.common.service;

import org.ks365.osmp.sys.utils.UserUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * SpringDataJpa 审计（即初始化createBy及updateBy）
 *
 * @author tianslc
 */
@Component
public class AuditorAwareImpl implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Integer id = UserUtils.getCurrentUserId();
        if (id == null) {
            id = 1;
        }
        return Optional.of(id);
    }

}
