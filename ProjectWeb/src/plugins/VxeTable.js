import XEUtils from "xe-utils";
import VXETable from "vxe-table";
import VXETablePluginExportXLSX from "vxe-table-plugin-export-xlsx"; //导入vxe-table的xlsx导出插件
import 'vxe-table/lib/style.css' //导入vxe-table的样式
import i18n from '@/i18n/index.js'

VXETable.use(VXETablePluginExportXLSX);

// 定义表格全局格式化
VXETable.formats.mixin({
  // 格式化日期，默认 yyyy-MM-dd HH:mm:ss
  formatDate({ cellValue }, format) {
    return XEUtils.toDateString(cellValue, format || "yyyy-MM-dd HH:mm:ss");
  }
});

// 自定义刷新指令，不携带任何附加条件（接口必传参数除外）
VXETable.commands.add("customRefresh", (params) => {
  const { $grid } = params;
  $grid.commitProxy("query", true).then(() => {
  });
});
// 控制表单显示隐藏
VXETable.commands.add("showForm", (params) => {
  const { $grid } = params;
  $grid.props.formConfig.enabled = !$grid.props.formConfig.enabled;
  $grid.recalculate(true).then(() => {
  });
});

VXETable.setConfig({
  // 对组件内置的提示语进行国际化翻译
  i18n: (key, args) => i18n.global.t(key, args)
})

export default VXETable;
