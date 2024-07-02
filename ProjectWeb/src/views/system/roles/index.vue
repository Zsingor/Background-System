<template>
  <div class="background">
    <vxe-grid ref='xGrid' v-bind="gridOptions" @cell-dblclick="dbclickHandler">
      <template #toolbar_buttons>
        <vxe-button status="primary" @click="addmessage">新增角色</vxe-button>
      </template>
      <!--  操作按钮组    -->
      <template #operate="{ row }">
        <vxe-button title="编辑" circle @click="updaterow(row)">
          <el-icon>
            <Edit/>
          </el-icon>
        </vxe-button>
        <vxe-button title="删除" circle @click="deleteTableData(xGrid,'/roles/delete',false,row)">
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
              <el-dropdown-item @click="assignRoute(row,'1')">
                <el-button type="text">
                  分配路由
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item @click="assignRoute(row,'2')">
                <el-button type="text">
                  分配权限
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </vxe-grid>

    <vxe-modal v-model="rootData.showForm" :title="rootData.selectRow ? '编辑&保存' : '新增&保存'" width="800" min-width="600" min-height="300" :loading="rootData.submitLoading" resize destroy-on-close>
      <template #default>
        <vxe-form :data="rootData.formData" :rules="rootData.formRules" title-align="right" title-width="100" @submit="submitEvent">
          <vxe-form-item title="基础信息" title-align="left" :title-width="200" :span="24" :title-prefix="{icon: 'vxe-icon-comment'}"></vxe-form-item>
          <vxe-form-item field="name" title="角色名称" :span="12" :item-render="{}">
            <template #default="{ data }">
              <vxe-input v-model="data.name" placeholder="请输入角色名称"></vxe-input>
            </template>
          </vxe-form-item>
          <vxe-form-item field="description" title="角色描述" :span="12" :item-render="{}">
            <template #default="{ data }">
              <vxe-input v-model="data.description" placeholder="请输入角色描述"></vxe-input>
            </template>
          </vxe-form-item>
          <vxe-form-item align="center" title-align="left" :span="24">
            <template #default>
              <vxe-button type="submit">提交</vxe-button>
              <vxe-button type="reset">重置</vxe-button>
            </template>
          </vxe-form-item>
        </vxe-form>
      </template>
    </vxe-modal>

    <RoutesDialog></RoutesDialog>
  </div>
</template>

<script setup>

import {dbclickHandler, deleteTableData, resetWatch, VxeTableCommonsConfig} from "@/utils/VxeTableConfig.js";
import {onActivated, onDeactivated, onMounted, provide, reactive, ref, toRaw} from "vue";
import {isEmpty} from "@/utils/commons.js";
import _ from "lodash";
import {message} from "@/utils/message.js";
import request from "@/request/index.js";
import {persistentConfig} from "@/layout/layout.js";
import {ElMessageBox} from "element-plus";
import axios from "axios";
import RoutesDialog from "@/views/system/roles/components/RoutesDialog.vue";

defineOptions({
  name: 'roles'
})

const xGrid = ref({})


const formRules = {
  name: [
    {required: true, message: '请输入角色名称'}
  ],
  description: [
    {required: true, message: '请输入角色描述'}
  ],
}

const rootData = reactive({
  showForm: false,
  showDialog:false,
  submitLoading: false,
  selectRow: null,
  formData: {
    id:0,
    name: "",
    description:"",
  },
  formRules: Object.assign({}, formRules),
  menuDatas: [],
  routesType:"1",
  rolesMenus: {// 角色拥有的菜单数据
    id: 0,
    menus: []
  },
})

const addmessage=()=>{
  Object.assign(rootData.formData, {
    id:"",
    name: "",
    description:"",
  })
  rootData.selectRow = null
  rootData.showForm = true
}

const updaterow=(row)=>{
  Object.assign(rootData.formData, row)
  rootData.selectRow = row
  rootData.showForm = true
}

//分配路由
const assignRoute=(row,type)=>{
  rootData.routesType=type
  rootData.rolesMenus.id = row.id
  let queryForm={type:rootData.routesType}
  // 发送两个请求：1.获取所有菜单  2.获取角色拥有的菜单
  axios.all([
    request.post('/routes/queryAll',queryForm),
    request.post("/routes/query", row)
  ]).then(axios.spread((menus, userMenus) => {
    rootData.menuDatas = []
    rootData.menuDatas = menus.data
    if (!isEmpty(userMenus.data)) {
      userMenus.data.forEach(item => {
        if (isEmpty(item.children)) {
          rootData.rolesMenus.menus.push(item.id)
        } else {
          item.children.forEach(child => {
            rootData.rolesMenus.menus.push(child.id)
          })
        }
      })
    }
  })).finally(() => {
    rootData.showDialog = true
  })
}

const submitEvent=()=>{
  rootData.submitLoading = true
  if (rootData.selectRow) {
    console.log(rootData.formData)
    request.post("/roles/update", rootData.formData).then(res => {
      rootData.showForm = false
      xGrid.value.commitProxy('query')
      message('修改成功')
      console.log(res)
    }).catch(() => {
      message("修改失败", "error")
    }).finally(() => {
      rootData.submitLoading = false
    })
  } else {
    request.post("/roles/add", rootData.formData).then(res => {
      rootData.showForm = false
      xGrid.value.commitProxy('query')
      message('添加成功')
      console.log(res)
    }).catch(() => {
      message("添加失败", "error")
    }).finally(() => {
      rootData.submitLoading = false
    })
  }
}

const gridOptions = reactive({
  rowId: 'id',
  ...VxeTableCommonsConfig,
  formConfig: {
    items: [
      {
        field: 'name',
        span: 6,
        itemRender: { name: '$input', props: { placeholder: '角色名称', clearable: true } }
      },
      {
        field: 'description',
        span: 6,
        itemRender: { name: '$input', props: { placeholder: '角色描述', clearable: true } }
      },
      {
        span: 6,
        align: 'left',
        itemRender: {
          name: '$buttons',
          children: [
            {
              props: {
                type: 'submit',
                content: '搜索',
                status: 'primary'
              }
            },
            {
              props: {
                type: 'reset',
                content: '重置'
              }
            }
          ]
        }
      }
    ]
  },
  columns: [
    {type: 'checkbox', width: 50, fixed: 'left'},
    {type: 'seq', width: 50},
    {field: 'name', title: '角色名称',minWidth:120},
    {field: 'description', title: '角色描述',minWidth:120},
    {title: '操作', minWidth: 140, fixed: 'right', slots: {default: 'operate'}}
  ],
  proxyConfig: {
    form: true,
    ajax: {
      query: ({form,page}) => {
        xGrid.value.clearCheckboxRow()
        return new Promise((resolve, reject) => {
          const params={
            currentPage:page.currentPage,
            pageSize:page.pageSize,
            queryForm:{
              "name": form.name,
              "description": form.description,
            }
          }
          console.log(page)
          request.post("/roles/query", params).then(res => {
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
        })
      },
    }
  },
})


provide('xGrid', xGrid)
provide('rootData', rootData)


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