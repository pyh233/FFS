$(()=>{
    $(".add-to-cart>a").first().click(function () {
        let url = ctx + "/cart/add";
        let qty = $("[name=qty]").val();
        let goodId = $(this).data("good-id");
        $.ajax({
           url,
           method:"post",
           dataType:"json",
           data:{
               goodId,
               qty,
           },
            success(resp){
               if(resp.success){
                   layer.msg(resp.msg);
               }
            },
            error(resp){
               if(resp.status === 401){
                   location.href = ctx + "/login";
               }else {
                   layer.msg(resp.responseJSON.msg);
               }
            }
        });
    });
});
