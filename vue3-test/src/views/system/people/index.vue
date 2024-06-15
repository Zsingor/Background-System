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
        <vxe-button status="danger" @click="deleteTableData(xGrid,'/user/delete',true)">批量删除</vxe-button>
      </template>
      <!--  所属角色显示显示    -->
      <template #role="{ row }">
          <div>
            <el-tag class="item-tag" v-for="(roleid, index) in row.rolesid">
              {{ rootData.rolesList[roleid] }}
            </el-tag>
          </div>
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
        <vxe-button title="删除" circle @click="deleteTableData(xGrid,'/user/delete',false,row)">
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
              <el-dropdown-item @click="assignRole(row)">
                <el-button type="text">
                  分配角色
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </vxe-grid>

    <el-dialog title="分配角色"
               width=30%
               v-model="rootData.showDialog">
      <el-select
          v-model="rootData.userMenus.menus"
          multiple
          placeholder="Select"
          style="width: 100%"
      >
        <el-option
            v-for="item in rootData.rolesMenu"
            :key="item.id"
            :label="item.name"
            :value="item.id"
        />
      </el-select>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="rootData.showDialog = false">取消</el-button>
          <el-button :loading="rootData.submitLoading" type="primary" @click="submitRoles">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>

    <SubmitForm></SubmitForm>
  </div>
</template>

<script setup>
import {provide, reactive, ref, onMounted, onActivated, onDeactivated, unref} from 'vue'
import QueryForm from "@/views/system/people/components/QueryForm.vue";
import request from "@/request/index.js";
import {VxeTableCommonsConfig, dbclickHandler, resetWatch, deleteTableData} from "@/utils/tableconfig";
import SubmitForm from "@/views/system/people/components/SubmitForm.vue";
import {getTableConfig} from "@/views/system/people/config.js";
import {persistentConfig, windowConfig} from "@/layout/layout.js";
import axios from "axios";
import {message} from "@/utils/message.js";
import {isEmpty} from "@/utils/commons.js";
import {routeMenus} from "@/layout/user.js";

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
  userroles:[],
  userMenus: {// 角色拥有的菜单数据
    id: 0,
    menus: []
  },
  rolesMenu:[],
  rolesList:{}
})

const addmessage = () => {
  Object.assign(rootData.formData, {
    id:"",
    name:"",
    password:"",
    email:"",
    roleid:null,
    description:"",
    status:"1",
    rolesid:[]
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

//分配角色
const assignRole=(row)=>{
  rootData.userMenus.menus=row.rolesid
  rootData.userMenus.id=row.id
  rootData.showDialog = true
}

const submitRoles=()=>{
  rootData.submitLoading = true
  request.post("/user/assignRole", {id:rootData.userMenus.id,rolesid:rootData.userMenus.menus}).then(res => {
    message('角色分配成功')
    rootData.showDialog = false
  }).catch(e=>{
    message("角色分配失败",error)
  }).finally(() => {
    rootData.submitLoading = false
  })
}

const gridOptions = reactive({
  rowId: 'id',
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
        const params={
          currentPage:page.currentPage,
          pageSize:page.pageSize,
          queryForm:rootData.queryData
        }
        return new Promise((resolve, reject) => {
          axios.all([
            request.post("/roles/queryAll"),
            request.post("/user/query", params)
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
        const params={
          currentPage:1,
          pageSize:1000000,
          queryForm:rootData.queryData
        }
        return new Promise((resolve, reject) => {
          request.post("/user/query", params).then(res => {
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

.item-tag{
  margin-left: 5px;
}
</style>