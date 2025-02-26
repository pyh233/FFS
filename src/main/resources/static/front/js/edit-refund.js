const form = layui.form;
const $ = layui.$;
$(()=>{

});
function doRefund(cb){
   let formData =  form.val("add-form");
   let url = ctx + "/order/refund";
   $.ajax({
      url,
      method:"put",
      dataType:"json",
      data:{
         orderId:refundOrder.id,
         refundCause:formData.refundCause
      },
      success(resp){
         if(typeof cb === "function"){
            cb(resp.success);
         }
      },
      error(resp){
         if (resp.status === 401){
            location.href = ctx + "/login";
         }
      }
   });
}
