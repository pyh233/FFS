const $ = layui.jquery;
const form = layui.form;
const dropdown = layui.dropdown;
const tree = layui.tree;


$(()=>{
    // 有默认的父种类或者选择的父种类
    if(parentCategory){
        $("[name=parentName]").val(parentCategory.name);
        $("[name=parentId]").val(parentCategory.id);
    }

    // 渲染父类选择按钮
    dropdown.render({
        id:"dw",
        // 需要加一个content以方便渲染请求成功后的树
        content:"<div id='category-tree'></div>",
        elem: '[name=parentName]',
        data: [],
        ready:function (elemPanel,elem){
            let url = ctx + "/admin/api/v1/category/all";
            $.ajax({
                url,
                method:"get",
                success(resp){
                    tree.render({
                        elem:"#category-tree",
                        data:resp.data,
                        onlyIconControl: true,
                        customName:{
                            id:"id",
                            title:"name",
                            children:"children"
                        },
                        click:function (obj){
                            let data = obj.data;
                            $("[name=parentName]").val(data.name);
                            $("[name=parentId]").val(data.id);
                            dropdown.close("dw");
                        }
                    });
                },
                error(resp){
                    let json = resp.responseJSON;
                    layer.msg(json.msg||"渲染失败");
                }
            });
        },
        click: function(data,othis,event){
            console.log("click");
        }
    });
});

function doSubmit(cb){
    let pass = form.validate("#add-form");
    if(!pass){
        return;
    }
    let formData = form.val("add-form");

    let url = ctx + "/admin/api/v1/category"
    $.ajax({
        url,
        data:formData,
        dataType:"json",
        method: "post",
        success(resp) {
            if(typeof cb === "function"){
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
