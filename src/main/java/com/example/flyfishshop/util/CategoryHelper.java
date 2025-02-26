package com.example.flyfishshop.util;

import com.example.flyfishshop.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CategoryHelper {
    // NOTE:判断树中是否存在某节点id为给定的id值
    public static boolean updateCategoryParent(Category categoryTree, Integer compareId) {
        // 先对比当前节点
        if (categoryTree.getId().equals(compareId)) {
            return true;
        }
        // 再对比多颗子树节点
        if (categoryTree.getChildren() != null && !categoryTree.getChildren().isEmpty()) {// 子树不空就一一对比子树id
            for (Category child : categoryTree.getChildren()) {
                if (updateCategoryParent(child, compareId)) {// 如果在子节点中找到匹配的节点返回true
                    return true;
                }
            }
        }
        // 一直没有发现相同id节点
        return false;
    }
    // NOTE:获取一颗树所有的id
    public static Integer[] getCategoryTreeAllId(Category categoryTree) {
        List<Integer> idList = new ArrayList<>();
        Stack<Category> stack = new Stack<>();
        stack.push(categoryTree);
        while (!stack.isEmpty()) {
            Category c = stack.pop();
            if(c != null) {
                idList.add(c.getId());
                for (Category child : c.getChildren()) {
                    stack.push(child);
                }
            }
        }
        return idList.toArray(new Integer[idList.size()]);
    }
}
