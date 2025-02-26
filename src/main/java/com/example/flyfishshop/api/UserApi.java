package com.example.flyfishshop.api;

import com.example.flyfishshop.model.search.SearchUserModel;
import com.example.flyfishshop.model.User;
import com.example.flyfishshop.service.UserService;
import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.CommonEditGroup;
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
@RequestMapping(value = "/admin/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApi {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<JsonResult> list(SearchUserModel searchUserModel,
                                           @RequestParam(name = "page", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "limit", defaultValue = "10") Integer pageSize) {
        // page分页工具
        Page<?> page = new Page<>(pageNo, pageSize);
        List<User> users = userService.list(searchUserModel, page);
        // pageInfo可以序列化传给前端,也可以获取pageinfo中的总数，layui需要总数这条数据
        PageInfo<?> pageInfo = new PageInfo<>(users);

        if (!users.isEmpty()) {
            JsonResult jr = JsonResult.success("查询用户成功", users);
            jr.setCount((int) pageInfo.getTotal());
            return ResponseEntity.ok(jr);
        } else {
            return ResponseEntity.ok(JsonResult.fail("查询用户失败"));
        }
    }

    @DeleteMapping
    public ResponseEntity<JsonResult> delete(Integer[] ids) {
        int count = userService.delete(ids);
        if (count > 0) {
            return ResponseEntity.ok(JsonResult.success("成功删除"+count+"条用户数据", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("删除用户数据失败!"));
        }
    }

    @PostMapping
    public ResponseEntity<JsonResult> save(@Validated(CommonAddGroup.class) User user) {
        boolean success = userService.insert(user);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("success", null));
        } else {
            return ResponseEntity.ok(JsonResult.fail("保存用户信息失败"));
        }
    }

    @PutMapping
    public ResponseEntity<JsonResult> update(@Validated(CommonEditGroup.class) User user) {
        // 这个标记会到user中找对应的标记进行验证抛出异常由统一的异常处理结构进行返回？
        boolean success = userService.update(user);
        if (success) {
            return ResponseEntity.ok(JsonResult.success("修改用户信息成功!", null));
        }else {
            return ResponseEntity.ok(JsonResult.fail("修改用户信息失败!"));
        }
    }
}
