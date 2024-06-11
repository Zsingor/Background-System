<template>
  <div class="background">
    <vxe-grid ref="xGrid" v-bind="gridOptions" @cell-dblclick="dbclickHandler" class="table1">
      <!--  表格上方的查询表单    -->
      <template #form>
        <query-form/>
      </template>
      <!--  表格上方的按钮组    -->
      <template #toolbar_buttons class="buttons">
        <vxe-button status="primary" @click="addmessage">新增</vxe-button>
      </template>
      <!--  所属角色显示显示    -->
      <template #role="{ row }">
        <div>{{ rootData.rolesList[row.roleid] }}</div>
      </template>
      <!--  状态显示    -->
      <template #status="{ row }">
        <div v-if="row.status==='1'">
          <h4 style="color: #67C23A">已启用</h4>
        </div>
        <div v-else>
          <h4 style="color: #F56C6C">未启用</h4>
        </div>
      </template>
      <!--  操作按钮组    -->
      <template #operate="{ row }">
        <vxe-button title="编辑" circle @click="updaterow(row)">
          <el-icon>
            <Edit/>
          </el-icon>
        </vxe-button>
        <vxe-button title="删除" circle @click="deleterow(row)">
          <el-icon>
            <Delete/>
          </el-icon>
        </vxe-button>
        <el-dropdown trigger="click" style="margin-left: 10px">
          <vxe-button title="更多操作" circle>
            <el-icon>
              <More/>
            </el-icon>
          </vxe-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="assignRoute(row)">
                <el-button type="text">
                  分配路由
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </vxe-grid>

    <SubmitForm></SubmitForm>
  </div>
</template>

<script setup>
import {provide, reactive, ref, onMounted, onActivated, onDeactivated} from 'vue'
import {message} from "@/utils/message.js";
import QueryForm from "@/views/system/people/components/QueryForm.vue";
import request from "@/request/index.js";
import {ElMessageBox} from "element-plus";
import {VxeTableCommonsConfig, dbclickHandler, resetWatch} from "@/utils/tableconfig";
import SubmitForm from "@/views/system/people/components/SubmitForm.vue";
import {getTableConfig} from "@/views/system/people/config.js";
import axios from "axios";
import {isEmpty} from "@/utils/commons.js";
import {persistentConfig} from "@/layout/layout.js";

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'people'
})

const xGrid = ref()
const tableConfig = getTableConfig()


const rootData = reactive({
  name: "测试列表",
  showForm: false,
  showDialog: false,
  submitLoading: false,
  selectRow: null,
  formData: {},
  queryData: {},
  formRules: Object.assign({}, tableConfig.formRules),
  rolesMenu:[],
  rolesList:{}
})

console.log("rootData", rootData.formRules)

onMounted(() => {
  console.log("页面被加载")
})

const addmessage = () => {
  Object.assign(rootData.formData, {
    id:0,
    name:"",
    password:"",
    email:"",
    roleid:null,
    description:"",
    status:"1"
  })
  rootData.name = "添加信息"
  rootData.selectRow = null
  rootData.showForm = true
}

const updaterow = (row) => {
  Object.assign(rootData.formData, row)
  rootData.name = "修改信息"
  rootData.selectRow = row
  rootData.showForm = true
}

const deleterow = (row) => {
  ElMessageBox.confirm('您确定要删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.post("/user/delete", row).then(res => {
      xGrid.value.commitProxy('query')
      if(res.code===1)
      {
        message('删除成功')
      }
      else
      {
        message(res.msg,"error")
      }
    }).catch(() => {
      message("删除失败", "error")
    })
  })
}

const gridOptions = reactive({
  rowId: 'name',
  ...VxeTableCommonsConfig,
  toolbarConfig: tableConfig.toolbarConfig,
  exportConfig: tableConfig.exportConfig,
  columns: tableConfig.columns,
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
        rootData.queryData["currentpage"]=page.currentPage
        rootData.queryData["pagesize"]=page.pageSize
        return new Promise((resolve, reject) => {
          request.post("/user/query", rootData.queryData).then(res => {
            console.log("res", res)
            resolve({
              page: {
                total: res.data.rowSum
              },
              result: res.data.userlist
            })
          }).catch(() => {
            reject()
          })
          request.post("/roles/queryAll").then(res => {
            rootData.rolesMenu = res.data
            res.data.forEach(item => {
              rootData.rolesList[item.id] = item.name
            })
            console.log(rootData.rolesList)
          })
        })
      },
      //用于导出全部数据时的查询
      queryAll: () => {
        xGrid.value.clearCheckboxRow()
        rootData.queryData["currentpage"]=1
        rootData.queryData["pagesize"]=1000000
        return new Promise((resolve, reject) => {
          request.post("/user/query", rootData.queryData).then(res => {
            console.log("res", res)
            resolve(res.data.userlist)
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