package com.example.flyfishshop.api.front;

import com.example.flyfishshop.model.Category;
import com.example.flyfishshop.model.Good;
import com.example.flyfishshop.model.search.FrontGoodSearchModel;
import com.example.flyfishshop.service.CategoryService;
import com.example.flyfishshop.service.GoodService;
import com.example.flyfishshop.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/front/api/v1")
public class FrontApi {
    private CategoryService categoryService;
    private GoodService goodService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @Autowired
    public void setGoodService(GoodService goodService) {
        this.goodService = goodService;
    }

    // 导航栏种类列表
    @RequestMapping("/categories")
    public ResponseEntity<JsonResult> getCategoriesTree() {
        Category categoryTree = categoryService.findCategoryTreeById(1);
        if (categoryTree != null) {
            return ResponseEntity.ok(JsonResult.success("查询成功", categoryTree));
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    // 分页查询数据请求
    @GetMapping("/goods")
    public ResponseEntity<JsonResult> getGoods(FrontGoodSearchModel fgsm,
                                               @RequestParam(defaultValue = "1")Integer pageNo, Model model) {
        fgsm.setName(fgsm.getKeyword());
        Page<?> page =new Page<>(pageNo,8);
        List<Good> goodList = goodService.getAllGoods(fgsm,page);

        PageInfo<?> pageInfo = new PageInfo<>(goodList);

        if(goodList.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            JsonResult jr = JsonResult.success("find goods", goodList);
            model.addAttribute("currentPage",pageInfo.getPageNum());
            jr.setPageInfo(pageInfo);
            return ResponseEntity.ok(jr);
        }
    }
}
