<template>
  <div class="background">
    <div class="bg-card">
      <div class="content-left">
        <div class="switch" id="switch-cnt" ref="switchCtn">
          <div class="left_container" id="switch-c1" ref="switchC1">
            <h2 class="left_title">Hello Friend!</h2>
            <p class="left_description">{{$t('login.welcome')}}</p>
          </div>
        </div>
      </div>
      <div class="content-right">
        <div class="top_icon">
          <el-icon @click="translate" size="20px" color="#4C5D6E">
            <IconRiTranslate/>
          </el-icon>
        </div>
        <div style="width: 100%;text-align: center;">
          <div style="font-size: 30px;width:100%;font-weight: bold;text-align: center;margin-bottom: 40px;color: #4C5D6E">{{$t('login.login')}}</div>
          <div style="margin-left: 20%;width: 60%">
            <el-form :model="user" :rules="rules" ref="loginRef">
              <el-form-item prop="name">
                <el-input clearable size="large" :placeholder="$t('login.username')" v-model="user.name"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input clearable size="large" show-password type="password" :placeholder="$t('login.password')" v-model="user.password"></el-input>
              </el-form-item>
              <el-form-item prop="validCode">
                <div style="width: 100%;display: flex;">
                  <el-input clearable size="large" style="width: 80%;" :placeholder="$t('login.validcode')" v-model="user.validCode"></el-input>
                  <div style="left: 0;background-color: #F56C6C">
                    <Identify ref="identifyRef" @identifyCode="getCode" />
                  </div>
                </div>
              </el-form-item>
            </el-form>
          </div>
          <div style="width:100%;text-align: center;margin-bottom: 15px">
            <button class="button-login" @click="login">{{ $t('login.sure') }}</button>
          </div>
          <div>
            {{ $t('login.tips') }}<span style="color: #409eff;cursor: pointer" @click="$router.push('/register')">{{ $t('login.apply') }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import {createRouteAndMenu} from "@/router/routeUtils.js";
import {persistentConfig} from "@/layout/layout.js";
import request from "@/request/index.js";
import {onMounted, onUnmounted, reactive, ref} from "vue";
import {userInfo} from "@/layout/user.js";
import {message} from "@/utils/message.js";
import websocket from "@/utils/WebSocket.js";
import Identify from "@/components/Identify.vue";
import {encodeData} from "@/utils/rsa.js";
import _ from 'lodash'

const router = useRouter()
const identifyRef=ref(null)

const loginRef = ref(null);
var user=reactive({
  name:"",
  password:"",
  validCode:""
})

let identifyCode = ref("");

const getCode=(data)=>{
  identifyCode.value=data
}

const rules = ref({
  name:[
    {required:true,message:"请输入用户名",trigger:"blur"}
  ],
  password:[
    {required:true,message:"请输入密码",trigger:"blur"}
  ],
  validCode: [
    {required:true,message:"请输入验证码",trigger:"blur" }
  ]
})

//切换中英文
const translate=()=>{
  persistentConfig.localeLang=persistentConfig.localeLang==='zhCn'?'en':'zhCn'
}


const login=()=>{
  loginRef.value.validate((valid) => {
    if (valid) {
      if(identifyCode.value.toLowerCase()===user.validCode.toLowerCase())
      {
        // user.password=encodeData(user.password)
        let newUser=_.cloneDeep(user)
        newUser.password=encodeData(newUser.password)
        request.post("/user/login",newUser).then(res => {
          if(res.code===1)
          {
            userInfo.baseInfo=res.data
            localStorage.setItem("User_Info", JSON.stringify(res.data)); //存储用户数据
            persistentConfig.routeTags = [];
            createRouteAndMenu(userInfo.baseInfo.menuList)
            websocket.Init(userInfo.baseInfo.user_id)
            message("登录成功")
            router.push("/");
          }
          else {
            message(res.msg,"error")
          }
        }).catch(error => {
          message(error,"error")
        })
      }
      else
      {
        message("验证码错误","error")
        identifyRef.value.refreshCode()
        user.validCode=""
      }
    }
  })
}


//点击回车键登录
const keyDown = (e) => {
  if (e.keyCode === 13) {
    login()
  }
}

onMounted(()=>{
  //绑定监听事件
  window.addEventListener('keydown', keyDown)
})

onUnmounted(()=>{
  //销毁事件
  window.removeEventListener('keydown', keyDown, false)
})
</script>

<style scoped>
.background{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #F0EFF2;
}

.bg-card{
  width: 70%;
  height: 80%;
  display: flex;
  border-radius: 10px;
  background-color: #ffffff;
}

.content-left{
  width: 50%;
  height: 100%;
  margin: 0;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  background-color: #3f8eca;
  overflow: hidden;
  box-shadow: 4px 4px 10px #d1dfe6;
}

.left_container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 50px 55px;
}

.left_title {
  font-size: 40px;
  font-weight: 700;
  line-height: 3;
  color: #ffffff;
  letter-spacing: 0.5px;
}

.left_description {
  font-size: 16px;
  line-height: 1.6;
  color: #ffffff;
  letter-spacing: 0.25px;
  text-align: center;
}

.content-right{
  position: relative;
  width: 50%;
  height: 100%;
  margin: 0;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}

.top_icon{
  position: absolute;
  top: 20px;
  right: 30px;
  cursor: pointer;
}

.button-login{
  width: 30%;
  height:50px;
  border-radius: 25px;
  margin-top: 5%;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1.15px;
  background-color: #4B70E2;
  color: #f9f9f9;
  box-shadow: 4px 4px 8px #d1d9e6;
  border: none;
  outline: none;
}

.button-login{
  cursor: pointer;
}

.button-login:hover {
  box-shadow: 6px 6px 10px #d1d9e6, -3px -3px 5px #f9f9f9;
  transform: scale(0.985);
  transition: 0.25s;
}
</style>
