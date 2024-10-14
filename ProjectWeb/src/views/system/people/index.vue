<template>
  <div class="background">
    <vxe-grid
      ref="xGrid"
      v-bind="gridOptions"
      @cell-dblclick="dbclickHandler"
      class="table1"
    >
      <!--  表格上方的查询表单    -->
      <template #form>
        <query-form />
      </template>
      <!--  表格上方的按钮组    -->
      <template #toolbar_buttons>
        <vxe-button status="primary" @click="addmessage">新增</vxe-button>
        <vxe-button
          status="danger"
          @click="deleteTableData(xGrid, '/user/delete', true)"
          >批量删除</vxe-button
        >
      </template>
      <!--  所属角色显示显示    -->
      <template #role="{ row }">
        <div>
          <el-tag
            :style="{ whiteSpace: 'pre-wrap' }"
            class="item-tag"
            v-for="(roleid, index) in row.rolesid"
            :key="index"
          >
            {{ ' ' + rootData.rolesList[roleid] + ' ' }}
          </el-tag>
        </div>
      </template>
      <!--  状态显示    -->
      <template #status="{ row }">
        <div v-if="row.status === '1'">
          <h4 style="color: #67c23a">已启用</h4>
        </div>
        <div v-else>
          <h4 style="color: #f56c6c">未启用</h4>
        </div>
      </template>
      <!--  操作按钮组    -->
      <template #operate="{ row }">
        <el-button type="primary" @click="updaterow(row)">编辑</el-button>
        <el-button
          type="danger"
          @click="deleteTableData(xGrid, '/user/delete', false, row)"
          >删除</el-button
        >
        <el-dropdown trigger="click" style="margin-left: 10px">
          <el-button type="info">更多</el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="assignRole(row)">
                <el-button type="primary" text> 分配角色 </el-button>
              </el-dropdown-item>
              <el-dropdown-item @click="updatepwd(row)">
                <el-button type="primary" text> 修改密码 </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </vxe-grid>

    <el-dialog title="分配角色" width="30%" v-model="rootData.showDialog">
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
          <el-button
            :loading="rootData.submitLoading"
            type="primary"
            @click="submitRoles"
          >
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="showUpdateDia" draggable title="修改密码" width="30%" @open="openHandler">
      <el-form
        ref="updateRef"
        :model="updateUser"
        :rules="rules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="密码:" prop="password">
          <el-input v-model="updateUser.password" clearable />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="updateLoading" type="primary" @click="editpwdsure">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <SubmitForm></SubmitForm>
  </div>
</template>

<script setup>
import {
  provide,
  reactive,
  ref,
  onMounted,
  onActivated,
  onDeactivated,
} from 'vue'
import QueryForm from '@/views/system/people/components/QueryForm.vue'
import request from '@/request/index.js'
import {
  VxeTableCommonsConfig,
  dbclickHandler,
  resetWatch,
  deleteTableData,
} from '@/utils/VxeTableConfig.js'
import SubmitForm from '@/views/system/people/components/SubmitForm.vue'
import { getTableConfig } from '@/views/system/people/config.js'
import { persistentConfig, windowConfig } from '@/layout/layout.js'
import axios from 'axios'
import { message } from '@/utils/message.js'
import _ from 'lodash'
import { encodeData } from "@/utils/rsa.js";

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'people',
})

const xGrid = ref()
const tableConfig = getTableConfig()
//更改密码表单
const updateRef = ref(null)

const rules = {
  password: [
    { required: true, message: '请输入密码' },
    { min: 6, max: 30, message: '长度在 6 到 30 个字符' },
  ],
}

const rootData = reactive({
  name: '测试列表',
  //显示编辑界面
  showForm: false,
  //显示分配弹窗
  showDialog: false,
  //提交加载
  submitLoading: false,
  //用于判断是新增还是修改
  selectRow: null,
  //提交表单的信息
  formData: {},
  //查询表单的信息
  queryData: {},
  //表单校验规则
  formRules: Object.assign({}, tableConfig.formRules),
  // 角色拥有的菜单数据
  userMenus: {
    id: 0,
    menus: [],
  },
  //角色列表
  rolesMenu: [],
  //存储角色id和名称的键值对
  rolesList: {},
})

//显示修改密码窗口
let showUpdateDia = ref(false)
let updateUser = ref({
  id: '',
  password: '',
})
let updateLoading=ref(false)

const addmessage = () => {
  const tempObj = {
    id: '',
    name: '',
    password: '',
    email: '',
    description: '',
    status: '1',
    rolesid: [],
  }
  rootData.formData = JSON.parse(JSON.stringify(tempObj))
  rootData.name = '添加信息'
  rootData.selectRow = null
  rootData.showForm = true
}

const updaterow = (row) => {
  Object.assign(rootData.formData, row)
  rootData.name = '修改信息'
  rootData.selectRow = _.cloneDeep(row)
  rootData.showForm = true
}

//分配角色
const assignRole = (row) => {
  rootData.userMenus.menus = row.rolesid
  rootData.userMenus.id = row.id
  rootData.showDialog = true
}

//显示修改密码弹窗
const updatepwd = (row) => {
  const tempObj = {
    id: row.id,
    password: '',
  }
  updateUser.value=JSON.parse(JSON.stringify(tempObj))
  showUpdateDia.value = true
  
}

const openHandler=()=>{
  updateRef.value.resetFields()
}

//修改密码
const editpwdsure=()=>{
  updateRef.value.validate((valid)=>{
    if(valid){
      updateLoading.value=true
      let newUser = {};
      newUser.id = updateUser.value.id;
      newUser.password = updateUser.value.password;
      newUser.password = encodeData(newUser.password);
      request.post('/user/updateUserPwd',newUser).then((res)=>{
        message("密码修改成功")
        showUpdateDia.value = false
      }).catch((e)=>{

      }).finally(()=>{
        updateLoading.value=false
      })
    }
  })
}

const submitRoles = () => {
  rootData.submitLoading = true
  request
    .post('/user/assignRole', {
      id: rootData.userMenus.id,
      rolesid: rootData.userMenus.menus,
    })
    .then((res) => {
      message('角色分配成功')
      rootData.showDialog = false
      xGrid.value.commitProxy('query')
    })
    .catch((e) => {
      message('角色分配失败', error)
    })
    .finally(() => {
      rootData.submitLoading = false
    })
}

const gridOptions = reactive({
  id: 'people',
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
      total: 'page.total', // 配置响应结果总页数字段
    },
    // 只接收Promise，具体实现自由发挥
    ajax: {
      query: ({ page }) => {
        xGrid.value.clearCheckboxRow()
        const params = {
          currentPage: page.currentPage,
          pageSize: page.pageSize,
          queryForm: rootData.queryData,
        }
        return new Promise((resolve, reject) => {
          axios
            .all([
              request.post('/roles/queryAll'),
              request.post('/user/query', params),
            ])
            .then(
              axios.spread((res1, res2) => {
                //处理全部角色的数据
                rootData.rolesMenu = res1.data
                res1.data.forEach((item) => {
                  rootData.rolesList[item.id] = item.name
                })
                //处理表格数据
                resolve({
                  page: {
                    total: res2.data.rowSum,
                  },
                  result: res2.data.resultList,
                })
              })
            )
            .catch(() => {
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
          queryForm: rootData.queryData,
        }
        return new Promise((resolve, reject) => {
          request
            .post('/user/query', params)
            .then((res) => {
              resolve(res.data.resultList)
            })
            .catch(() => {
              reject()
            })
        })
      },
    },
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

.item-tag {
  margin-left: 5px;
}
</style>
