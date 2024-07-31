import Clipboard from 'clipboard'
import {message} from "@/utils/message.js";
import _ from "lodash"
import {persistentConfig, windowConfig} from "@/layout/layout.js";
import {unref, watch} from "vue";
import {ElMessageBox} from "element-plus";
import request from "@/request/index.js";

export const pageSizes = [10, 25, 50, 100, 200]

// Vxe高级表格常用配置
export const VxeTableCommonsConfig = {
    //自动调整高度
    height: 'auto',
    //显示边框
    border: true,
    //当内容过长时显示省略号
    showOverflow: true,
    //高亮鼠标所在的行
    highlightHoverRow: true,
    //单元格中信息的位置
    align: 'center',
    //表格是否显示正在加载中
    loading: false,
    //允许列拖动
    columnConfig: {
        resizable: true
    },
    //复选框配置
    checkboxConfig: {
        reserve: false, // 是否保留勾选状态
        highlight: true, // 高亮选中行
        range: true //鼠标滑动选中
    },
    //个性化信息配置项
    customConfig: {
        storage: true //是否将列操作状态保存在本地
    },
    //设置分页
    pagerConfig: {
        align: 'center',  // 指定分页栏布局
        pageSize: getDefaultPageSize(),   //单页大小
        pageSizes: pageSizes, //分页列表
        layouts: ['PrevPage', 'JumpNumber', 'NextPage', 'Sizes', 'FullJump', 'Total']   //自定义布局
    },
    //上方按钮分区
    toolbarConfig: {
        refresh: true, //刷新
        zoom: true, //放大
        custom: true,//个性化配配置
        slots: {
            buttons: 'toolbar_buttons'
        }
    },
}

//双击复制数据
export const dbclickHandler = (event) => {
    const clipboard = new Clipboard(event.$event.target, {
        text: () => event.cell.innerText
    })
    clipboard.on('success', () => {
        message('复制成功')
        clipboard.destroy()
    })
    clipboard.on('error', () => {
        message('复制失败', 'error')
        clipboard.destroy()
    })
}

//获取分页数据，如果直接使用persistentConfig.defaultPageSize会有初始化问题
export function getDefaultPageSize() {
    const appConfig = localStorage.getItem("Global_Config")
    if (appConfig && JSON.parse(appConfig).defaultPageSize) {
        return JSON.parse(appConfig).defaultPageSize
    } else {
        return 25
    }
}

/**
 * 表格重置监听
 * @param xGrid  vxe表格实例
 */
export function resetWatch(xGrid) {
    // 监听菜单缩放，滚动条变化调用重置表格宽高函数
    return watch(
        [
            () => persistentConfig.isCollapse,
            () => persistentConfig.scroll.openCustom,
            () => persistentConfig.scroll.size,
            () => windowConfig.breakpoint
        ],
        () => {
            setTimeout(() => {
                unref(xGrid).recalculate(true)
            }, 500)
        }
    )
}

//返回上方按钮分区
export function getToolbarConfig() {
    return {
        className: 'custom-vxe-table-toolbar',
        refresh: true,
        zoom: true,
        custom: true,
        slots: {
            buttons: 'toolbar_buttons'
        }
    }
}

/**
 * 删除数据（基于vxe高级表格，必须设置rowId）
 * @param $grid   vxe表格实例
 * @param url     接口地址
 * @param batch   是否为批量删除
 * @param row     当前行数据，当batch为false时生效
 */
export function deleteTableData($grid, url, batch, row) {
    if (batch) {
        //批量删除
        const datas = $grid.getCheckboxRecords();
        if (_.isArray(datas) && datas.length > 0) {
            ElMessageBox.confirm("您确定要删除吗？", "提示", {
                type: "warning"
            }).then(() => {
                const ids = [];
                datas.forEach(item => {
                    ids.push(item[$grid.props.rowId]);
                });
                $grid.reactData.tableLoading = true;
                request.post(url, ids).then((res) => {
                    if (res.code === 1) {
                        message("删除成功");
                        $grid.commitProxy("query");
                    }
                    else {
                        message(res.msg,"error")
                    }
                });
            }).catch(() => { }).finally(() => {
                $grid.reactData.tableLoading = false;
            });
        } else {
            message("请选择数据！", "warning");
        }
    } else {
        //单个删除
        ElMessageBox.confirm("您确定要删除吗？", "提示", {
            type: "warning"
        }).then(() => {
            $grid.reactData.tableLoading = true;
            const ids = [];
            ids.push(row[$grid.props.rowId])
            request.post(url, ids).then((res) => {
                if (res.code === 1) {
                    message("删除成功");
                    $grid.commitProxy("query");
                }
                else
                {
                    message(res.msg,"error")
                }
            });
        }).catch(() => { }).finally(() => {
            $grid.reactData.tableLoading = false;
        });
    }
}