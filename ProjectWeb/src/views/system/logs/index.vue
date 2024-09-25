<template>
  <div class="background">
    <vxe-grid ref="xGrid" v-bind="gridOptions" @cell-dblclick="dbclickHandler" class="table1">
      <!--  表格上方的查询表单    -->
      <template #form >
        <query-form v-show="queryShow"/>
      </template>
      <!--  表格上方左侧的按钮组    -->
      <template #toolbar_buttons> 
        <vxe-button status="danger" @click="deleteTableData(xGrid,'/logs/delete',true)">批量删除</vxe-button>
      </template>
      <!--  表格上方右侧的按钮组    -->
      <template #toolbar_tools>
        <vxe-button @click="changeSearch" style="margin-right: 10px" icon="vxe-icon-search" circle></vxe-button>
      </template>
      <!--  详细内容    -->
      <template #details="{ row }">
        <el-button type="primary" link @click="showDetails(row)">查看详情</el-button>
      </template>
      <!--  操作结果    -->
      <template #errFlag="{ row }">
        <div v-if="row.errFlag==='1'">
          <span style="color:#67C23A">成功</span>
        </div>
        <div v-else>
          <span style="color:#F56C6C">失败</span>
        </div>
      </template>
      <!--  错误信息    -->
      <template #errMsg="{ row }">
        <el-button type="primary" link @click="showErrmsg(row)">查看详情</el-button>
      </template>
      <!--  操作按钮组    -->
      <template #operate="{ row }">
        <vxe-button title="删除" circle @click="deleteTableData(xGrid,'/logs/delete',false,row)">
          <el-icon>
            <Delete/>
          </el-icon>
        </vxe-button>
      </template>
    </vxe-grid>

    <el-dialog v-model="showdialog" title="详细内容" draggable>
      <el-input
          v-model="rowDetails"
          style="width: 100%"
          readonly
          resize="none"
          :rows="8"
          type="textarea"
      />
    </el-dialog>

    <el-dialog v-model="showErrDialog" title="错误信息" draggable>
      <el-input
          v-model="rowErrmsg"
          style="width: 100%"
          readonly
          resize="none"
          :rows="8"
          type="textarea"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import {provide, reactive, ref, onMounted, onActivated, onDeactivated, unref} from 'vue'
import QueryForm from "@/views/system/logs/components/QueryForm.vue";
import request from "@/request/index.js";
import {
  VxeTableCommonsConfig,
  dbclickHandler,
  resetWatch,
  deleteTableData,
  getToolbarConfig
} from "@/utils/VxeTableConfig.js";
import {persistentConfig} from "@/layout/layout.js";
import {isEmpty, parseDate} from "@/utils/commons.js";
import axios from "axios";

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'logs'
})

const xGrid = ref()
//显示详细内容窗口
let showdialog=ref(false)
//详细内容信息
let rowDetails=ref("")
//显示错误信息窗口
let showErrDialog=ref(false)
//错误信息内容
let rowErrmsg=ref("")
//显示上方查询表单
let queryShow=ref(true)

const rootData = reactive({
  queryData: {},
  rolesMenu: [],
  rolesList: {}
})

const showDetails=(row)=>{
  rowDetails.value=row.details
  showdialog.value=true
}

const showErrmsg=(row)=>{
  rowErrmsg.value=row.errMsg
  showErrDialog.value=true
}

//控制搜索部分的显示与否
const changeSearch=()=>{
  queryShow.value=!queryShow.value
  setTimeout(() => {
    xGrid.value.recalculate(true)
  }, 200)
}

//自定义详细内容的导出格式
const exportDetails=({ row, column, options })=>{
  return row.details
}

//自定义错误信息的导出格式
const exportErrmsg=({ row, column, options })=>{
  return row.errMsg
}

const gridOptions = reactive({
  id:'logs',
  rowId: 'id',
  ...VxeTableCommonsConfig,
  border:false,
  stripe: true,
  toolbarConfig: {
    ...getToolbarConfig(),
    export: true,
  },
  exportConfig: {
    types: ["csv", "xlsx", "html", "xml", "txt"],
    modes: ["current", "selected", "all"],
    columnFilterMethod: ({column}) => {
      if (column.type === 'checkbox' || column.type === 'seq') {
        return false
      } else {
        return !isEmpty(column.property)
      }
    },
  },
  columns: [
    {type: 'checkbox', width: 50, fixed: 'left'},
    {type: 'seq', width: 50},
    {field: 'userid', visible: false, title: '用户ID', minWidth: 100},
    {field: 'username', title: '用户名称', minWidth: 100},
    {field: 'module', title: '操作模块', minWidth: 100},
    {field: 'operate', title: '操作内容', minWidth: 100},
    {field: 'details', title: '详细内容', minWidth: 100,slots: {default: 'details'},exportMethod:exportDetails},
    {field: 'errFlag', title: '操作结果', minWidth: 100,slots: {default: 'errFlag'}},
    {field: 'errMsg', title: '错误信息', minWidth: 100,slots: {default: 'errMsg'},exportMethod:exportErrmsg},
    {field: 'ip', title: 'IP地址', minWidth: 100},
    {field: 'operatedate', title: '操作时间', minWidth: 150, formatter: "formatDate"},
    {title: '操作', minWidth: 60, fixed: 'right', slots: {default: 'operate'}}
  ],
  proxyConfig: {
    seq: true,
    sort: true,
    filter: true,
    form: true,
    props: {
      result: 'result',
      total: 'page.total' // 配置响应结果总页数字段
    },
    // 只接收Promise，具体实现自由发挥
    ajax: {
      query: ({page}) => {
        xGrid.value.clearCheckboxRow()
        const params = {
          currentPage: page.currentPage,
          pageSize: page.pageSize,
          queryForm: rootData.queryData
        }
        return new Promise((resolve, reject) => {
          axios.all([
            request.post("/roles/queryAll"),
            request.post("/logs/query", params)
          ]).then(axios.spread((res1, res2) => {
            //处理全部角色的数据
            rootData.rolesMenu = res1.data
            res1.data.forEach(item => {
              rootData.rolesList[item.id] = item.name
            })
            //处理表格数据
            resolve({
              page: {
                total: res2.data.rowSum
              },
              result: res2.data.resultList
            })
          })).catch(() => {
            reject()
          })
        })
      },
      //用于导出全部数据时的查询
      queryAll: () => {
        xGrid.value.clearCheckboxRow()
        const params = {
          currentPage: 1,
          pageSize: -1,
          queryForm: rootData.queryData
        }
        return new Promise((resolve, reject) => {
          request.post("/logs/query", params).then(res => {
            resolve(res.data.resultList)
          }).catch(() => {
            reject()
          })
        })
      }
    }
  },
})

provide('rootData', rootData)
provide('xGrid', xGrid)


//用于处理改变滑动条时表格的变化
let tableWatch
onMounted(() => {
  if (!persistentConfig.openKeepalive) {
    tableWatch = resetWatch(xGrid.value)
  }
})
onActivated(() => {
  if (persistentConfig.openKeepalive) {
    tableWatch = resetWatch(xGrid.value)
  }
})
onDeactivated(() => {
  tableWatch()
})
</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
}
</style>
