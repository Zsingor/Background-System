<template>
  <div class="background">
    <div class="bg-card">
      <div class="content-left">
        <div class="switch" id="switch-cnt" ref="switchCtn">
          <div class="left_container" id="switch-c1" ref="switchC1">
            <h2 class="left_title">Hello Friend!</h2>
            <p class="left_description">{{$t('register.welcome')}}</p>
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
          <div style="font-size: 30px;width:100%;font-weight: bold;text-align: center;margin-bottom: 40px;color: #4C5D6E">{{$t('register.apply')}}</div>
          <div style="margin-left: 20%;width: 60%">
            <el-form :model="user" :rules="rules" ref="registerRef">
              <el-form-item prop="name">
                <el-input clearable size="large" :placeholder="$t('register.username')" v-model="user.name"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input clearable size="large" show-password type="password" :placeholder="$t('register.password')" v-model="user.password"></el-input>
              </el-form-item>
              <el-form-item prop="checkPassword">
                <el-input clearable size="large" show-password type="password" :placeholder="$t('register.validpwd')" v-model="user.checkPassword"></el-input>
              </el-form-item>
              <el-form-item prop="email">
                <el-input clearable size="large" :placeholder="$t('register.email')" v-model="user.email"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div style="width:100%;text-align: center;margin-bottom: 15px">
            <button class="button-login" @click="register">{{$t('register.sure')}}</button>
          </div>
          <div>
            {{$t('register.tips')}}<span style="color: #409eff;cursor: pointer" @click="$router.push('/login')">{{$t('register.login')}}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

import {onMounted, onUnmounted, reactive, ref} from "vue";
import {persistentConfig} from "@/layout/layout.js";
import request from "@/request/index.js";
import {message} from "@/utils/message.js";
import {isEmpty} from "@/utils/commons.js";

const registerRef=ref(null)

const checkUser = (rule, value, callback) => {
  if (value.length<2 || value.length>15) {
    callback(new Error('用户名长度为2-15位'));
  } else {
    callback();
  }
}
const checkPwd = (rule, value, callback) => {
  if (value.length<6 || value.length>20) {
    callback(new Error('密码长度为6-20位'));
  } else {
    callback();
  }
}
const checkPass = (rule, value, callback) => {
  if (value !== user.password) {
    callback(new Error('两次输入密码不一致'));
  } else {
    callback();
  }
}

const rules = ref({
  name:[
    {required:true,message:"请输入用户名",trigger:"blur"},
    {validator: checkUser, trigger: 'blur'}
  ],
  password:[
    {required:true,message:"请输入密码",trigger:"blur"},
    { validator: checkPwd, trigger: 'blur' }
  ],
  checkPassword:[
    {required:true,message:"请确认密码密码",trigger:"blur"},
    { validator: checkPass, trigger: 'blur' }
  ]
})

var user=reactive({
  name:"",
  password:"",
  checkPassword:"",
  email:"",
})

const translate=()=>{
  persistentConfig.localeLang=persistentConfig.localeLang==='zhCn'?'en':'zhCn'
}

const register=()=>{
  registerRef.value.validate((valid) => {
    if (valid) {
      if(!isEmpty(user.email))
      {
        const regEmail = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
        if (!regEmail.test(user.email))
        {
          message("邮箱输入的格式错误","error")
          return
        }
      }
      request.post("/user/register",user).then(res => {
        if(res.code===1)
        {
          message("申请成功，请等待管理员回复")
          Object.assign(user, {
            name:"",
            password:"",
            checkPassword:"",
            email:"",
          })
          registerRef.value.resetFields()
        }
        else {
          message(res.msg,"error")
        }
      }).catch(error => {
        console.log(error);
      })
    }
  })
}

//点击回车键申请
const keyDown = (e) => {
  if (e.keyCode === 13) {
    register()
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
  margin-top: 50px;
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