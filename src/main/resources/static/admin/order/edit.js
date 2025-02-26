const $ = layui.jquery;
const form = layui.form;
const layer = layui.layer;
const upload = layui.upload;
const laydate = layui.laydate;
const element = layui.element;
$(() => {
    if(error){
        layer.msg(error);
        return;
    }
    form.val("edit-form",editOrder);
});
// 添加事件
function doEdit(cb) {
    // 提交前在前端进行参数验证
    let pass = form.validate("#edit-form")
    if (!pass) {
        return;
    }
    let formData = form.val("edit-form");
    formData.id = editOrder.id;
    const url = ctx + "/admin/api/v1/order";
    $.ajax({
        url,
        method: "patch",
        dataType: "json",
        data: formData,
        success(resp) {
            if (typeof cb === "function") {
                cb(resp.success);
            }
        },
        error(resp){
            if(resp.status === 401){
                location.href = ctx +"/admin/login";
            }else {
                layer.msg(resp.responseJSON.msg);
            }
        }
    });
}
