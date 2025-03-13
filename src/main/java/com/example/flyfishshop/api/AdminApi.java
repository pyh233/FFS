package com.example.flyfishshop.api;

import com.example.flyfishshop.model.Admin;
import com.example.flyfishshop.service.AdminService;
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
@RequestMapping(value = "/admin/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminApi {
    AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> findAll(Admin admin,
                                              @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "limit", defaultValue = "5") Integer limit) {
        // TODO:删除最后一整页，传入的pageNo应该-1
        Page<?> page = new Page<>(pageNo, limit);
        List<Admin> adminList = adminService.findAll(admin, page);
        PageInfo<?> pageInfo = new PageInfo<>(adminList);
        if(!adminList.isEmpty()){
            JsonResult jr = JsonResult.success("查询管理员信息成功", adminList);
            jr.setCount((int)pageInfo.getTotal());
            return ResponseEntity.ok(jr);
        }else {
            return ResponseEntity.ok(JsonResult.fail("查询管理员信息失败"));
        }
    }
    @DeleteMapping
    public ResponseEntity<JsonResult> delete(Integer[] ids) {
        int count = adminService.delete(ids);
        if(count > 0){
            return ResponseEntity.ok(JsonResult.success("删除成功,共删除"+count+"条管理员数据",count));
        }else {
            return ResponseEntity.ok(JsonResult.fail("删除失败，可能存在有关数据!"));
        }
    }
    @PostMapping
    public ResponseEntity<JsonResult> add(@RequestBody @Validated(CommonAddGroup.class) Admin admin) {
        boolean success = adminService.insert(admin);
        if(success){
            return ResponseEntity.ok(JsonResult.success("成功添加一条信息",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("添加失败,用户名已存在"));
        }
    }
    @PutMapping
    public ResponseEntity<JsonResult> update(@Validated(CommonEditGroup.class) Admin admin) {
        boolean success = adminService.update(admin);
        if(success){
            return ResponseEntity.ok(JsonResult.success("成功添加一条信息",null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("添加失败"));
        }
    }
}
