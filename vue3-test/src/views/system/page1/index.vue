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
      <!--  持续时间显示    -->
      <template #duration="{ row }">
        <div>{{ row.startdate }} —— {{ row.enddate }}</div>
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
    <RoutesDialog></RoutesDialog>
  </div>
</template>

<script setup>
import {provide, reactive, ref, onMounted, onActivated, onDeactivated} from 'vue'
import {message} from "@/utils/message.js";
import QueryForm from "@/views/system/page1/components/QueryForm.vue";
import request from "@/request/index.js";
import {ElMessageBox} from "element-plus";
import {VxeTableCommonsConfig, dbclickHandler, resetWatch} from "@/utils/tableconfig";
import SubmitForm from "@/views/system/page1/components/SubmitForm.vue";
import {getTableConfig} from "@/views/system/page1/config.js";
import RoutesDialog from "@/views/system/page1/components/RoutesDialog.vue";
import axios from "axios";
import {isEmpty} from "@/utils/commons.js";
import {persistentConfig} from "@/layout/layout.js";

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'page1'
})

const xGrid = ref()
const tableConfig = getTableConfig()


const rootData = reactive({
  name: "测试列表",
  showForm: false,
  showDialog: false,
  submitLoading: false,
  selectRow: null,
  menuDatas: [],
  userMenus: {                                          // 用户拥有的菜单数据
    name: "",
    menus: []
  },
  formData: {},
  queryData: {},
  formRules: Object.assign({}, tableConfig.formRules),
})

console.log("rootData", rootData.formRules)

onMounted(() => {
  console.log("页面被加载")
})

const addmessage = () => {
  Object.assign(rootData.formData, {
    name: "",
    sex: "",
    address: "",
    createdate: "",
    startdate: "",
    enddate: "",
    date_list: [],
  })
  rootData.name = "添加信息"
  rootData.selectRow = null
  rootData.showForm = true
}

const updaterow = (row) => {
  Object.assign(rootData.formData, row)
  rootData.formData.date_list = [row.startdate, row.enddate]
  rootData.name = "修改信息"
  rootData.selectRow = row
  rootData.showForm = true
}

const assignRoute = (row) => {
  rootData.userMenus.name = row.name
  if (rootData.menuDatas.length === 0) {
    // 发送两个请求：1.获取所有菜单  2.获取用户拥有的菜单
    axios.all([
      request.post('/routes/all'),
      request.post("/routes/query", {name: row.name})
    ]).then(axios.spread((menus, userMenus) => {
      rootData.menuDatas = menus.data
      if (!isEmpty(userMenus.data)) {
        userMenus.data.forEach(item => {
          if (isEmpty(item.children)) {
            rootData.userMenus.menus.push(item.id)
          } else {
            item.children.forEach(child => {
              rootData.userMenus.menus.push(child.id)
            })
          }
        })
      }
    })).finally(() => {
      rootData.showDialog = true
    })
  } else {
    request.post("/routes/query", {name: row.name}).then(res => {
      if (!isEmpty(res.data)) {
        res.data.forEach(item => {
          if (isEmpty(item.children)) {
            rootData.userMenus.menus.push(item.id)
          } else {
            item.children.forEach(child => {
              rootData.userMenus.menus.push(child.id)
            })
          }
        })
      }
    }).finally(() => {
      rootData.showDialog = true
    })
  }
}

const deleterow = (row) => {
  ElMessageBox.confirm('您确定要删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.post("/user/delete", row).then(res => {
      console.log(res)
      xGrid.value.commitProxy('query')
      message('删除成功')
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
      // 对应响应结果 Promise<{ result: [], page: { total: 100 } }>
      result: 'result',
      total: 'page.total' // 配置响应结果总页数字段
    },
    // 只接收Promise，具体实现自由发挥
    ajax: {
      query: ({form, sorts, filters, page}) => {
        xGrid.value.clearCheckboxRow()
        return new Promise((resolve, reject) => {
          const params = {
            "currentpage": page.currentPage,
            "pagesize": page.pageSize,
            "queryData": rootData.queryData,
          }
          request.post("/user/query", params).then(res => {
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
        })
      },
      //用于导出全部数据时的查询
      queryAll: () => {
        xGrid.value.clearCheckboxRow()
        return new Promise((resolve, reject) => {
          const params = {
            "currentpage": 1,
            "pagesize": 1000000,
            "queryData": rootData.queryData,
          }
          request.post("/user/query", params).then(res => {
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