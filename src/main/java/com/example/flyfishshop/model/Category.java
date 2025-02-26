package com.example.flyfishshop.model;

import com.example.flyfishshop.util.CommonAddGroup;
import com.example.flyfishshop.util.CommonEditGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Setter
@Getter
@ToString(exclude = "parent")
@JsonIgnoreProperties("handler")
public class Category {
    Integer id;
    @NotEmpty(message = "类名不可为空!",groups = {CommonAddGroup.class, CommonEditGroup.class})
    String name;
    String alias;
    String iconCls;
    Integer seq;
    @NotNull(message = "父类别不可为空!",groups = {CommonAddGroup.class, CommonEditGroup.class})
    Integer parentId;
    String description;
    // 同时存在的时候序列化会导致序列化递归进入死循环
    @JsonBackReference
    Category parent;
    @JsonManagedReference
    List<Category> children;

    public String getParentTreeName() {
        Queue<String> nameQueue = new LinkedList<>();
        Category parent = this.getParent();
        String res = "";
        while (parent != null) {
            nameQueue.add(parent.getName());
            parent = parent.getParent();
        }
        while (!nameQueue.isEmpty()) {
            res += nameQueue.poll() + ">";
        }
        if (res.endsWith(">")) {
            res = res.substring(0, res.length() - 1);
        }
        return res;
    }
}
