package org.ks365.osmp.sys.utils;

import org.ks365.osmp.sys.entity.AreaEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 区域工具类
 *
 * @author tianslc
 */
public class AreaUtils {

    public static void treeAreas(Set<AreaEntity> areaSet, AreaEntity nowArea, int level, int nowLevel) {
        if (level != -1) {
            nowLevel++;
            if (nowLevel > level) {
                return;
            }
        }
        List<AreaEntity> children = areaSet.stream().filter(areaEntity -> areaEntity.getParentId().equals(nowArea.getId()) && !areaEntity.getId().equals(0)).collect(Collectors.toList());
        if (children != null && children.size() > 0) {
            children.sort(Comparator.comparing(AreaEntity::getSort));
            nowArea.setChildren(children);
            int finalNowLevel = nowLevel;
            children.forEach(areaEntity -> {
                treeAreas(areaSet, areaEntity, level, finalNowLevel);
            });
        }
    }

}
