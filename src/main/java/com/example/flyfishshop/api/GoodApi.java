package com.example.flyfishshop.api;

import com.example.flyfishshop.model.Good;
import com.example.flyfishshop.service.CategoryService;
import com.example.flyfishshop.service.GoodService;
import com.example.flyfishshop.util.validate.CommonAddGroup;
import com.example.flyfishshop.util.validate.CommonEditGroup;
import com.example.flyfishshop.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin/api/v1/good")
public class GoodApi {
    GoodService goodService;
    CategoryService categoryService;

    @Autowired
    public void setGoodService(GoodService goodService) {
        this.goodService = goodService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> getAllGood(Good searchGood,
                                                 @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "limit") Integer limit) {
        Page<?> page = new Page<>(pageNo, limit);
        List<Good> goodList = goodService.getAllGoods(searchGood, page);
        PageInfo<?> pageInfo = new PageInfo<>(goodList);
        if (goodList.isEmpty()) {
            return ResponseEntity.ok(JsonResult.fail("查询商品信息失败!"));
        } else {
            JsonResult jr = JsonResult.success("查询商品信息成功", goodList);
            jr.setCount((int) pageInfo.getTotal());
            return ResponseEntity.ok(jr);
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteGood(Integer[] ids) {
        System.out.println(Arrays.toString(ids));
        int count = goodService.deleteGoodByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条商品数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除商品信息失败"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addGood(@Validated(CommonAddGroup.class) Good good) {
        boolean success = goodService.addGood(good);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("添加失败"));
        }
    }

    @PutMapping
    ResponseEntity<JsonResult> updateGood(@Validated(CommonEditGroup.class) Good good) {
        boolean success = goodService.updateGood(good);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }
}
