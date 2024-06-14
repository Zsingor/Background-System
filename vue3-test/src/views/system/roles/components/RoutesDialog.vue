<template>
  <el-dialog title="分配菜单"
             :width="windowConfig.dialogWidth"
             v-model="rootData.showDialog"
             :before-close="handlerClose">
    <el-tree
        ref="treeRef"
        :data="rootData.menuDatas"
        show-checkbox
        node-key="id"
        :expand-on-click-node="false"
        check-on-click-node
        accordion
        :props="defaultProps">
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span>{{ data.title }}</span>
        </span>
      </template>
    </el-tree>
    <template #footer>
        <span class="dialog-footer">
          <el-button :loading="rootData.submitLoading" type="primary" @click="submit">更新菜单</el-button>
        </span>
    </template>
  </el-dialog>
</template>

<script setup>
import {inject, nextTick, ref, unref, watch} from "vue";
import request from "@/request/index.js";
import {message} from "@/utils/message.js";
import {windowConfig} from "@/layout/layout.js";

const rootData=inject("rootData")
const treeRef = ref(null)

// 关闭编辑dialog时清除数据
const handlerClose=(done)=>{
  done()
}

const defaultProps={
  children: 'children',
  title: 'label'
}

const submit=()=>{
  rootData.submitLoading = true
  const ids = []
  unref(treeRef).getCheckedNodes(false, true).forEach(item => {
    ids.push(item.id)
  })
  //const routesid=ids.join(',')
  request.post("/roles/assignRoute", {id:rootData.rolesMenus.id,routesid:ids}).then(res => {
    message('菜单分配成功')
    rootData.showDialog = false
  }).catch(e=>{
    message("菜单分配失败",error)
  }).finally(() => {
    rootData.submitLoading = false
  })
}

// 当打开dialog时手动挂载用户菜单，关闭则清除
watch(() => rootData.showDialog, value => {
  // 等dom渲染完成后再进行操作，否则拿不到ref
  nextTick(() => {
    if (value) {
      unref(treeRef).setCheckedKeys(rootData.rolesMenus.menus)
    } else {
      rootData.rolesMenus.menus = []
      unref(treeRef).setCheckedKeys([])
    }
  })
})
</script>

<style scoped>

</style>