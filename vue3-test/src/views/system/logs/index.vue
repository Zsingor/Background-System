<template>
  <div class="background">
    <vxe-grid ref="xGrid" v-bind="gridOptions" @cell-dblclick="dbclickHandler" class="table1">
      <!--  表格上方的查询表单    -->
      <template #form>
        <query-form/>
      </template>
      <!--  表格上方的按钮组    -->
      <template #toolbar_buttons class="buttons">
        <vxe-button status="danger" @click="addmessage">批量删除</vxe-button>
      </template>
      <!--  所属角色显示显示    -->
      <template #role="{ row }">
        <div>{{ rootData.rolesList[row.roleid] }}</div>
      </template>
      <!--  操作按钮组    -->
      <template #operate="{ row }">
        <vxe-button title="删除" circle @click="deleteTableData(xGrid,'/user/delete',false,row)">
          <el-icon>
            <Delete/>
          </el-icon>
        </vxe-button>
      </template>
    </vxe-grid>
  </div>
</template>

<script setup>
import {provide, reactive, ref, onMounted, onActivated, onDeactivated} from 'vue'
import QueryForm from "@/views/system/logs/components/QueryForm.vue";
import request from "@/request/index.js";
import {VxeTableCommonsConfig, dbclickHandler, resetWatch, deleteTableData} from "@/utils/tableconfig";
import {persistentConfig} from "@/layout/layout.js";
import {parseDate} from "@/utils/commons.js";

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'logs'
})

const xGrid = ref()


const rootData = reactive({
  queryData: {},
  rolesMenu:[],
  rolesList:{}
})

const gridOptions = reactive({
  rowId: 'id',
  ...VxeTableCommonsConfig,
  columns: [
    {type: 'checkbox', width: 50, fixed: 'left'},
    {type: 'seq', width: 50},
    {field: 'username', title: '用户名称',minWidth:120},
    {field: 'roleid', title: '所属角色',minWidth:120,slots: {default: 'role'}},
    {field: 'module', title: '操作模块',minWidth:120},
    {field: 'operate', title: '操作内容',minWidth:120},
    {field: 'details', title: '详细内容',minWidth:120},
    {field: 'ip', title: 'IP地址',minWidth:120},
    {field: 'operatedate', title: '操作时间',minWidth:120,formatter: "formatDate"},
    {title: '操作', minWidth: 50, fixed: 'right', slots: {default: 'operate'}}
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
        const params={
          currentPage:page.currentPage,
          pageSize:page.pageSize,
          queryForm:rootData.queryData
        }
        return new Promise((resolve, reject) => {
          request.post("/logs/query", params).then(res => {
            console.log("res", res)
            resolve({
              page: {
                total: res.data.rowSum
              },
              result: res.data.resultList
            })
          }).catch(() => {
            reject()
          })
          request.post("/roles/queryAll").then(res => {
            rootData.rolesMenu = res.data
            res.data.forEach(item => {
              rootData.rolesList[item.id] = item.name
            })
          })
        })
      },
      //用于导出全部数据时的查询
      queryAll: () => {
        xGrid.value.clearCheckboxRow()
        const params={
          currentPage:1,
          pageSize:1000000,
          queryForm:rootData.queryData
        }
        return new Promise((resolve, reject) => {
          request.post("/logs/query", params).then(res => {
            console.log("res", res)
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