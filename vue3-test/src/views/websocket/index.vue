<template>
  <div>
    <!-- 输入框和按钮 -->
    <el-input v-model="inputUserId" placeholder="输入用户ID"></el-input>
    <el-input v-model="inputMessage" placeholder="输入消息"></el-input>
    <el-button type="primary" @click="sendMessage">发送消息</el-button>
  </div>
  <hr />
  <div>
    <div>收到的消息: {{ receivedMessage }}</div>
  </div>
  <hr />
  <div>
    <el-button type="primary" @click="getUserList">获取在线用户列表</el-button>
    <div class="userList">
      <ul>
        <li v-for="item in userList" :key="item.index">
          {{ item }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import {getUserService, sendMessageAll, sendMessageToService} from "@/request/api/websocket.js";
import websocket from "@/utils/WebSocket.js";
import {isEmpty} from "@/utils/commons.js";

// 收到的消息
const receivedMessage = ref('')
// 输入框中的消息
const inputMessage = ref('')
// 输入框中的用户ID
const inputUserId = ref('')
// 在线用户
const userList = ref([])

const getMessageCallback = (message) => {
  receivedMessage.value = message
}

// 在组件挂载时设置消息处理回调
onMounted(() => {
  websocket.setMessageCallback(getMessageCallback)
})

// 在组件销毁前取消消息处理回调
onBeforeUnmount(() => {
  websocket.setMessageCallback(null)
})

// 获取在线用户
const getUserList = async () => {
  const res = await getUserService()
  userList.value = res.data
}

// 发送消息
const sendMessage = async () => {
  const username = inputUserId.value
  const message = inputMessage.value

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
</script>

<style scoped>
.userList {
  background-color: pink;
}
</style>