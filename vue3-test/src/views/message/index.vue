<template>
  <div class="background">
    <el-card class="card-background">
      <div class="background-left">
        <div class="background-leftTop">
          <p class="left-title">我的信息</p>
        </div>
        <div class="background-leftBottom">
          <div class="left-item" v-for="(item,index) in userList" :key="index"
               :class="{'activeCss':activeVar===index }" @click="activeFun(item,index)">
            <div class="user-content">
              <p class="user-title">{{item.name}}</p>
              <p class="user-description">{{item.description}}</p>
            </div>
          </div>
        </div>
      </div>
      <div class="background-right">
        <div class="background-rightTop">
          <el-scrollbar>
            <div v-for="(item,index) in msgList">
              <!-- 我方发的信息 -->
              <div class="msg-item" v-if="item.senderId===userId">
                <div></div>
                <!-- 文字内容 -->
                <div class="msg-content">
                  {{item.content}}
                </div>
              </div>
              <div class="msg-item" v-else>
                <div class="msg-content">
                  {{item.content}}
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
        <div class="background-rightBottom">
          <el-select
              size="large"
              v-model="userMessage.to"
              placeholder="请选择接收人"
              style="width: 15%">
            <el-option
                v-for="item in userList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
          <input class="inputs" v-model="userMessage.content" @keyup.enter="sendText" />
          <div class="send boxinput" @click="sendText">
            <el-icon style="width: 100%;font-size: 25px"><TopRight /></el-icon>
          </div>
        </div>
      </div>
    </el-card>

  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {message, notification} from "@/utils/message.js";
import {getAllUser, getMessage, sendMessageAll, sendMessageToService} from "@/request/api/websocket.js";
import {isEmpty} from "@/utils/commons.js";
import websocket from "@/utils/WebSocket.js";
import {userInfo} from "@/layout/user.js";
import {ElNotification} from "element-plus";
import {persistentConfig} from "@/layout/layout.js";

let activeVar=ref(-1)

let userId=userInfo.baseInfo.user_id
let userMessage=ref({
  senderId:userInfo.baseInfo.user_id,
  receiverId:"",
  content:""
})


let userList=ref([])

let msgList=ref([])

const activeFun=async (item, index) => {
  activeVar.value = index
  userMessage.value.receiverId = item.id
  const res2 = await getMessage(userMessage.value.senderId, userMessage.value.receiverId)
  msgList.value = res2.data
}

const getMessageCallback = async (content) => {
  //notification("您有新的消息", content, 0)
  ElNotification({
    title: "您有新的消息",
    message: content,
    duration:0,
    position: persistentConfig.notiPosition,
  })
  const res2 = await getMessage(userMessage.value.senderId, userMessage.value.receiverId)
  msgList.value = res2.data
}

// 发送消息
const sendText = async () => {
  let res
  if(!isEmpty(userMessage.value.receiverId))
  {
    console.log(userMessage.value)
    // 调用发送消息的接口
    res=await sendMessageToService(userMessage.value)
  }
  else
  {
    message("未选择接收者","error")
    //res=await sendMessageAll(userMessage.value)
  }

  if(res.code===1)
  {
    userMessage.value.content=""
    const res2 = await getMessage(userMessage.value.senderId, userMessage.value.receiverId)
    msgList.value = res2.data
  }
  else
  {
    message("消息发送失败","error")
  }

}

onMounted(async () => {
  websocket.setMessageCallback(getMessageCallback)
  const res = await getAllUser()
  userList.value=res.data
})
</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.card-background{
  width: 97%;
  height: 97%;
  border-radius: 1%;
  display: flex;
  background-color: #F0F0F1;
}

.background-left {
  height: 99%;
  width: 24%;
  margin-right: 1%;
  float: left;
  border-right: 2px solid #DCDFE6;
  background-color: #F0F0F1;
}

.background-leftTop{
  width: 100%;
  height: 8%;
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 2px solid #DCDFE6;
  margin-bottom: 1%;
}

.left-title{
  font-size: 25px;
  font-weight: bold;
  letter-spacing: 5px;
  color: #4C5D6E;
}

.background-leftBottom{
  width: 100%;
  height: 91%;
}

.left-item{
  padding-left: 10%;
  width: 100%;
  height: 10%;
  display: flex;
  align-items: center;
}

.left-item{
  cursor: pointer;
}

.left-item:hover {
  background-color: #E4E4E6;
}

.activeCss {
  background-color: #D1DEF0;
}

.user-content{
  width: 100%;
  overflow: hidden;
}

.user-title{
  width: 100%;
  font-size: 20px;
  letter-spacing: 3px;
  color: #4C5D6E;
}

.user-description{
  font-size: 12px;
  letter-spacing: 2px;
  color: #7e92a5;
}

.background-right {
  height: 99%;
  width: 75%;
  float: right;
  background-color: #F6F6F7;
  border-radius: 2%;
  padding: 1%;
}

.background-rightTop{
  width: 100%;
  height: 85%;
  padding-top: 1%;
}

.msg-item{
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
}

.msg-content{
  position: relative;
  max-width: 60%;
  word-wrap: break-word;
  padding: 1%;
  border-radius: 8px;
  font-size: 18px;
  font-weight: 500;
  color: #333333;
  line-height: 25px;
  background-color: #E8E8E9;
}

.background-rightBottom{
  width: 100%;
  height: 15%;
  background-color: white;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}
/*
.input_select :deep(.el-input__inner)
{
  height: 200px;
}*/

.inputs {
  width: 70%;
  height: 50px;
  background-color: #FFFFFFFF;
  border-radius: 15px;
  border: 2px solid rgb(34, 135, 225);
  padding: 10px;
  box-sizing: border-box;
  transition: 0.2s;
  font-size: 20px;
  color: #151515;
  font-weight: 100;
  margin: 0 20px;
  &:focus {
    outline: none;
  }
}

.boxinput {
  width: 50px;
  height: 50px;
  background-color: #505567;
  border-radius: 15px;
  border: 1px solid #505567;
  position: relative;
  cursor: pointer;
}

.send {
  background-color: #D1DEF0;
  border: 0;
  transition: 0.3s;
  display: flex;
  align-items: center;
  box-shadow: 0px 0px 5px 0px #0A88F6;
  &:hover {
    box-shadow: 0px 0px 10px 0px #0A88F6;
  }
}
</style>
