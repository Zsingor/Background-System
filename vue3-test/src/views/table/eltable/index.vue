<template>
  <div class="background">
    <QueryForm v-if="searchFlag" @query="getTableData"></QueryForm>
    <div class="top-button">
      <div class="top-left-button">
        <el-button type="primary" plain :icon="Plus">新增</el-button>
        <el-button type="danger" plain :icon="Delete">删除</el-button>
        <el-button type="warning" plain :icon="Edit">修改</el-button>
        <el-button type="success" plain :icon="Upload" @click="exportData">导出</el-button>
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
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="username" label="用户名称" width="180" align="center"></el-table-column>
      <el-table-column prop="module" label="操作模块" width="180" align="center"></el-table-column>
      <el-table-column prop="operate" label="操作内容" width="180" align="center"></el-table-column>
      <el-table-column prop="details" label="详细内容" show-overflow-tooltip align="center"></el-table-column>
      <el-table-column prop="ip" label="IP地址" align="center"></el-table-column>
      <el-table-column prop="operatedate" label="操作时间" align="center" :formatter="formatDate"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleDelete(scope.row)">修改</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          :pager-count="currentPage"
          :page-size="pageSize"
          :page-sizes="pageSizes"
          layout="sizes, prev, pager, next, jumper,total"
          :total="tableTotal"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import {onMounted, provide, reactive, ref} from "vue";
import {Delete, Edit, Plus, Refresh, Search, Upload} from "@element-plus/icons";
import request from "@/request/index.js";
import {formatDate} from "@/utils/ElTableConfig.js";
import {getDefaultPageSize, pageSizes} from "@/utils/VxeTableConfig.js";
import QueryForm from "@/views/table/eltable/components/QueryForm.vue";
import * as XLSX from 'xlsx';
import {parseDate} from "@/utils/commons.js";

defineOptions({
  name: 'eltable'
})

const tableRef=ref(null)

//表格数据
let tableData=ref([])
//当前页
let currentPage=ref(1)
//显示的订单数
let pageSize=ref(getDefaultPageSize())
//总商品数
let tableTotal=ref()
//加载判断
let tableLoading=ref(false)
//是否显示搜索
let searchFlag=ref(true)

const rootData = reactive({
  queryData: {},
})

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

//导出数据
const exportData=()=>{
  const tableHeader = ['用户名称', '操作模块', '操作内容', '详细内容', 'IP地址','操作时间'];   // 设置Excel表格的表头
  const header_zh = {
    username: '用户名称',
    module: '操作模块',
    operate: '操作内容',
    details: '详细内容',
    ip: 'IP地址',
    operatedate: '操作时间',
  }
  const filterVal = ['username', 'module', 'operate', 'details', 'ip', 'operatedate'];  // 跟表格表头对应的绑定的prop
  const list = filterTableData(JSON.parse(JSON.stringify(tableRef.value.getSelectionRows())))
  let data = formatJson(filterVal, list);
  const filename = `表格-${Date.now()}.xlsx`;
  //const ws = XLSX.utils.json_to_sheet(data);
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
    item.operatedate=parseDate(item.operatedate)
  })
  return data;
}

//显示搜索
const showSearch=()=>{
  searchFlag.value=!searchFlag.value
}

const handleDelete=(row)=>{
  console.log(row)
}

//获取表格数据
const getTableData=()=>{
  tableLoading.value=true
  tableRef.value.clearSelection()
  const params={
    currentPage:currentPage.value,
    pageSize:pageSize.value,
    queryForm:rootData.queryData
  }
  request.post("/logs/query", params).then(res=>{
    tableTotal.value=res.data.rowSum
    tableData.value=res.data.resultList
  }).catch(err=>{

  }).finally(()=>{
    tableLoading.value=false
  })
}

onMounted(()=>{
  getTableData()
})

provide('rootData', rootData)
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

.pagination{
  margin-top: 10px;
  margin-bottom: 10px;
  display: flex;
  justify-content: center;
}
</style>