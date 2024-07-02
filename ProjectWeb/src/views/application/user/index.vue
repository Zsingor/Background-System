<template>
  <div class="background">
    <div class="top-button">
      <div class="top-left-button">
        <el-button type="primary" plain :icon="Plus" @click="agree(true)">批量同意</el-button>
        <el-button type="danger" plain :icon="Minus" @click="reject(true)">批量拒绝</el-button>
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
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column prop="name" label="用户名" width="180" align="center"></el-table-column>
      <el-table-column prop="password" label="密码" align="center"></el-table-column>
      <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
      <el-table-column prop="description" label="账号描述" align="center"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" align="center"
                       :formatter="formatDate"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" type="success" @click="agree(false,scope.row)">通过</el-button>
          <el-button size="small" type="danger" @click="reject(false,scope.row)">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {Minus, Plus, Refresh, Search, Upload} from "@element-plus/icons";
import {formatDate} from "@/utils/ElTableConfig.js";
import {onMounted, ref} from "vue";
import request from "@/request/index.js";
import {isEmpty} from "@/utils/commons.js";
import {message} from "@/utils/message.js";
import {ElMessageBox} from "element-plus";

defineOptions({
  name: 'applicationUser'
})

const tableRef = ref(null)

let tableData = ref([])
//加载判断
let tableLoading = ref(false)

//获取表格数据
const getTableData = () => {
  tableLoading.value = true
  tableRef.value.clearSelection()
  request.post("/user/applicationquery").then(res => {
    tableData.value = res.data
  }).catch(err => {

  }).finally(() => {
    tableLoading.value = false
  })
}

//批量同意
const agree=(flag,row)=>{
  const ids = [];
  if(flag)
  {
    let datas=tableRef.value.getSelectionRows()
    if(isEmpty(datas))
    {
      message("请先选择数据","warning")
      return
    }
    datas.forEach(item => {
      ids.push(item["id"]);
    });
  }
  else
  {
    ids.push(row.id)
  }
  tableLoading.value = true
  request.post("/user/agree",ids).then(res=>{
    if (res.code === 1) {
      message("操作成功");
      getTableData()
    }
    else {
      message(res.msg,"error")
    }
  }).finally(() => {
    tableLoading.value = false
  })
}

//批量拒绝
const reject=(flag,row)=>{
  ElMessageBox.confirm("您确定要删除吗？", "提示", {
    type: "warning"
  }).then(()=>{
    const ids = [];
    if(flag)
    {
      let datas=tableRef.value.getSelectionRows()
      if(isEmpty(datas))
      {
        message("请先选择数据","warning")
        return
      }
      datas.forEach(item => {
        ids.push(item["id"]);
      });
    }
    else
    {
      ids.push(row.id)
    }
    tableLoading.value = true
    request.post("/user/delete",ids).then(res=>{
      if (res.code === 1) {
        message("操作成功");
        getTableData()
      }
      else {
        message(res.msg,"error")
      }
    }).finally(() => {
      tableLoading.value = false
    })
  })
}

onMounted(()=>{
  getTableData()
})
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