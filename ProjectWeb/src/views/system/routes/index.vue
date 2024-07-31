<template>
  <div class="background">
    <el-card>
      <vxe-grid
        ref="xGrid"
        v-bind="gridOptions"
        v-on="gridEvents"
        @cell-dblclick="dbclickHandler"
      >
        <template #toolbar_buttons> 
          <vxe-button status="primary" @click="addmessage">新增菜单</vxe-button>
          <vxe-button
            status="danger"
            @click="deleteTableData(xGrid, '/routes/delete', true)"
            >批量删除</vxe-button
          >
        </template>
        <template #queryType="{ data }">
          <vxe-select
            v-model="data.type"
            transfer
            clearable
            placeholder="菜单类别"
          >
            <vxe-option
              v-for="item in typeList"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            ></vxe-option>
          </vxe-select>
        </template>
        <template #menuicon="{ row }">
          <el-icon size="large">
            <component :is="row.icon"></component>
          </el-icon>
        </template>
        <template #menupath="{ row }">
          <div v-if="row.level === 1">
            <span style="color: #16aad8">{{ row.path }}</span>
          </div>
          <div v-else>
            <span style="color: #67c23a">{{ row.path }}</span>
          </div>
        </template>
        <template #menustatus="{ row }">
          <!--          <vxe-switch v-model="row.status"-->
          <!--                      @click="switchUpdateHandler(row)"-->
          <!--                      open-label="启用" close-label="禁用"-->
          <!--                      open-value="1" close-value="0"></vxe-switch>-->
          <div v-if="row.status === '1'">
            <span style="color: #16aad8">已启用</span>
          </div>
          <div v-else>
            <span style="color: #f56c6c">已禁用</span>
          </div>
        </template>
        <template #menulevel="{ row }">
          <div v-if="row.level === 1">
            <span style="color: #16aad8">一级菜单</span>
          </div>
          <div v-else>
            <span style="color: #67c23a">二级菜单</span>
          </div>
        </template>
        <template #menutype="{ row }">
          <div v-if="row.type === '1'">
            <span style="color: #16aad8">路由</span>
          </div>
          <div v-else>
            <span style="color: #67c23a">接口</span>
          </div>
        </template>
        <template #operate="{ row }">
          <vxe-button title="编辑" circle @click="updaterow(row)">
            <el-icon>
              <Edit />
            </el-icon>
          </vxe-button>
          <vxe-button title="复制菜单" circle @click="copyMenu(row, $event)">
            <el-icon>
              <CopyDocument />
            </el-icon>
          </vxe-button>
        </template>
      </vxe-grid>
    </el-card>

    <SubmitForm></SubmitForm>
  </div>
</template>

<script setup>
import {
  onActivated,
  onDeactivated,
  onMounted,
  provide,
  reactive,
  ref,
  toRaw,
} from 'vue'
import request from '@/request/index.js'
import {
  dbclickHandler,
  deleteTableData,
  resetWatch,
  VxeTableCommonsConfig,
} from '@/utils/VxeTableConfig.js'
import SubmitForm from '@/views/system/routes/components/SubmitForm.vue'
import { isEmpty } from '@/utils/commons.js'
import _ from 'lodash'
import useClipboard from 'vue-clipboard3'
import { message } from '@/utils/message.js'
import { persistentConfig } from '@/layout/layout.js'
import axios from 'axios'
const { toClipboard } = useClipboard()

//定义界面的name，用于使用keep-alive
defineOptions({
  name: 'routes',
})

const xGrid = ref({})

const formRules = {
  name: [{ required: true, message: '请输入标识' }],
  title: [{ required: true, message: '请输入标题' }],
  path: [{ required: true, message: '请输入路径' }],
}

const rootData = reactive({
  name: '添加路由',
  showForm: false,
  submitLoading: false,
  selectRow: null,
  formData: {},
  formRules: Object.assign({}, formRules),
  parentMenus: [],
})

const typeList = ref([
  { label: '路由', value: '1' },
  { label: '接口', value: '2' },
])

const addmessage = () => {
  const tempObj = {
    name: '',
    title: '',
    path: '',
    level: 1,
    position: 0,
    status: '1',
    icon: '',
    parentid: '',
    type: '1',
  }
  rootData.formData = JSON.parse(JSON.stringify(tempObj))
  rootData.name = '添加信息'
  rootData.selectRow = null
  rootData.showForm = true
}

//编辑路由
const updaterow = (row) => {
  Object.assign(rootData.formData, row)
  rootData.name = '修改信息'
  rootData.selectRow = _.cloneDeep(row)
  const parentMenuUrl = Object.assign(
    {},
    toRaw(
      rootData.parentMenus.filter((item) => {
        return item.id === row.parentid
      })[0]
    )
  ).path
  if (!isEmpty(parentMenuUrl)) {
    rootData.formData.parentMenuPath = parentMenuUrl
  }
  rootData.showForm = true
}

//复制信息
async function copyMenu(row, event) {
  const cloneRow = _.cloneDeep(toRaw(row))
  await toClipboard(JSON.stringify(cloneRow))
  message('复制成功')
}

const switchUpdateHandler = (row) => {
  console.log('row', row)
}

const gridOptions = reactive({
  id: 'routes',
  rowId: 'id',
  ...VxeTableCommonsConfig,
  // height: 'auto',
  // align: 'center',
  // showOverflow: true,
  // highlightHoverRow: true,
  // loading: false,
  // toolbarConfig: {
  //   refresh: true,
  //   zoom: true,
  //   custom: true,
  //   tools: [],
  //   slots: {
  //     buttons: 'toolbar_buttons',
  //   },
  // },
  // //允许列拖动
  // columnConfig: {
  //   resizable: true,
  // },
  checkboxConfig: {
    labelField: 'title',
    reserve: true, // 是否保留勾选状态
    highlight: true, // 高亮选中行
  },
  treeConfig: {
    children: 'children',
  },
  formConfig: {
    items: [
      {
        field: 'title',
        span: 6,
        itemRender: {
          name: '$input',
          props: { placeholder: '菜单名称', clearable: true },
        },
      },
      {
        field: 'type',
        span: 6,
        itemRender: {},
        slots: { default: 'queryType' },
      },
      {
        field: 'creatorId',
        span: 6,
        itemRender: {
          name: '$input',
          props: { placeholder: '创建者ID', clearable: true },
        },
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
                status: 'primary',
              },
            },
            {
              props: {
                type: 'reset',
                content: '重置',
              },
            },
          ],
        },
      },
    ],
  },
  columns: [
    {
      type: 'checkbox',
      title: '菜单名称',
      width: 250,
      treeNode: true,
      align: 'left',
    },
    {
      field: 'path',
      title: '菜单地址',
      minWidth: 150,
      align: 'left',
      slots: { default: 'menupath' },
    },
    {
      field: 'status',
      title: '是否启用',
      width: 100,
      slots: { default: 'menustatus' },
    },
    {
      field: 'icon',
      title: '菜单图标',
      width: 100,
      slots: { default: 'menuicon' },
    },
    {
      field: 'level',
      title: '菜单级别',
      width: 100,
      slots: { default: 'menulevel' },
    },
    {
      field: 'type',
      title: '菜单类别',
      width: 100,
      slots: { default: 'menutype' },
    },
    {
      field: 'position',
      title: '菜单权重',
      width: 100,
    },
    {
      field: 'createTime',
      title: '创建时间',
      formatter: 'formatDate',
      minWidth: 180,
    },
    {
      field: 'creatorId',
      title: '创建者ID',
      visible: false,
      minWidth: 200,
    },
    {
      title: '操作',
      minWidth: 150,
      fixed: 'right',
      slots: { default: 'operate' },
    },
  ],
  proxyConfig: {
    form: true,
    ajax: {
      query: ({ form,page }) => {
        return new Promise((resolve, reject) => {
          const params={
            currentPage:page.currentPage,
            pageSize:page.pageSize,
            queryForm:{
              "name": form.name,
              "type": form.type,
              "creatorId": form.creatorId,
            }
          }
          axios
            .all([
              request.post('/routes/queryAll', params),
              request.post('/routes/parents'),
            ])
            .then(
              axios.spread((res1, res2) => {
                xGrid.value.clearCheckboxRow()
                //如果有菜单名字搜索，展开树
                if (!isEmpty(form.title)) {
                  xGrid.value.setAllTreeExpand(true)
                }
                //resolve(res1.data)
                rootData.parentMenus = res2.data
                resolve({
                  page: {
                    total: res1.data.rowSum,
                  },
                  result: res1.data.resultList,
                })
              })
            )
            .catch(() => {
              reject()
            })
        })
      },
    },
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

.el-card {
  width: 100%;
  height: 99%;
}
</style>
