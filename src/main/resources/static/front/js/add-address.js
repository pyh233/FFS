$(() => {
    // 省份列表
    let $select_prov = $("[name=province]").next();
    $select_prov.click(function (e) {
        renderProvinceData($(this).find("ul"));
    });
    // 省份下拉点击li
    $select_prov.on("click", "ul>li", function (e) {
        $(this).parent().parent().prev().val($(this).data("province-id"));
        window.provinceId = $(this).data("province-id");
    });
    // 市列表
    let $select_city = $("[name=city]").next();
    $select_city.click(function (e) {
        if (!window.provinceId) {
            $(this).find("ul").css("display", "none");
            layer.msg("请先选择省份!");
        } else {
            renderCityData(window.provinceId, $(this).find("ul"));
            $(this).find("ul").show();
        }
    });
    // 城市下拉点击li
    $select_city.on("click", "ul>li", function (e) {
        $(this).parent().parent().prev().val($(this).data("city-id"));
        window.cityId = $(this).data("city-id");
    });
    // 区域列表
    let $select_area = $("[name=area]").next();
    $select_area.click(function (e) {
        if (!window.cityId) {
            $(this).find("ul").css("display", "none");
            layer.msg("请先选择城市!");
        } else {
            renderAreaData(window.cityId, $(this).find("ul"));
            $(this).find("ul").show();
        }
    });
    // 区域下拉点击li
    $select_area.on("click", "ul>li", function (e) {
        $(this).parent().parent().prev().val($(this).data("area-id"));
        window.areaId = $(this).data("area-id");
    });
    //  接到列表
    let $select_street = $("[name=street]").next();
    $select_street.click(function (e) {
        if (!window.areaId) {
            $(this).find("ul").css("display", "none");
            layer.msg("请先选择区域!");
        } else {
            renderStreetData(window.areaId, $(this).find("ul"));
            $(this).find("ul").show();
        }
    });
    // 街道下拉点击li
    $select_street.on("click", "ul>li", function (e) {
        $(this).parent().parent().prev().val($(this).data("street-id"));
        window.streetId = $(this).data("street-id");
    });
    //  添加地址按钮事件
    $(".btn").click(function (e) {
        e.preventDefault();
        addAddress();
    })
});

function renderProvinceData($ul) {
    $.ajax({
        url: ctx + "/front/api/v1/province",
        method: "get",
        success(resp) {
            $ul.empty();
            for (let li of resp.data) {
                let $li = $("<li>").data("province-id", li.id);
                $li.addClass("option");
                $li.text(li.province);
                $ul.append($li);
            }
        },
        error(resp) {
            layer.msg(resp.msg);
        }
    });
}

function renderCityData(provinceId, $ul) {
    $.ajax({
        url: ctx + "/front/api/v1/city",
        method: "get",
        data: {
            provinceId
        },
        success(resp) {
            $ul.empty();
            for (let li of resp.data) {
                let $li = $("<li>").data("city-id", li.id);
                $li.addClass("option");
                $li.text(li.city);
                $ul.append($li);
            }
        },
        error(resp) {
            layer.msg(resp.msg);
        }
    });
}

function renderAreaData(cityId, $ul) {
    $.ajax({
        url: ctx + "/front/api/v1/area",
        method: "get",
        data: {
            cityId
        },
        success(resp) {
            $ul.empty();
            for (let li of resp.data) {
                let $li = $("<li>").data("area-id", li.id);
                $li.addClass("option");
                $li.text(li.area);
                $ul.append($li);
            }
        },
        error(resp) {
            layer.msg(resp.msg);
        }
    });
}

function renderStreetData(areaId, $ul) {
    $.ajax({
        url: ctx + "/front/api/v1/street",
        method: "get",
        data: {
            areaId
        },
        success(resp) {
            $ul.empty();
            for (let li of resp.data) {
                let $li = $("<li>").data("street-id", li.id);
                $li.addClass("option");
                $li.text(li.street);
                $ul.append($li);
            }
        },
        error(resp) {
            layer.msg(resp.msg);
        }
    });
}


function addAddress() {
    // 获取已有信息,除了备注都不能缺少
    let streetId = window.streetId;
    let receiver = $("[name=receiver]").val();
    let phone = $("[name=phone]").val()
    let detail = $("[name=detail]").val();

    if (receiver && streetId && detail) {
        // 发送补丁请求
        layer.confirm("确认保存此地址?请您仔细检查.", function (index) {
            layer.close(index);
            let url = ctx + "/my/address/add"
            $.ajax({
                url,
                method: "post",
                dataType: "json",
                data: {
                    streetId,
                    detail,
                    phone,
                    receiver
                },
                success(resp) {
                    if (resp.success) {
                        layer.msg("添加成功");
                        setTimeout("location.href=ctx+'/my/address'",1400);
                    } else {
                        //  修改信息失败
                        layer.msg(resp.msg);
                    }
                },
                error(resp) {
                    if (resp.status === 401) {
                        location.href = ctx + "/login";
                    } else if (resp.status === 400) {
                        layer.msg(resp.responseJSON.msg);
                    }
                }
            });
        });
    } else {
        layer.msg("请输入完整信息!");
    }
}
