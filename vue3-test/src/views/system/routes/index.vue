<template>
  <div class="background">
    <el-card>
      <vxe-grid ref='xGrid' v-bind="gridOptions" v-on="gridEvents" @cell-dblclick="dbclickHandler">
        <template #toolbar_buttons>
          <vxe-button status="primary" @click="addmessage">新增菜单</vxe-button>
          <vxe-button status="danger" @click="deleteTableData(xGrid,'/routes/delete',true)">批量删除</vxe-button>
        </template>
        <template #menuicon="{ row }">
          <el-icon size="large">
            <component :is="row.icon"></component>
          </el-icon>
        </template>
        <template #menupath="{ row }">
          <div v-if="row.level===1">
            <span style="color:#16aad8">{{ row.path }}</span>
          </div>
          <div v-else>
            <span style="color:#67C23A">{{ row.path }}</span>
          </div>
        </template>
        <template #menustatus="{ row }">
          <vxe-switch v-model="row.status"
                      @click="switchUpdateHandler(row)"
                      open-label="启用" close-label="禁用"
                      open-value="1" close-value="0"></vxe-switch>
        </template>
        <template #menulevel="{ row }">
          <div v-if="row.level===1">
            <span style="color:#16aad8">一级菜单</span>
          </div>
          <div v-else>
            <span style="color:#67C23A">二级菜单</span>
          </div>
        </template>
        <template #operate="{ row }">
          <vxe-button title="编辑" circle @click="updaterow(row)">
            <el-icon>
              <Edit/>
            </el-icon>
          </vxe-button>
          <vxe-button title="复制菜单" circle @click="copyMenu(row,$event)">
            <el-icon>
              <CopyDocument/>
            </el-icon>
          </vxe-button>
        </template>
      </vxe-grid>
    </el-card>

    <SubmitForm></SubmitForm>
  </div>
</template>

<script setup>

import {onActivated, onDeactivated, onMounted, provide, reactive, ref, toRaw} from "vue";
import request from "@/request/index.js";
import {dbclickHandler, deleteTableData, resetWatch} from "@/utils/tableconfig.js";
import SubmitForm from "@/views/system/routes/components/SubmitForm.vue";
import {isEmpty} from "@/utils/commons.js";
import _ from "lodash";
import useClipboard from 'vue-clipboard3';
import {message} from "@/utils/message.js";
import {persistentConfig} from "@/layout/layout.js";
const { toClipboard } = useClipboard();

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'routes'
})

const xGrid = ref({})


const formRules = {
  name: [
    {required: true, message: '请输入标识'}
  ],
  title: [
    {required: true, message: '请输入标题'}
  ],
  path: [
    {required: true, message: '请输入路径'}
  ],
}

const rootData = reactive({
  name: "添加路由",
  showForm: false,
  submitLoading: false,
  selectRow: null,
  formData: {
    name: "",
    title: "",
    path: "",
    level: 1,
    status: "1",
    icon: "",
    parentid: "",
    parentMenuPath: ""
  },
  formRules: Object.assign({}, formRules),
  parentMenus: []
})

const addmessage = () => {
  Object.assign(rootData.formData, {
    name: "",
    title: "",
    path: "",
    level: 1,
    position:0,
    status: "1",
    icon: "",
    parentid: "",
    parentMenuPath: ""
  })
  rootData.name = "添加信息"
  rootData.selectRow = null
  rootData.showForm = true
}

//编辑路由
const updaterow = (row) => {
  Object.assign(rootData.formData, row)
  console.log("rootData.formData", rootData.formData)
  rootData.name = "修改信息"
  rootData.selectRow = row
  const parentMenuUrl = Object.assign({}, toRaw(rootData.parentMenus.filter(item => {
    return item.id === row.parentid
  })[0])).path
  if (!isEmpty(parentMenuUrl)) {
    rootData.formData.parentMenuPath = parentMenuUrl
  }
  rootData.showForm = true
}

//复制信息
async function copyMenu(row,event) {
  const cloneRow = _.cloneDeep(toRaw(row))
  await toClipboard(JSON.stringify(cloneRow))
  message("复制成功")
}

const switchUpdateHandler = (row) => {
  console.log("row", row)
}

const gridOptions = reactive({
  rowId: 'id',
  height: 'auto',
  align: 'center',
  showOverflow: true,
  highlightHoverRow: true,
  loading: false,
  toolbarConfig: {
    refresh: true,
    zoom: true,
    custom: true,
    tools: [],
    slots: {
      buttons: 'toolbar_buttons'
    }
  },
  //允许列拖动
  columnConfig: {
    resizable: true
  },
  checkboxConfig: {
    labelField: 'title',
    reserve: true,     // 是否保留勾选状态
    highlight: true    // 高亮选中行
  },
  treeConfig: {
    children: 'children'
  },
  columns: [
    {
      type: 'checkbox',
      title: '菜单名字',
      width: 250,
      treeNode: true,
      align: 'left'
    },
    {
      field: 'path',
      title: '菜单地址',
      minWidth: 150,
      align: 'left',
      slots: {default: 'menupath'}
    },
    {
      field: 'status',
      title: '是否启用',
      width: 100,
      slots: {default: 'menustatus'}
    },
    {
      field: 'icon',
      title: '菜单图标',
      width: 100,
      slots: {default: 'menuicon'}
    },
    {
      field: 'level',
      title: '菜单权重',
      width: 100,
      slots: {default: 'menulevel'}
    },
    {title: '操作', minWidth: 150, fixed: 'right', slots: {default: 'operate'}}
  ],
  proxyConfig: {
    form: true,
    ajax: {
      query: () => {
        return new Promise((resolve, reject) => {
          xGrid.value.clearCheckboxRow()
          request.post("/routes/queryAll").then(res => {
            resolve(res.data)
          }).catch(() => {
            reject()
          })
          request.post("/routes/parents").then(res => {
            rootData.parentMenus = res.data
          })
        })
      }
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

.el-card{
  width: 100%;
  height: 99%;
}
</style>
