package com.example.flyfishshop.api;

import com.example.flyfishshop.model.Category;
import com.example.flyfishshop.service.CategoryService;
import com.example.flyfishshop.util.validate.CommonAddGroup;
import com.example.flyfishshop.util.validate.CommonEditGroup;
import com.example.flyfishshop.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/api/v1/category",produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryApi {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<JsonResult> getCategories() {// TODO:下拉列表使用应该剪掉以当前id为父类的整颗树
        Category root = categoryService.findCategoryTreeById(1);
        List<Category> categoryList = root.getChildren();
        if (!categoryList.isEmpty()) {
            return ResponseEntity.ok(JsonResult.success("查询类别成功", categoryList));
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @DeleteMapping
    public ResponseEntity<JsonResult> deleteCategory(Integer[] ids) {
        try{
            int count = categoryService.deleteCategoryByIds(ids);
            if (count > 0) {
                return ResponseEntity.ok(JsonResult.success("删除成功,共删除" + count + "条类别数据", null));
            } else {
                return ResponseEntity.ok(JsonResult.fail("删除失败，存在未删除的子类或未指定删除项!"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(JsonResult.fail(e.getMessage()));
        }

    }
    @PostMapping
    public ResponseEntity<JsonResult> addCategory(@Validated(CommonAddGroup.class) Category category) {
        boolean success = categoryService.addCategory(category);
        if(success) {
            return ResponseEntity.ok(JsonResult.success("新增类别成功", null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("新增类别失败"));
        }
    }
    @PutMapping
    public ResponseEntity<JsonResult> updateCategory(@Validated(CommonEditGroup.class) Category category) {
        boolean success = categoryService.updateCategory(category);
        if(success) {
            return ResponseEntity.ok(JsonResult.success("修改类别成功", null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("修改类别失败，请不要将此类添加到子类或自身!"));
        }
    }
}
