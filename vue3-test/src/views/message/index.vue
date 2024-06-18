<template>
  <div class="background">
    <el-card class="card-background">
      <div class="background-left">
        <div class="background-leftTop">
          <p class="left-title">我的信息</p>
        </div>
        <div class="background-leftBottom">
          <div class="left-item" v-for="(item,index) in listArr" :key="index"
               :class="{'activeCss':activeVar===index }" @click="activeFun(item,index)">
            <p class="left-message">{{item}}</p>
          </div>
        </div>
      </div>
      <div class="background-right">
        <div class="background-rightTop">
          <el-scrollbar>
            <div v-for="(item,index) in msgList">
              <!-- 我方发的信息 -->
              <div class="msg-item" v-if="item.from===userId">
                <div></div>
                <!-- 文字内容 -->
                <div class="msg-content">
                  {{item.message}}
                </div>
              </div>
              <div class="msg-item" v-else>
                <div class="msg-content">
                  {{item.message}}
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
          <input class="inputs" v-model="userMessage.message" @keyup.enter="sendText" />
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
import {handlePaste} from "@/utils/clipboard.js";
import {message} from "@/utils/message.js";
import {sendMessageAll, sendMessageToService} from "@/request/api/websocket.js";
import {isEmpty} from "@/utils/commons.js";
import websocket from "@/utils/WebSocket.js";

const listArr=ref(["全体信息","个人信息"])
let activeVar=ref(0)

let userId=ref("张三")
let userMessage=ref({
  from:"",
  to:"",
  message:""
})


let userList=ref([
  {
    id:"root",
    name:"root",
  },
  {
    id:"test",
    name:"test",
  },
])

let msgList=ref([
  {
    id:1,
    from:"张三",
    to:"李四",
    message:"你好，李四，我是张三，很高兴认识你jabdgufabfh汇报给i圣彼得堡撕逼看比赛本刊表示的法国队克里夫第三笔！",
    isall:"0",
    creatdate:"2023-05-06 12:00:00",
  },
  {
    id:1,
    username:"张三",
    from:"李四",
    to:"张三",
    message:"你好，李四，我是张三，很高兴认识你jabdgufabfh汇报给i圣彼得堡撕逼看比赛本刊表示的法国队克里夫第三笔！",
    isall:"0",
    creatdate:"2023-05-06 12:00:00",
  },
])

const activeFun=(item,index)=>{
  activeVar.value=index
}

const getMessageCallback = (message) => {
  let obj={
    id:1,
    from:"张三",
    to:"李四",
    message:message,
    isall:"0",
    creatdate:"2023-05-06 12:00:00",
  }
  msgList.value.push(obj)
}

// 发送消息
const sendText = async () => {
  const username = userMessage.value.to
  const message = userMessage.value.message
  userMessage.value.message=""

  if(isEmpty(username))
  {
    await sendMessageAll(message)
  }
  else
  {
    // 调用发送消息的接口
    await sendMessageToService({ username, message })
  }
}

onMounted(()=>{
  websocket.setMessageCallback(getMessageCallback)
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
  width: 100%;
  height: 10%;
  display: flex;
  justify-content: center;
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

.left-message{
  font-size: 20px;
  letter-spacing: 5px;
  color: #4C5D6E;
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
