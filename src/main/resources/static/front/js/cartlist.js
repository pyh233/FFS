$(()=>{
   // 初始化账单
   reloadBill(cartlist);
   // FIXME:这个模板绑定了默认事件全体增/减，吧默认事件给他清除了
   $(".minus").off();
   $(".plus").off();

   // 删除事件
   $(".container>.row").on("click",".deleteFormMyCart",function (e){
      e.preventDefault();
      let itemId = $(this).data("item-id");
      layer.confirm("确定删除?",function (index) {
         deleteById(itemId);
         layer.close(index);
      });
   });
   // 减少数量按钮,当减少到0的时候删除
   $("#cartListShow").on("click",".minus",function (e) {
      e.preventDefault();
      $(this).off();
      let $input = $(this).parent().next().find("[type=text]");
      // 获取当前数量
      let curr_qty = $input.val();
      // 获取改变的目标商品id配合后台的用户id修改数据库
      let goodId = $input.attr("id");
      // 进行修改
      let qty = parseInt(curr_qty) - 1;

      // 1.不能按到0以下，按 到0的时候直接删除

      if(parseInt(curr_qty) === 1){
         // 不用减1了，直接删除;
         layer.confirm("即将删除此商品,您确认?",function (index){
            let id = $input.data("item-id");
            deleteById(id);
            layer.close(index);
         });
      }else{
         $input.val(qty);
         updateQty(goodId,qty,$input);
      }
   });
   // 增加数量按钮
   $("#cartListShow").on("click",".plus",function (e) {
      e.preventDefault();
      $(this).off();
      let $input = $(this).parent().prev().find("[type=text]");
      // 获取当前数量
      let curr_qty = $input.val();
      // 获取改变的目标商品id配合后台的用户id修改数据库
      let goodId = $input.attr("id");
      // 进行修改
      let qty = parseInt(curr_qty) + 1;
      $input.val(qty);
      updateQty(goodId,qty,$input);
   });
   // 创建订单按钮
   $(".checkout-btn").click(function (e) {
      e.preventDefault();
      let ids = [];
      for(let items of $(".deleteFormMyCart")){
         if($(items).data("item-id") ===undefined){
            break;
         }
         ids.push($(items).data("item-id"));
      }
      createOrder(ids);
   });
});
// 修改数量请求
function updateQty(goodId,qty,$input){
   if(window.task){
      clearTimeout(window.task);
   }
   window.task = setTimeout(function () {
      //将修改后的值更新到数据库后
      const url = ctx + "/cart/edit";
      $.ajax({
         url,
         method: "patch",
         data: {
            goodId,
            qty
         },
         success(resp) {
            if (resp.success) {
               layer.msg(resp.msg);
               //FIXME:我希望在前端快速更新账单而不是在更新数据后
               reloadBill(resp.data);
            }
         },
         error(resp) {
            if (resp.status === 401) {
               location.href = ctx + "/login";
            }else {
               layer.msg(resp.responseJSON.msg);
            }
         }
      });
   }, 500);
}
// 删除请求
function deleteById(id){
   let url = ctx + "/cart/delete"
   $.ajax({
      url,
      method:"delete",
      dataType:"json",
      data:{
         id
      },
      success(resp){
         layer.msg(resp.msg);
         // 重新加载购物车列表
         let cartList = resp.data;
         $("#cartListShow").empty();
         for(let item of cartList){
            let $cartItemTemplate = $("#cart-item-template").clone().removeAttr("id");
            $cartItemTemplate.find(".row img").first().attr("src",ctx + "/static/" + item.good.pic);

            $cartItemTemplate.find(".cart-title>h6").text(item.good.name);

            $cartItemTemplate.find(".deleteFormMyCart").data("item-id",item.id);

            $cartItemTemplate.find(".cart-price").text('$'+item.good.price+' x 1 ');

            $cartItemTemplate.find(".cart-price").next().text('单品总价:$'+(item.qty * item.good.price));

            $cartItemTemplate.find(".input-number").val(item.qty).attr("id",item.good.id).data("item-id",item.id);

            $cartItemTemplate.css("display","block");
            $("#cartListShow").append($cartItemTemplate);
         }
         // FIXME:重载账单其实不需要cartList，因为数据已经放在页面上了，可以通过jquery更新数据
         reloadBill(cartList);
      },
      error(resp){
         // 拦截器返回401
         if (resp.status === 401){
            location.href = ctx + "/login";
         }else {
            layer.msg(resp.responseJSON.msg);
         }
      }
   });
}
// FIXME:加载账单
function reloadBill(cartList){
   let total = 0;
   for(let item of cartList){
      total += item.qty * item.good.price;
   }
   let discount = 20;
   let tax = (total - discount) * 0.0001;
   let $payment = $(".cart-total-amount>ul");
   $payment.find("li>span").first().text("$"+total.toFixed(2));
   $payment.find("li>.ship").text("Free");
   $payment.find("li>.discount").text("$"+discount);
   $payment.find("li>.tax").text("$"+tax.toFixed(2));
   $payment.find("li>span").last().text("$"+(total - discount + tax).toFixed(2));
}

// 创建订单
function createOrder(ids){
   let url = ctx + "/order/create";
   $.ajax({
      url,
      method:"post",
      data:{
         ids
      },
      traditional:true,
      success(resp){
         if(resp.success){
            location.href = ctx + "/order/checkout?id="+resp.data;
         }else{
            layer.msg(resp.msg);
         }
      },
      error(resp){
         if(resp.status===401){
            location.href = ctx + "/login";
         }
      }
   });
}
