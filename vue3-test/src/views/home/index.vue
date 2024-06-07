<template>
  <div>
    首页
    <button @click="setrequest">发送请求</button>
    <button @click="opendialog">打开窗口</button>
    <FullDialog v-model="showdialog"></FullDialog>
  </div>
</template>

<script setup>
import request from "@/request/index.js";
import {ref} from "vue";
import {getExplorerInfo, parseDate} from "@/utils/commons.js";
import FullDialog from "@/views/home/components/FullDialog.vue";


defineOptions({
  name: 'home'
})

const showdialog=ref(false)
const name=ref("user11")

const setrequest = () => {
  // request.get('/routes/get/'+name.value).then(res => {
  //   console.log(res)
  // })
  //     .catch(error => {
  //       console.log(error)
  //     })

  request.post('/routes/all').then(res => {
    console.log(res)
  })
      .catch(error => {
        console.log(error)
      })
}

const opendialog=()=>{
  //showdialog.value=true
  let explorerInfo=getExplorerInfo()
  console.log("explorerInfo",explorerInfo)
}
</script>

<style scoped>

</style>