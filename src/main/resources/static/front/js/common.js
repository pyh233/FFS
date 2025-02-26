$(() => {
    //菜单
    displayCategory();
    $(".btnn").click(function (event) {
        event.preventDefault();
        let params = "categoryId=" + 1;
        let keyword = $("input[name=keyword]").val();
        if (keyword && keyword !== '') {
            params += "&" + "keyword=" + keyword;
        }
        location.href = ctx + "/shop-list?" + params;
    });
});

function displayCategory() {
    let url = ctx + "/front/api/v1/categories"
    $.ajax({
        url,
        method: "get",
        success(resp) {
            let categories = resp.data.children;
            // 一级菜单
            let $ul_1 = $("#nav-menu-categories");
            for (let cat of categories) {
                let $li_1 = $("<li>");
                let $a_1 = $("<a>").attr("href", ctx + "/shop-list?categoryId=" + cat.id).text(cat.name);
                // 添加默认图标
                let $i_1_1 = $("<i>").addClass("flaticon-shopping-bag category-icon")
                //  如果有图标直接替换掉
                if (cat.iconCls) {
                    $i_1_1.attr("class", cat.iconCls);
                }
                $a_1.prepend($i_1_1);
                $li_1.append($a_1);
                //  二级菜单
                if (Array.isArray(cat.children) && cat.children.length > 0) {
                    // 先添加二级菜单提示箭头
                    let $i_1_2 = $("<i>").addClass("fal fa-angle-right").css("aria-hidden", "true");
                    $a_1.append($i_1_2);
                    // 创建二级菜单
                    let $ul_2 = $("<ul>").addClass("sub-category");
                    for (let cat_2 of cat.children) {
                        let $li_2 = $("<li>");
                        let $a_2 = $("<a>").attr("href", ctx + "/shop-list?categoryId=" + cat_2.id).text(cat_2.name);
                        // 添加默认图标
                        let $i_2_1 = $("<i>").addClass("flaticon-shopping-bag category-icon");
                        if (cat_2.iconCls) {
                            //? 不要把attr添加属性和css添加样式混了
                            $i_2_1.attr("class", cat_2.iconCls);
                        }
                        $a_2.prepend($i_2_1);
                        $li_2.append($a_2);
                        // 三级菜单
                        if (Array.isArray(cat_2.children) && cat_2.children.length > 0) {
                            // 需要让二级菜单ul2添加属性
                            // 二级菜单上添加指示箭头
                            let $i_2_2 = $("<i>").addClass("fal fa-angle-right").css("aria-hidden", "true");
                            $a_2.append($i_2_2);
                            // 创建三级菜单
                            let $ul_3 = $("<ul>").addClass("sub-sub-category");
                            for (let cat_3 of cat_2.children) {
                                let $li_3 = $("<li>");
                                let $a_3 = $("<a>").attr("href", ctx + "/shop-list?categoryId=" + cat_3.id).text(cat_3.name);
                                // 添加默认图标
                                let $i_3_1 = $("<i>").addClass("flaticon-shopping-bag category-icon");
                                if (cat_3.iconCls) {
                                    $i_3_1.attr("class", cat_3.iconCls);
                                }
                                $a_3.prepend($i_3_1);
                                $li_3.append($a_3);
                                // 四级菜单在这里断开
                                $ul_3.append($li_3);
                            }
                            $li_2.append($ul_3)
                        }
                        $ul_2.append($li_2);
                    }
                    $li_1.append($ul_2);
                }
                $ul_1.append($li_1);
            }
        },
        error(resp) {
            console.log(resp.responseJSON.msg);
        }
    });
}
