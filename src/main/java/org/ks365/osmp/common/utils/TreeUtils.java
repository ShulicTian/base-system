package org.ks365.osmp.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 构造树形结构工具类
 *
 * @author Kulo
 * @date 2021-04-25 18:07
 **/
public class TreeUtils {
    /**
     * 先根据List数据集合获取每一组树的根集合，然后将根集合和数据List集合进行递归得到树
     *
     * @param nodes            全部数据节点-list
     * @param parentColumnName 对象根ID的属性名-id
     * @param pidColumnName    对象pid的属性名-parentId
     * @param rootValue        根的属性值为多少-1
     * @param childrenName     对象装子节点的属性名-childList
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> getTree(List<T> nodes, String parentColumnName, String pidColumnName,
                                      String rootValue, String childrenName) throws Exception {
        List<T> root = new ArrayList<T>();
        for (T t : nodes) {
            Field field = t.getClass().getDeclaredField(pidColumnName);
            field.setAccessible(true);
            Object pid = field.get(t);

            if (rootValue.equals(pid)) {
                root.add(t);
                break;
            }
        }
        return buildTree(nodes, root, parentColumnName, pidColumnName, childrenName);
    }

    /**
     * 循环根的集合， 将根的id和数据集合的父id进行遍历对比整合为子集合，然后继续将子集合作为根进行递归，最后将子集合存入根对象中
     *
     * @param nodes            全部数据节点
     * @param root             根节点的集合
     * @param parentColumnName 对象根ID的属性名
     * @param pidColumnName    对象pid的属性名
     * @param childrenName     对象装子节点的属性名
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> List<T> buildTree(List<T> nodes, List<T> root, String parentColumnName,
                                         String pidColumnName, String childrenName) throws Exception {
        for (int i = 0; i < root.size(); i++) {

            T rootT = root.get(i);
            Class<?> tClass = rootT.getClass();
            Field rootField = tClass.getDeclaredField(parentColumnName);
            Field childrenNameField = tClass.getDeclaredField(childrenName);
            rootField.setAccessible(true);
            childrenNameField.setAccessible(true);
            Object id = rootField.get(rootT);

            List<T> children = new ArrayList<>();
            for (T t : nodes) {
                Field field = t.getClass().getDeclaredField(pidColumnName);
                field.setAccessible(true);
                Object pid = field.get(t);

                if (id.equals(pid)) {
                    children.add(t);
                }
            }
            buildTree(nodes, children, parentColumnName, pidColumnName, childrenName);
            childrenNameField.set(rootT, children);
        }
        return root;
    }

    // 需要将list先进行排序，按照parentId
    public static <T> List<T> getTreeForSortList(List<T> nodes, String parentColumnName, String pidColumnName, String childrenName) throws Exception {
        List<T> root = new ArrayList<T>();
        if (nodes.size() < 1) {
            return root;
        }
        root.add(nodes.get(0));
        return buildTreeForSortList(nodes, root, parentColumnName, pidColumnName, childrenName);
    }

    private static <T> List<T> buildTreeForSortList(List<T> nodes, List<T> root, String parentColumnName,
                                                    String pidColumnName, String childrenName) throws Exception {
        for (int i = 0; i < root.size(); i++) {
            T rootT = root.get(i);
            Class<?> tClass = rootT.getClass();
            Field rootField = tClass.getDeclaredField(parentColumnName);
            Field childrenNameField = tClass.getDeclaredField(childrenName);
            rootField.setAccessible(true);
            childrenNameField.setAccessible(true);
            Object id = rootField.get(rootT);

            List<T> children = new ArrayList<>();
            boolean match = false;
            for (T t : nodes) {
                Field field = t.getClass().getDeclaredField(pidColumnName);
                field.setAccessible(true);
                Object pid = field.get(t);
                if (id.equals(pid)) {
                    match = true;
                    children.add(t);
                    continue;
                }
                if (match) {
                    match = false;
                    break;
                }
            }
            buildTree(nodes, children, parentColumnName, pidColumnName, childrenName);
            childrenNameField.set(rootT, children);
        }
        return root;
    }
}
