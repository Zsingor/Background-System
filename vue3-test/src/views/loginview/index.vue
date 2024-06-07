<template>
  <div>
    <input v-model="text"/>
    <button @click="login">登录</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import {createRouteAndMenu} from "@/router/routeUtils.js";
import {persistentConfig} from "@/layout/layout.js";
import request from "@/request/index.js";
import {ref} from "vue";
import {userInfo} from "@/layout/user.js";

const router = useRouter()
var text=ref("")

const login=()=>{
  request.post("/user/login",{name:text.value}).then(res => {
    console.log("res.data",res.data)
    userInfo.baseInfo=res.data
    localStorage.setItem("User_Info", JSON.stringify(res.data)); //存储用户数据
    persistentConfig.routeTags = [];
    createRouteAndMenu(userInfo.baseInfo.menuList)
    router.push("/");
  }).catch(error => {
    console.log(error);
  })

}
</script>

<style scoped>

</style>