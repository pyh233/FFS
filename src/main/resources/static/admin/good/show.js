const layer = layui.layer;
const form = layui.form;
const $ = layui.jquery;
const upload = layui.upload;
const dropdown = layui.dropdown;
const element = layui.element;
const tree = layui.tree;
$(() => {
    if (error) {
        layer.msg(error);
    }
    // 渲染类别下拉列表
    //渲染富文本编辑器
    // let ue = UE.getEditor('editor', {
    //     serverUrl: ctx + "/admin/api/v1/ue",
    //     toolbars: [
    //         [
    //             "fullscreen",   // 全屏
    //             "source",       // 源代码
    //             "|",
    //             "undo",         // 撤销
    //             "redo",         // 重做
    //             "|",
    //             "bold",         // 加粗
    //             "italic",       // 斜体
    //             "underline",    // 下划线
    //             "fontborder",   // 字符边框
    //             "strikethrough",// 删除线
    //             "superscript",  // 上标
    //             "subscript",    // 下标
    //             "removeformat", // 清除格式
    //             "formatmatch",  // 格式刷
    //             "autotypeset",  // 自动排版
    //             "blockquote",   // 引用
    //             "pasteplain",   // 纯文本粘贴模式
    //             "|",
    //             "forecolor",    // 字体颜色
    //             "backcolor",    // 背景色
    //             "insertorderedlist",   // 有序列表
    //             "insertunorderedlist", // 无序列表
    //             "selectall",    // 全选
    //             "cleardoc",     // 清空文档
    //             "|",
    //             "rowspacingtop",// 段前距
    //             "rowspacingbottom",    // 段后距
    //             "lineheight",          // 行间距
    //             "|",
    //             "customstyle",         // 自定义标题
    //             "paragraph",           // 段落格式
    //             "fontfamily",          // 字体
    //             "fontsize",            // 字号
    //             "|",
    //             "indent",              // 首行缩进
    //             "|",
    //             "justifyleft",         // 居左对齐
    //             "justifycenter",       // 居中对齐
    //             "justifyright",
    //             "justifyjustify",      // 两端对齐
    //             "|",
    //             "touppercase",         // 字母大写
    //             "tolowercase",         // 字母小写
    //             "|",
    //             "link",                // 超链接
    //             "unlink",              // 取消链接
    //             "anchor",              // 锚点
    //             "|",
    //             "imagenone",           // 图片默认
    //             "imageleft",           // 图片左浮动
    //             "imagecenter",         // 图片居中
    //             "imageright",          // 图片右浮动
    //             "|",
    //             "simpleupload",        // 单图上传
    //             "emotion",             // 表情
    //             "|",
    //             "horizontal",          // 分隔线
    //             "date",                // 日期
    //             "time",                // 时间
    //             "spechars",            // 特殊字符
    //         ]
    //     ]
    // });
    // window.ue = ue;



    // 数据回填
    form.val("show-form", showGood);
    // 回填分类名称
    $("[name=categoryName]").val(showGood.category.name);
    //  回填品牌名称
    $("[name=brandName]").val(showGood.brand.name);
    // 回填单图片
    let url_single = ctx + "/static/" + showGood.pic;
    fillSingleImage(url_single);

    // 回填多图片
    let other_url = showGood.otherPics;
    fillMultipleImage(other_url);
    // 回显富文本
    fillRichText();
});

// 回填单图片事件
function fillSingleImage(url) {
    if (url && url !== "") {
        $("#pic-upload-img").attr("src", url);
        $(".pic-preview").css("background-image", "url(" + url + ")");
    }
}

// 回填多图片函数
function fillMultipleImage(other_url) {
    if (other_url && other_url !== "") {
        other_url = other_url.split(",");
        for (let url of other_url) {
            url = ctx + "/static/" + url;
            $('#otherPic-upload-preview').append('<img src="' + url + '" style="width: 90px; height: 90px;">');
        }
    }
}
//  回填富文本
function fillRichText(){

    // window.ue.ready(()=>{
    //     if(editGood.detail){
    //         window.ue.setContent(editGood.detail);
    //     }
    // });
    let $text = $("[name=detail]");
    $text.val("");
    $text.html(showGood.detail);
}
