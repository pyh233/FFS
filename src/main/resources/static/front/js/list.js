const layer = layui.layer;
$(() => {
    // NOTE:单独在商品列表页面重新添加一下事件
    $(".btnn").off();
    $(".btnn").click(function (event) {
        event.preventDefault();
        let categoryId = 1;
        if (fgsm && fgsm.categoryId) {
            categoryId = fgsm.categoryId;
        }
        let params = "categoryId=" + categoryId;
        let keyword = $("input[name=keyword]").val();
        if (keyword && keyword !== '') {
            params += "&" + "keyword=" + keyword;
        }
        location.href = ctx + "/shop-list?" + params;
    });
    activeCurrentPage(1);
    // 获取搜索页面后的关键词和类别
    let keyword = fgsm.keyword;
    let categoryId = fgsm.categoryId;

    // 添加分页条事件 根据点击更新当前页面下的pageNum
    $(".pagination>ul").on("click", "li>a", function (e) {
        e.preventDefault();//阻止默认事件

        let page = window.pi.pageNum;//当前页
        let pages = window.pi.pages;//总页数

        let $li = $(this).parent();//li元素
        if ($li.is(".first")) {//首页
            page = 1;
            getGoodsByPageNo(keyword, categoryId, page);
        } else if ($li.is(".last")) {//尾页
            page = pages;
            getGoodsByPageNo(keyword, categoryId, page);
        } else if ($li.is(".prev")) {//前一页
            page--;
            if (page < 1) {
                // 这里并不需要更新页码 因为每次触发导航栏事件后，只有查询数据后才会更新window里面的当前页面数据
                // page = 1;
                layer.msg("已经是第一页了");
                return;
            }
            getGoodsByPageNo(keyword, categoryId, page);
        } else if ($li.is(".next")) {//下一页
            page++;
            if (page > pages) {
                // page = pages;
                layer.msg("已经是最后一页了");
                return;
            }
            getGoodsByPageNo(keyword, categoryId, page);
        } else {//数字页码
            page = parseInt($(this).text());
            getGoodsByPageNo(keyword, categoryId, page);
        }
    });
    //  添加到购物车事件
    $("#goodList").on("click", ".product-cart-btn", function (e) {
        e.preventDefault();
        let goodId = $(this).data("good-id");
        addGoodToCart(goodId);
    });

});

// 初始化active函数
function activeCurrentPage(page) {
    $(".pagination>ul>li>a").each(function () {
        if (parseInt($(this).text()) === page) {
            $(this).parent().addClass("active");
        } else {
            $(this).parent().removeClass("active");
        }
    });
}


// 根据页码获取数据（需要获取当前页面中的keyword和categoryId，如果没有传入空字符串）将数据组合后返回给列表
// 并且更新当前page信息（重要）
function getGoodsByPageNo(keyword = '', categoryId = '', pageNo) {
    $.ajax({
        url: ctx + "/front/api/v1/goods",
        method: "get",
        dataType: "json",
        data: {
            categoryId,
            keyword,
            pageNo
        },
        success(resp) {
            if (resp.success) {
                //  通过ajax不断更新数据和页面上的pageInfo
                window.pi = resp.pageInfo;
                let data = resp.data;
                // 更新列表中的数据
                $("#goodList").empty();
                for (let good of data) {
                    let $good = $("#goodList-template").clone().removeAttr("id");//复制
                    //商品图片与图片上的链接
                    $good.find(".product-img>a").first().attr("href", "/good-show?gid=" + good.id);
                    $good.find("#img-pic-template")
                        .attr("src", ctx + "/static/" + good.pic)
                        .data("src", ctx + "/static/" + good.pic);
                    // 商品名称
                    $good.find(".product-content>h3>a")
                        .attr("href", "/good-show?gid=" + good.id).text(good.name);
                    // 商品简介
                    $good.find(".my-2").text(good.description);
                    // 商品旧价
                    $good.find(".product-price>span").first()
                        .text("$" + good.markPrice);
                    // 商品现价
                    $good.find(".product-price>span").last()
                        .text("$" + good.name);
                    // 添加当前商品id
                    $good.find(".product-cart-btn").data("good-id", good.id);
                    // 显示新的页面并添加到商品列表上
                    $good.show().css("display", "block");
                    $("#goodList").append($good);
                }
                // 更新分页
                $(".pagination>ul>li:not(.first,.last,.prev,.next)").remove();
                let paginationLen = 4;
                for (let i = pi.pageNum - 1; i <= pi.pageNum + 2; i++) {
                    if (i === 0) {
                        i = 1;
                    }
                    if (i === pi.navigateLastPage + 1) {
                        return;
                    }
                    let $li = $("<li>");
                    if (i === pi.pageNum) {
                        $li.addClass("active");
                    }
                    let $a = $("<a>").text(i);
                    $li.append($a);

                    $(".pagination .next").before($li);
                }
            }
        },
        error(resp) {
            let JSON = resp.responseJSON;
            console.log(resp);
        }
    });
}
// 添加到购物车请求
function addGoodToCart(goodId) {
    let url = ctx + "/cart/add";
    $.ajax({
        url,
        method: "post",
        dataType: "json",
        data: {
            goodId,
            qty:1
        },
        success(resp) {
            if(resp.success){
                layer.msg(resp.msg);
            }
        },
        error(resp) {
            // 如果是拦截器返回的，说明需要登录
            if(resp.status === 401){
                location.href = ctx + "/login";
            }else {
                // 如果不是拦截器返回的，就是单纯没有成功添加
                layer.msg(resp.responseJSON.msg);
            }
        }
    });
}
