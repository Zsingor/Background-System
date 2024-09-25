<template>
  <div class="background">
    <el-card class="card-background">
      <div class="background-left">
        <div class="background-leftTop">
          <el-select
              v-model="selectUser"
              filterable
              clearable
              placeholder="搜索用户"
              style="width: 70%"
              @change="chooseUser"
          >
            <el-option
                v-for="item in allList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </div>
        <div class="background-leftBottom">
          <el-scrollbar>
            <div class="left-item" v-for="(item,index) in userList" :key="index"
                 :class="{'activeCss':activeVar===index }" @click="activeFun(index)">
              <div class="user-content">
                <div class="content-left">
                  <p class="user-title">{{item.name}}</p>
                  <p class="user-description">{{item.description}}</p>
                </div>
                <div class="content-right">
                  <el-badge v-if="item.unreadCount>0" :value="item.unreadCount" :max="99"></el-badge>
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
      </div>
      <div class="background-right">
        <div class="background-rightTop">
          {{receiverName}}
        </div>
        <div class="background-rightMid">
          <el-scrollbar ref="scrollbarRef" class="scroller">
            <div ref="innerRef">
              <div v-for="(item,index) in msgList" :key="index">
                <!-- 我方发的信息 -->
                <div class="msg-item" v-if="item.senderId===userId">
                  <div class="msg-item-line">
                    <div></div>
                    <div class="msg-item-time">
                      {{parseDate(item.createTime)}}
                    </div>
                  </div>
                  <!-- 文字内容 -->
                  <div class="msg-item-line">
                    <div></div>
                    <div class="msg-content">
                      {{item.content}}
                    </div>
                  </div>
                </div>
                <!-- 对方发的信息 -->
                <div class="msg-item" v-else>
                  <div class="msg-item-line">
                    <div class="msg-item-time">
                      {{parseDate(item.createTime)}}
                    </div>
                    <div></div>
                  </div>
                  <div class="msg-item-line">
                    <div class="msg-content">
                      {{item.content}}
                    </div>
                    <div></div>
                  </div>
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
        <div class="background-rightBottom">
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
import {nextTick, onMounted, ref, watch} from "vue";
import {message, notification} from "@/utils/message.js";
import {
  getAllUser,
  getConversation,
  getMessage,
  sendMessageToService
} from "@/request/api/websocket.js";
import {isEmpty, parseDate} from "@/utils/commons.js";
import websocket from "@/utils/WebSocket.js";
import { userInfo} from "@/layout/user.js";
import {useRoute} from "vue-router";
import _ from "lodash"
import request from "@/request/index.js";

defineOptions({
  name: 'message'
})

const route=useRoute()

const scrollbarRef = ref() // 滚动条实例
const innerRef = ref() // 内容实例

let activeVar=ref(-1)
let userId=userInfo.baseInfo.user_id
let receiverName=ref("")
let userMessage=ref({
  senderId:userInfo.baseInfo.user_id,
  senderName:userInfo.baseInfo.user_name,
  receiverId:"",
  content:"",
  createTime:0,
})

//拥有的用户列表
let userList=ref([])
//全部的用户列表
let allList=ref([])
//消息列表
let msgList=ref([])
//搜索的用户
let selectUser=ref()

const chooseUser=async () => {
  if (isEmpty(selectUser.value)) {
    const res1 = await getConversation(userInfo.baseInfo.user_id)
    userList.value = res1.data
  } else {
    let lis=[]
    lis.push(allList.value.find(user => user.id === selectUser.value))
    userList.value=lis;
  }
}

//滚动条滑动到最底部
const scrollerToBottom=()=>{
  nextTick(() => {
    scrollbarRef.value.setScrollTop(innerRef.value.clientHeight)
  })
}

const activeFun=async (index) => {
  activeVar.value = index
  receiverName.value=userList.value[index].name
  userMessage.value.receiverId = userList.value[index].id
  const res2 = await getMessage(userMessage.value.senderId, userMessage.value.receiverId)
  msgList.value = res2.data
  request.post("/conversations/updateUnreadCount",
      {userId:userInfo.baseInfo.user_id,contactId:userList.value[index].id}).then(res=>
  {
    userInfo.unread_count-=userList.value[index].unreadCount
    userList.value[index].unreadCount=0
  })
  scrollerToBottom()
}

const getMessageCallback = async (message) => {
  if(activeVar.value!==-1)
  {
    msgList.value.push(message)
    //若用户在当前的消息列表中，消去未读数
    request.post("/conversations/updateUnreadCount",
        {userId:userInfo.baseInfo.user_id,contactId:userList.value[activeVar.value].id}).then(res=>{

    })
    scrollerToBottom()
  }
}

// 发送消息
const sendText = async () => {
  try {
    let res
    if(!isEmpty(userMessage.value.receiverId))
    {
      // 调用发送消息的接口
      res=await sendMessageToService(userMessage.value)
    }
    else
    {
      message("未选择接收者","error")
    }
    if(res.code===1)
    {
      userMessage.value.createTime=Date.now()
      let sendMessage=_.cloneDeep(userMessage.value)
      msgList.value.push(sendMessage)
      userMessage.value.content=""
      scrollerToBottom()
    }
    else
    {
      message("消息发送失败","error")
    }
  } catch (error) {
    message("消息发送失败","error")
  }
}

onMounted(async () => {
  //给接收信息设置回调函数
  websocket.setMessageCallback(getMessageCallback)

  //获得全部的用户列表
  const res = await getAllUser()
  allList.value=res.data

  //获得朋友列表
  const res1=await getConversation(userInfo.baseInfo.user_id)
  userList.value=res1.data

  let query=route.query
  if(!_.isEmpty(query))
  {
    const index = userList.value.findIndex(user => user.id === query.senderId);
    await activeFun(index)
  }
})
</script>

<style scoped lang="scss">
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

.background-leftBottom{
  width: 100%;
  height: 91%;
}

.left-item{
  padding-left: 10%;
  width: 97%;
  height: 80px;
  display: flex;
  align-items: center;
  border-bottom: 2px solid #e1e4ea;
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
  display: flex;
  flex-wrap: wrap;
  overflow: hidden;
}

.content-left{
  width: 90%;
  overflow: hidden;
}

.content-right{
  width: 10%;
  display: flex;
  justify-content: center;
  align-items: center;
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
  padding: 0 1% 1% 1%;
}

.background-rightTop{
  width: 100%;
  height: 8%;
  border-bottom: 2px solid #DCDFE6;
  margin-bottom: 1%;
  display: flex;
  align-items: center;
  font-size: 25px;
  letter-spacing: 2px;
}

.background-rightMid{
  width: 100%;
  height: 76%;
}

.scroller{
  padding-right: 10px;
}

.msg-item{
  margin-bottom: 15px;
}

.msg-item-line{
  display: flex;
  justify-content: space-between;
}

.msg-item-time{
  font-size: 14px;
}

.msg-content{
  max-width: 60%;
  text-align: left;
  word-wrap: break-word;
  padding: 10px;
  border-radius: 8px;
  font-size: 18px;
  font-weight: 500;
  color: #333333;
  line-height: 25px;
  background-color: #E8E8E9;
}

.background-rightBottom{
  width: 99%;
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
  width: 80%;
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
