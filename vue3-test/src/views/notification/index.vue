<template>
  <div class="background">
    <div class="top-button">
      <div class="top-left-button">
        <el-button type="primary" plain :icon="Plus" @click="addNotify">新增</el-button>
        <el-button type="success" plain :icon="Upload" @click="exportData">导出</el-button>
      </div>
      <div class="top-right-button">
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
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="senderId" label="发送者Id" width="180" align="center"></el-table-column>
      <el-table-column prop="title" label="通知标题" align="center"></el-table-column>
      <el-table-column prop="content" label="通知内容" align="center"></el-table-column>
      <el-table-column prop="createTime" label="发送时间" width="180" align="center" :formatter="formatDate"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      :before-close="handleClose">
    <el-form ref="addForm" :model="notifyForm" label-width="100px" :rules="formRules">
      <el-form-item label="通知标题:" prop="title">
        <el-input v-model="notifyForm.title" />
      </el-form-item>
      <el-form-item label="通知内容:" prop="content">
        <el-input v-model="notifyForm.content" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="dialogLoading" @click="submit()">发 送</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>

import {onMounted, reactive, ref} from "vue";
import {Plus, Refresh, Upload} from "@element-plus/icons";
import request from "@/request/index.js";
import {formatDate} from "@/utils/ElTableConfig.js";
import * as XLSX from 'xlsx';
import {parseDate} from "@/utils/commons.js";
import {sendNotification} from "@/request/api/websocket.js";
import _ from "lodash";
import {message} from "@/utils/message.js";
import {userInfo} from "@/layout/user.js";

const tableRef=ref(null)

let tableData=ref([])
//加载判断
let tableLoading=ref(false)
const addForm=ref(null)

let dialogTitle=ref("")
let dialogVisible=ref(false)
let dialogLoading=ref(false)
let notifyForm=reactive({
  senderId:"",
  title:"",
  content:"",
})

const formRules = {
  title: [
    {required: true, message: '请输入通知标题'}
  ],
}

//获取表格数据
const getTableData=()=>{
  tableLoading.value=true
  tableRef.value.clearSelection()
  request.post("/notification/query").then(res => {
    tableData.value = res.data
  }).catch(err=>{

  }).finally(()=>{
    tableLoading.value=false
  })
}

//新增通知
const addNotify=()=>{
  dialogTitle.value="新增通知"
  dialogVisible.value=true
}

//发送通知
const submit=()=>{
  dialogLoading.value=true
  addForm.value.validate(async (valid) => {
    if (valid) {
      try {
        notifyForm.senderId=userInfo.baseInfo.user_id
        let res = await sendNotification(notifyForm)
        if(res.code===1)
        {
          message("通知发送成功")
          getTableData()
          dialogLoading.value=false
          dialogVisible.value=false
        }
        else
        {
          message("通知发送失败","error")
          dialogLoading.value=false
        }
      } catch (error) {
        dialogLoading.value=false
      }
    }
  })
}


//关闭弹窗
const handleClose=()=>{
  Object.assign(rootData.formData, {
    senderId:"",
    title:"",
    content:"",
  })
  addForm.value.resetFields()
}

//删除数据
const handleDelete=(row)=>{
  request.delete("/notification/delete/"+row.id).then(res=>{
    if (res.code===1)
    {
      getTableData()
      message("删除成功")
    }
    else
    {
      message("删除失败","error")
    }
  }).catch(err=>{
    message("删除失败","error")
  })
}

//导出数据
const exportData=()=>{
  const header_zh = {
    senderId: '发送者Id',
    title: '通知标题',
    content: '通知内容',
    createTime: '发送时间',
  }
  const filterVal = ['senderId', 'title', 'content', 'createTime'];  // 跟表格表头对应的绑定的prop
  const list = filterTableData(JSON.parse(JSON.stringify(tableRef.value.getSelectionRows())))
  let data = formatJson(filterVal, list);
  const filename = `表格-${Date.now()}.xlsx`;
  const arrayWithHeader = [header_zh, ...data];
  const ws = XLSX.utils.json_to_sheet(
      arrayWithHeader,
      {
        header: filterVal,
        skipHeader: true
      }
  )
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
  XLSX.writeFile(wb, filename);
  tableRef.value.clearSelection()
}

//处理数据使之只导出固定列
const formatJson=(filterVal, Data)=>{
  return Data.map(item => {
    // 获取所有在filterVal中的键值对
    const filteredEntries = Object.entries(item).filter(([key]) => filterVal.includes(key));
    // 使用Object.fromEntries将键值对数组转换回对象
    return Object.fromEntries(filteredEntries);
  })
}

//导出数据前对数据处理
const filterTableData=(data)=>{
  data.forEach(item => {
    // 处理单元数据
    item.createTime=parseDate(item.createTime)
  })
  return data;
}

onMounted(()=>{
  getTableData()
})
</script>

<style scoped>
.background{
  width: 100%;
}

.top-button{
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.top-right-button{
  position: relative;
  float: right;
}
</style>