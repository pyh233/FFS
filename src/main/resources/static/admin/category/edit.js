const $ = layui.jquery;
const tree = layui.tree;
const form = layui.form;
const dropdown = layui.dropdown;
$(() => {
    if (error) {
        layer.msg(error);
        return;
    }
    // 首先将父类数据填写(父类只需要填写name,子类中含有父类的id)
    if (parentCategory) {
        $("[name=parentName]").val(parentCategory.name);
    }
    // 然后将子类的数据填写上
    form.val("edit-form", editCategory);
    // 渲染dropdown tree
    dropdown.render({
        id: "dw",
        content: "<div id='category-tree'></div>",
        elem: "[name=parentName]",
        // 加载dropdown结束后 在dropdown上渲染一个tree
        ready() {
            let url = ctx + "/admin/api/v1/category/all";
            $.ajax({
                url,
                method:"get",
                success(resp){
                    tree.render({
                        elem: "#category-tree",
                        data:resp.data,
                        customName:{
                            id:"id",
                            title:"name",
                            children:"children"
                        },
                        click:function (obj){
                            let id = obj.data.id;
                            let name = obj.data.name;
                            if(obj.data.id === editCategory.id){
                                layer.msg("请不要选择类自己作为父类!");
                            }else {
                                $("[name=parentName]").val(name);
                                $("[name=parentId]").val(id);
                            }
                            dropdown.close("dw");
                        }
                    });
                },
                error(resp){
                    let json = resp.responseJSON;
                    layer.msg(json.msg||"渲染失败,请稍后再试");
                }
            });
        }
    });
});

function doEdit(cb){
    // TODO:表单参数验证
    // let pass= form.validate("#");
    // 获取表单数据
    let formData = form.val("edit-form");
    formData.id = editCategory.id;
    let url = ctx+"/admin/api/v1/category";
    $.ajax({
        url,
        method: "put",
        dataType:"json",
        data:formData,
        success(resp) {
            if(typeof cb ==="function"){
                cb(resp);
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
