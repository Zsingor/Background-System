<template>
  <el-drawer :title="rootData.name"
             size=40%
             :direction="persistentConfig.drawerPosition"
             v-model="rootData.showForm"
             @open="openHandler"
             @close="closeHandler">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <div class="drawer_content">
        <el-form ref="formRef" :model="rootData.formData" :rules="rootData.formRules"
                 label-width="120px" label-position="left">
          <el-row :gutter="16">
            <el-col :span="18">
              <el-form-item label="用户名:" prop="name">
                <el-input size="large" v-model="rootData.formData.name" clearable/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="密码:" prop="password">
                <el-input size="large" v-model="rootData.formData.password" clearable/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="邮箱:" prop="email">
                <el-input size="large" v-model="rootData.formData.email" clearable/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="描述:" prop="description">
                <el-input type="textarea" rows="4" size="large" v-model="rootData.formData.description" clearable/>
              </el-form-item>
            </el-col>
            <el-col :span="18">
              <el-form-item label="角色分配:" prop="rolesid">
                <el-select
                    clearable filterable placeholder="选择角色"
                    v-model="rootData.formData.rolesid"
                    multiple
                    size="large">
                  <el-option
                      v-for="item in rootData.rolesMenu"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                  >
                    <span style="float: left">{{ item.name }}</span>
                    <span style="float: right">{{ item.description }}</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否启用:" prop="status">
                <el-switch size="large" :active-value="'1'" :inactive-value="'0'"
                           v-model="rootData.formData.status"></el-switch>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div class="drawer_footer">
        <el-button size="large" @click="resetForm">重置表单</el-button>
        <el-button size="large" style="margin-left: 45px" type="primary" native-type="submit"
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

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      rootData.submitLoading = true
      if (rootData.selectRow) {
        request.post("/user/update", rootData.formData).then(res => {
          rootData.showForm = false
          xGrid.value.commitProxy('query')
          message('修改成功')
        }).catch(() => {
          message("修改失败", "error")
        }).finally(()=>{
          rootData.submitLoading = false
        })
      } else {
        rootData.formData.type=1
        request.post("/user/add", rootData.formData).then(res => {
          if(res.code===1)
          {
            rootData.showForm = false
            xGrid.value.commitProxy('query')
            message('添加成功')
          }
          else if(res.code===2)
          {
            message(res.msg,"error")
          }
        }).catch(() => {
          message("添加失败", "error")
        }).finally(()=>{
          rootData.submitLoading = false
        })
      }
    }
  })
}

const resetForm = () => {
  if(rootData.selectRow)
  {
    rootData.formData=JSON.parse(JSON.stringify(rootData.selectRow))
  }
  else
  {
    const tempObj={
      id:"",
      name:"",
      password:"",
      email:"",
      roleid:-1,
      description:"",
      status:"1",
      rolesid:[]
    }
    rootData.formData=JSON.parse(JSON.stringify(tempObj))
  }
  formRef.value.resetFields()
}

// 打开dialog时清除表单校验
const openHandler=()=>{
  if(!rootData.selectRow)
  {
    formRef.value.resetFields()
  }
}

// 关闭dialog时销毁粘贴事件监听
const closeHandler=()=>{
  formRef.value.resetFields()
}

</script>

<style scoped>

</style>
