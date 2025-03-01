package com.example.flyfishshop.service.impl;

import com.example.flyfishshop.dao.CategoryDao;
import com.example.flyfishshop.model.Category;
import com.example.flyfishshop.service.CategoryService;
import com.example.flyfishshop.util.CategoryHelper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
// 本类service的唯一缓存
@CacheConfig(cacheNames = "CategoryService")
public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao;

    @Autowired
    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Deprecated(since = "全部查询出的数据每一条都会递归,因此下面会多出多余的条目")
    @Cacheable(keyGenerator = "myKeyGenerator")
    public List<Category> findAll() {
//        return categoryDao.findAll();
        return null;
    }

    // 查询根节点
    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Category findCategoryTreeById(Integer id) {
        return categoryDao.findTreeById(id);
    }

    @Override
    @Cacheable(keyGenerator = "myKeyGenerator")
    public Category findNodeById(Integer id) {
        return categoryDao.findNodeById(id);
    }


    @Override
    @CacheEvict(allEntries = true)
    public int deleteCategoryByIds(Integer[] ids) {
        List<Integer> idList = new ArrayList<>();
        //  参数验证
        if(ids == null || ids.length == 0) {
            return 0;
        }
        // 安全检查
        for (Integer id : ids) {
            // this无法触发AOP缓存,只有代理类才能使用AOP缓存
            // 获取代理Service类应该在Configuration上添加注解@EnableAspectJAutoProxy
            CategoryService cs = (CategoryService) AopContext.currentProxy();
            // 可能存在id下为空类别
            Category delCategory = cs.findCategoryTreeById(id);
            if(delCategory == null) {
                continue;
            }
            idList.add(id);
            // 不为空检查是否有子类
            List<Category> categories = delCategory.getChildren();
            if (!categories.isEmpty()) {
                throw new RuntimeException("删除失败，存在子类别未删除");
            }
        }
        if(idList.isEmpty()) {
            throw new RuntimeException("请传入至少一个有效的商品id");
        }
        // TODO:判断是否有商品属于此类别
        return categoryDao.deleteByIds(idList.toArray(new Integer[idList.size()]));
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addCategory(Category category) {
        return categoryDao.insert(category) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateCategory(Category updateCategory) {
        // NOTE:从前台传来的种类没有子节点无法进行合法性判断
        // NOTE:需要判断修改后的父类id是否是自身以及子类，则需要以自身为树查询
        Category categoryWithChildren = categoryDao.findTreeById(updateCategory.getId());
        categoryWithChildren.setParentId(updateCategory.getParentId());
        if(CategoryHelper.updateCategoryParent(categoryWithChildren,updateCategory.getParentId())) {
            // 如果发现种类改变有问题，直接返回false
            return false;
        }
        return categoryDao.update(updateCategory) > 0;
    }

}
