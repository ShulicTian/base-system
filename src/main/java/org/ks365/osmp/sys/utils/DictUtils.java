package org.ks365.osmp.sys.utils;

import org.ks365.osmp.common.utils.SpringContextHolder;
import org.ks365.osmp.common.utils.StringUtils;
import org.ks365.osmp.sys.dao.DictDao;
import org.ks365.osmp.sys.entity.DictEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author Kulo
 */
public class DictUtils {
    private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
    private static Map<String, List<DictEntity>> listMap = new HashMap<>();

    /**
     * 无匹配返回空字符
     *
     * @param type
     * @param value
     * @return
     */
    public static String getDictLabelByValue(String type, String value) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(value)) {
            return "";
        }
        List<DictEntity> dicList = listMap.get(type);
        if (dicList == null) {
            dicList = dictDao.findListByType(type);
            listMap.put(type, dicList);
        }
        for (DictEntity d : dicList) {
            if (value.equals(d.getValue())) {
                return d.getLabel();
            }
        }
        return "";
    }

    /**
     * 无匹配返回空字符
     *
     * @param type
     * @param label
     * @return
     */
    public static String getDictValueByLabel(String type, String label) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(label)) {
            return "";
        }
        List<DictEntity> dicList = listMap.get(type);
        if (dicList == null) {
            dicList = dictDao.findListByType(type);
            listMap.put(type, dicList);
        }
        for (DictEntity d : dicList) {
            if (label.equals(d.getLabel())) {
                return d.getValue();
            }
        }
        return "";
    }

}
