<template>
  <el-drawer :title="rootData.name"
             size=40%
             :direction="persistentConfig.drawerPosition"
             v-model="rootData.showForm"
             @close="closeHandler">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <div class="drawer_content">
        <el-form ref="formRef" :model="rootData.formData" :rules="rootData.formRules"
                 size="medium" label-width="130px" label-position="left">
          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="名字:" prop="name">
                <el-input v-model="rootData.formData.name" clearable/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="性别:" prop="sex">
                <el-select v-model="rootData.formData.sex" clearable>
                  <el-option v-for="item in sexList" :key="item.value" :value="item.value" :label="item.label"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="创建时间:" prop="createdate">
                <el-date-picker size="default" v-model="rootData.formData.createdate"
                                style="width: 100%;" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="活动时间:" prop="date_list">
                <el-date-picker
                    v-model="rootData.formData.date_list"
                    type="daterange"
                    value-format="YYYY-MM-DD"
                    range-separator="至"
                    start-placeholder="活动开始日期"
                    end-placeholder="活动结束日期"
                    size="default"
                    style="width: 100%;"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="地址:" prop="address">
                <el-input v-model="rootData.formData.address"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="drawer_footer">
        <el-button @click="resetForm">重置表单</el-button>
        <el-button style="margin-left: 45px" type="primary" native-type="submit"
                   :loading="rootData.submitLoading" @click="submitForm">提交表单
        </el-button>
      </div>
    </el-scrollbar>
  </el-drawer>
</template>

<script setup>

import request from "@/request/index.js";
import {message} from "@/utils/message.js";
import {inject, ref} from "vue";
import {persistentConfig} from "@/layout/layout.js";

const rootData = inject("rootData");
const xGrid = inject("xGrid");

const formRef = ref(null)

const sexList = ref([
  {label: '男', value: 'male'},
  {label: '女', value: 'female'}
])

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      rootData.submitLoading = true
      rootData.formData.startdate=rootData.formData.date_list[0]
      rootData.formData.enddate=rootData.formData.date_list[1]
      if (rootData.selectRow) {
        request.post("/user/update", rootData.formData).then(res => {
          rootData.submitLoading = false
          rootData.showForm = false
          xGrid.value.commitProxy('query')
          message('修改成功')
          console.log(res)
        }).catch(() => {
          rootData.submitLoading = false
          message("修改失败", "error")
        })
      } else {
        request.post("/user/add", rootData.formData).then(res => {
          rootData.submitLoading = false
          rootData.showForm = false
          xGrid.value.commitProxy('query')
          message('添加成功')
          console.log(res)
        }).catch(() => {
          rootData.submitLoading = false
          message("添加失败", "error")
        })
      }
    }
  })
}

const resetForm = () => {
  Object.assign(rootData.formData, {
    name:"",
    sex:"",
    address:"",
    createdate:"",
    startdate:"",
    enddate:"",
    date_list:[],
  })
  formRef.value.resetFields()
}


// 关闭dialog时销毁粘贴事件监听
const closeHandler=()=>{
  formRef.value.resetFields()
}

</script>

<style scoped>

</style>