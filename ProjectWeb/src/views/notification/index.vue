<template>
  <div class="background">
    <QueryForm v-if="searchFlag" @query="getTableData"></QueryForm>
    <div class="top-button">
      <div class="top-left-button">
        <el-button type="primary" plain :icon="Plus" @click="addNotify">新增</el-button>
        <el-button type="success" plain :icon="Upload" @click="showExport">导出</el-button>
      </div>
      <div class="top-right-button">
        <el-button :icon="Search" circle title="显示搜索" @click="showSearch" />
        <el-button :icon="Refresh" circle title="刷新" @click="getTableData"/>
      </div>
    </div>
    <el-table
        id="elTable"
        v-loading="tableLoading" element-loading-text="数据加载中..."
        ref="tableRef"
        :data="tableData"
        border
        style="width: 100%"
        :row-style="{height:'50px'}"
        :header-cell-style="{backgroundColor:'aliceblue',fontWeight:'blod',color:'#666'}"
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column prop="senderId" label="发送者Id" width="180" align="center"></el-table-column>
      <el-table-column prop="title" label="通知标题" align="center"></el-table-column>
      <el-table-column prop="content" label="通知内容" align="center"></el-table-column>
      <el-table-column prop="createTime" label="发送时间" width="180" align="center"
                       :formatter="formatDate"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          :pager-count="currentPage"
          :total="tableTotal"
          :page-size="pageSize"
          :page-sizes="pageSizes"
          layout="sizes, prev, pager, next, jumper,total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
        :title="dialogTitle"
        draggable
        v-model="dialogVisible"
        @closed="handleClose">
      <el-form ref="addRef" :model="notifyForm" label-width="100px" :rules="formRules">
        <el-form-item label="通知标题:" prop="title">
          <el-input v-model="notifyForm.title"/>
        </el-form-item>
        <el-form-item label="通知内容:" prop="content">
          <el-input v-model="notifyForm.content"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" :loading="dialogLoading" @click="submit()">发 送</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        class="center-dialog"
        :width="'40%'"
        draggable
        :title="dialogTitle"
        v-model="ExportVisible"
        @closed="exportClose">
      <el-form style="width: 70%" ref="exportRef" :model="exportForm" label-width="100px">
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="exportForm.fileName" placeholder="请输入文件名"/>
        </el-form-item>
        <el-form-item label="选择数据" prop="data">
          <el-select
              v-model="exportForm.data"
          >
            <el-option
                v-for="item in dataList"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="选择字段" prop="filter">
          <el-checkbox-group
              style="display: flex;
                        flex-flow: column nowrap;
                        align-items: flex-start;"
              v-model="exportForm.filter"
          >
            <el-checkbox v-for="(item,index) in filterList" :key="index" :label="item.title" :value="item.value">
              {{ item.title }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="ExportVisible = false">取 消</el-button>
          <el-button type="primary" :loading="ExportLoading" @click="exportData()">导 出</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {onMounted, provide, reactive, ref} from "vue";
import {Plus, Refresh, Search, Upload} from "@element-plus/icons";
import request from "@/request/index.js";
import {exportTableData, formatDate} from "@/utils/ElTableConfig.js";
import {parseDate} from "@/utils/commons.js";
import {sendNotification} from "@/request/api/websocket.js";
import {message} from "@/utils/message.js";
import {userInfo} from "@/layout/user.js";
import {ElMessageBox} from "element-plus";
import QueryForm from "@/views/notification/components/QueryForm.vue";
import {getDefaultPageSize, pageSizes} from "@/utils/VxeTableConfig.js";

defineOptions({
  name: 'notification'
})

const tableRef = ref(null)
const addRef = ref(null)
const exportRef = ref(null)

let tableData = ref([])
//加载判断
let tableLoading = ref(false)
//当前页
let currentPage=ref(1)
//显示的订单数
let pageSize=ref(getDefaultPageSize())
//总条目数
let tableTotal=ref()
//是否显示搜索
let searchFlag=ref(true)
//页面参数
const rootData = reactive({
  queryData: {},
})

//新增弹窗配置
let dialogTitle = ref("")
let dialogVisible = ref(false)
let dialogLoading = ref(false)
let notifyForm = reactive({
  senderId: "",
  title: "",
  content: "",
})

//导出弹窗配置
let ExportVisible = ref(false)
let ExportLoading = ref(false)
let exportForm = reactive({
  fileName: "",
  data: 0,
  filter: ["senderId", "title", "content", "createTime"],
})

const formRules = {
  title: [
    {required: true, message: '请输入通知标题'}
  ],
}

//导出数据选择
const dataList = reactive([
  {
    id: 0,
    title: "全部数据"
  },
  {
    id: 1,
    title: "选中数据"
  }
])
//导出字段选择
const filterList = reactive([
  {
    value: "senderId",
    title: "发送者ID"
  },
  {
    value: "title",
    title: "通知标题"
  },
  {
    value: "content",
    title: "通知内容"
  },
  {
    value: "createTime",
    title: "发送时间"
  }
])

//显示搜索
const showSearch=()=>{
  searchFlag.value=!searchFlag.value
}

// 改变每页大小， 根据页码更新dataShow数据(显示的项目)
const handleSizeChange=(val)=>{
  pageSize.value=val
  getTableData()
}

// 点击分页组件会返回页码， 根据页码更新tableData数据(显示的项目)
const handleCurrentChange=(val)=>{
  currentPage.value = val
  getTableData()
}

//获取表格数据
const getTableData = () => {
  tableLoading.value = true
  tableRef.value.clearSelection()
  const params = {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    queryForm: rootData.queryData
  }
  request.post("/notification/query",params).then(res => {
    tableData.value = res.data.resultList
    tableTotal.value=res.data.rowSum
  }).catch(err => {

  }).finally(() => {
    tableLoading.value = false
  })
}

//新增通知
const addNotify = () => {
  dialogTitle.value = "新增通知"
  dialogVisible.value = true
}

//发送通知
const submit = () => {
  dialogLoading.value = true
  addRef.value.validate(async (valid) => {
    if (valid) {
      try {
        notifyForm.senderId = userInfo.baseInfo.user_id
        let res = await sendNotification(notifyForm)
        if (res.code === 1) {
          message("通知发送成功")
          getTableData()
          dialogLoading.value = false
          dialogVisible.value = false
        } else {
          message("通知发送失败", "error")
          dialogLoading.value = false
        }
      } catch (error) {
        dialogLoading.value = false
      }
    }
  })
}

//关闭弹窗
const handleClose = () => {
  Object.assign(notifyForm, {
    senderId: "",
    title: "",
    content: "",
  })
  addRef.value.resetFields()
}

//关闭导出弹窗
const exportClose = () => {
  Object.assign(exportForm, {
    fileName: "",
    data: 0,
    filter: ["senderId", "title", "content", "createTime"],
  })
}

//删除数据
const handleDelete = (row) => {
  ElMessageBox.confirm("您确定要删除吗？", "提示", {
    type: "warning"
  }).then(()=>{
    request.delete("/notification/delete/" + row.id).then(res => {
      if (res.code === 1) {
        getTableData()
        message("删除成功")
      } else {
        message("删除失败", "error")
      }
    }).catch(err => {
      message("删除失败", "error")
    })
  })
}

const showExport = () => {
  dialogTitle.value = "导出设置"
  ExportVisible.value = true
}

//导出数据
const exportData = () => {
  try {
    exportTableData(exportForm,tableRef,filterList,tableData.value,filterTableData)
    message("导出成功")
  }catch(error){
    message("导出失败","error")
  }
  ExportVisible.value = false
}

//导出数据前对数据处理
const filterTableData = (data) => {
  data.forEach(item => {
    // 处理单元数据
    item.createTime = parseDate(item.createTime)
  })
  return data;
}

onMounted(() => {
  getTableData()
})

provide('rootData', rootData)
</script>

<style scoped>
.background {
  width: 100%;
}

.top-button {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.top-right-button {
  position: relative;
  float: right;
}

.pagination{
  margin-top: 10px;
  margin-bottom: 10px;
  display: flex;
  justify-content: center;
}
</style>
