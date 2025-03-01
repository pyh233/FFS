package com.example.flyfishshop.api;

import com.example.flyfishshop.model.Brand;
import com.example.flyfishshop.service.BrandService;
import com.example.flyfishshop.util.validate.CommonAddGroup;
import com.example.flyfishshop.util.validate.CommonEditGroup;
import com.example.flyfishshop.util.JsonResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/api/v1/brand",produces = MediaType.APPLICATION_JSON_VALUE)
public class BrandApi {
    BrandService brandService;

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    // 实际上这并不是brand自己独自使用的接口，可能是商品下俩列表需要这些数据等等...
    @GetMapping("/all")
    public ResponseEntity<JsonResult> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        if (brands.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(JsonResult.success("查询成功", brands));
        }
    }

    @GetMapping
    public ResponseEntity<JsonResult> getShowBrand(Brand brand,
                                                   @RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "5") Integer limit) {
        Page<?> pages = new Page<>(page, limit);
        List<Brand> brandList = brandService.getAllBrands(brand, pages);

        // 可序列化的pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        if (brandList.isEmpty()) {
            return ResponseEntity.ok(JsonResult.fail("查询品牌失败"));
        } else {
            JsonResult jr = JsonResult.success("查询成功", brandList);
            jr.setCount((int) pageInfo.getTotal());
            return ResponseEntity.ok(jr);
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> addBrand(@Validated(CommonAddGroup.class) Brand brand) {
        boolean success = brandService.saveBrand(brand);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("添加成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("添加失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> updateBrand(@Validated(CommonEditGroup.class) Brand brand) {
        boolean success = brandService.updateBrand(brand);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改成功", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("修改失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> deleteBrandByIds(Integer[] ids) {
        int count = brandService.deleteBrandByIds(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除" + count + "条数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除失败"));
        }
    }
}
