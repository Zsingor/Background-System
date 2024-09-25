<template>
  <el-drawer :title="rootData.name" size="40%"
             :direction="persistentConfig.drawerPosition"
             v-model="rootData.showForm"
             @open="openHandler"
             @close="closeHandler">
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <div class="drawer_content">
        <el-form ref="formRef" :model="rootData.formData" :rules="rootData.formRules"
                 label-width="120px" label-position="left" status-icon>
          <el-form-item label="菜单标识:" prop="name">
            <el-input size="large" v-model="rootData.formData.name"/>
          </el-form-item>
          <el-form-item label="菜单标题:" prop="title">
            <el-input size="large" v-model="rootData.formData.title"/>
          </el-form-item>
          <el-form-item label="菜单类别:" prop="type">
            <el-select filterable placeholder="选择菜单类别"
                       size="large"
                       v-model="rootData.formData.type">
              <el-option v-for="item in typeList" :key="item.value"
                         :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="菜单级别:">
            <el-input-number size="large" v-model="rootData.formData.level" :min="1" :max="2"/>
          </el-form-item>
          <el-form-item label="父级菜单:" prop="parent_menu_id">
            <el-select clearable filterable placeholder="选择父级菜单"
                       size="large"
                       :disabled="disableSelectParentMenu"
                       v-model="rootData.formData.parentid"
                       @change="parentMenuChangeHandler"
                       @clear="parentMenuClearHandler">
              <el-option v-for="item in rootData.parentMenus" :key="item.id"
                         :label="item.title" :value="item.id">
                <span style="float: left">{{ item.title }}</span>
                <span style="float: right;color:#16aad8" v-if="item.type==='1'">路由</span>
                <span style="float: right;color:#67C23A" v-else>接口</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="菜单url:" prop="path">
            <el-input size="large" v-model="rootData.formData.path">
              <template #prepend>
                {{ rootData.formData.parentMenuPath }}
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="菜单图标:" prop="icon">
            <el-input size="large" v-model="rootData.formData.icon" style="width: 60%" clearable/>
            <el-button size="large" style="margin-left: 8px;margin-right: 8px" @click="showIconDialog=true">选择icon</el-button>
            <el-icon size="large">
              <component :is="rootData.formData.icon"></component>
            </el-icon>
          </el-form-item>
          <el-form-item label="菜单权重:">
            <el-input-number size="large" v-model="rootData.formData.position" :min="0" :max="999"/>
          </el-form-item>
          <el-form-item label="是否启用:" prop="status">
            <el-switch size="large" :active-value="'1'" :inactive-value="'0'"
                       v-model="rootData.formData.status"></el-switch>
          </el-form-item>
        </el-form>
      </div>
      <div class="drawer_footer">
        <el-button size="large" @click="resetForm">重置表单</el-button>
        <el-button size="large" style="margin-left: 45px" type="primary" native-type="submit"
                   :loading="rootData.submitLoading" @click="submitForm">提交表单
        </el-button>
      </div>
    </el-scrollbar>

    <IconDialog v-model="showIconDialog" @setIcon="setIcon"></IconDialog>
  </el-drawer>
</template>

<script setup>

import request from "@/request/index.js";
import {message} from "@/utils/message.js";
import {computed, inject, onMounted, ref, toRaw, watch} from "vue";
import IconDialog from "@/components/icons/IconDialog.vue";
import {ElNotification} from "element-plus";
import _ from "lodash";
import {persistentConfig} from "@/layout/layout.js";
import { userInfo } from "@/layout/user";

const rootData = inject("rootData")
const xGrid = inject("xGrid")

const typeList = ref([
  {label: '路由', value: '1'},
  {label: '接口', value: '2'}
])

const formRef = ref()
const showIconDialog = ref(false)

//设置Icon
const setIcon=(icon)=>{
  rootData.formData.icon = icon
}

//用于判断能否选择父级菜单
const disableSelectParentMenu = computed(() => {
  return rootData.formData.level === 1
})

//提交表格
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if(rootData.formData.parentid)
      {
        const menu = rootData.parentMenus.find(item => item.id === rootData.formData.parentid);
        if(rootData.formData.type!==menu.type)
        {
          message("该菜单类型与父菜单类型不匹配","error")
          return
        }
      }
      rootData.formData.children=[]
      rootData.submitLoading = true
      if (rootData.selectRow){
        request.post("/routes/update", rootData.formData).then(res => {
          if(res.code===1)
          {
            rootData.showForm = false
            xGrid.value.commitProxy('query')
            resetForm()
            message('修改成功')
          }
          else
          {
            message(res.msg,"error")
          }
        }).catch(() => {
          message("修改失败", "error")
        }).finally(() => {
          rootData.submitLoading = false
        })
      }
      else
      {
        rootData.formData.creatorId=userInfo.baseInfo.user_id
        request.post("/routes/add", rootData.formData).then(res => {
          if (res.code === 0) {
            message(res.msg,"error")
          }
          else
          {
            rootData.showForm = false
            xGrid.value.commitProxy('query')
            resetForm()
            message('添加成功')
          }
        }).catch((e) => {
          message("添加失败:"+e, "error")
        }).finally(() => {
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
    parentMenuChangeHandler(rootData.formData.parentid)
  }
  else
  {
    const tempObj={
      name: "",
      title: "",
      path: "",
      level: 1,
      position:0,
      status: "1",
      icon: "",
      parentid: "",
      type:"1",
    }
    rootData.formData=JSON.parse(JSON.stringify(tempObj))
  }
  formRef.value.resetFields()
}

// 当选中的父菜单发生改变修改url上的内容
const parentMenuChangeHandler = (value) => {
  const parentMenu = Object.assign({}, toRaw(rootData.parentMenus.filter(item => {
    return item.id === value
  })[0]))
  rootData.formData.parentMenuPath = parentMenu.path
}

// 处理父菜单清除事件
const parentMenuClearHandler = () => {
  rootData.formData.parentMenuPath = ''
  rootData.formData.parentid = ''
}

// 粘贴事件
const  pasteEvent=async (event) => {
  // 拿到粘贴数据
  const text = event.clipboardData.getData('Text')
  let menuObj
  let flag = true
  try {
    menuObj = JSON.parse(await navigator.clipboard.readText())
  } catch (e) {
    flag = false
  }
  if (flag) {
    if (!_.isUndefined(menuObj.name)) {
      rootData.formData.name = menuObj.name
    }
    if (!_.isUndefined(menuObj.title)) {
      rootData.formData.title = menuObj.title
    }
    if (!_.isUndefined(menuObj.path)) {
      rootData.formData.path = menuObj.path
    }
    if (!_.isUndefined(menuObj.icon)) {
      rootData.formData.icon = menuObj.icon
    }
    if (!_.isUndefined(menuObj.status)) {
      rootData.formData.status = menuObj.status
    }
    if (!_.isUndefined(menuObj.type)) {
      rootData.formData.type = menuObj.type
    }
    if (!_.isUndefined(menuObj.level)) {
      rootData.formData.level = menuObj.level
    }
    // 判断父菜单是否为空,为空不做任何操作
    if (!_.isEmpty(menuObj.parentid)) {
      // 过滤出父菜单对象
      const temp = rootData.parentMenus.filter(item => {
        return item.id === menuObj.parentid
      })
      // 判断是否存在父菜单
      if (_.isArray(temp) && temp.length > 0) {
        rootData.formData.parentid = menuObj.parentid
        rootData.formData.parentMenuPath = temp[0].path
      } else {
        ElNotification({
          title: '提示',
          type: 'warning',
          message: '粘贴的二级菜单所包含的父级菜单ID不存在，父级菜单选项系统将保持默认，请手动选择！',
          duration: 3000,
        });
      }
    }
  }
}

//监听路由的等级变化
watch(() => rootData.formData.level, value => {
  if (value === 1) {
    rootData.formData.parentid = ''
    rootData.formData.parentMenuPath = ''
  }
})

// 打开dialog时开启粘贴事件监听
const openHandler=()=>{
  document.addEventListener('paste', pasteEvent);
  if(!rootData.selectRow)
  {
    formRef.value.resetFields()
  }
}

// 关闭dialog时销毁粘贴事件监听
const closeHandler=()=>{
  document.removeEventListener('paste', pasteEvent)
  formRef.value.resetFields()
}

</script>

<style>

</style>
