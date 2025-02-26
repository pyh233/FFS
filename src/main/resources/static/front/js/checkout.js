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
    loadBillData();
    patchOrderAndPutOnPay();
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


//  加载账单数据和结算未完成的收货人数据
function loadBillData() {
    $("[name=receiver]").val(order.receiver);
    $("[name=receiverPhone]").val(order.receiverPhone);
    // TODO:数据库order表中应该查询streetId
    $("[name=detail]").val(order.receiverDetailAddress);
    $("[name=note]").val(order.note);
    /*===========账单===========*/
    let total = 0;
    let tax = 0;
    for (let item of order.orderItemList) {
        total += parseInt(item.price) * item.count;
    }
    tax = (total - 20) * 0.0001;
    let $div = $(".cart-total-amount");
    $div.find(".cart-subtotal").text('$' + total.toFixed(2));
    $div.find(".tax").text('$' + tax.toFixed(2));
    $div.find(".last>span").text('$' + ((total - 20) - tax).toFixed(2));
}

// 将数据添加到订单上,并且提交订单数据提起支付
function patchOrderAndPutOnPay() {
    $(".checkout-section").click(function (e) {
        e.preventDefault();
        // 获取已有信息,除了备注都不能缺少
        let receiver = $("[name=receiver]").val();
        let receiverPhone = $("[name=receiverPhone]").val();
        // TODO:DELETE
        let countryName = $("[name=country_name]").next().find(".current").text();

        let province = $("[name=province]").next().find(".current").text();
        let city = $("[name=city]").next().find(".current").text();
        let area = $("[name=area]").next().find(".current").text();
        let street = $("[name=street]").next().find(".current").text();
        let streetId = window.streetId;
        let detail = $("[name=detail]").val();

        let note = $("[name=note]").val();
        if (receiver && receiverPhone && province && city && area && street && streetId && detail) {
            // 发送补丁请求
            let totalPay = $(".cart-total-amount .last span").text();
            totalPay = parseFloat(totalPay.substring(1, totalPay.length));
            layer.confirm("即将发起支付宝支付，确认?", function (index) {
                layer.close(index);
                let url = ctx + "/order/patch"
                $.ajax({
                    url,
                    method: "patch",
                    dataType: "json",
                    data: {
                        id: order.id,
                        orderNo: order.orderNo,
                        memberId: order.memberId,
                        state: order.state,
                        createdTime: order.createdTime,
                        totalPay: totalPay,
                        note: note,
                        receiver,
                        receiverPhone,
                        receiverDetailAddress: province + city + area + street + detail,
                        payType:"支付宝",
                        streetId
                    },
                    success(resp) {
                        if(resp.success){
                            location.href = ctx +"/order/alipay?id="+resp.data.id;
                        }else {
                            //  修改信息失败
                            layer.msg(resp.msg);
                        }
                    },
                    error(resp) {
                        if (resp.status === 401) {
                            location.href = ctx + "/login";
                        }else if(resp.status === 400){
                            layer.msg(resp.responseJSON.msg);
                        }
                    }
                });
            });
        } else {
            layer.msg("请输入完整信息!");
        }
    });
}
