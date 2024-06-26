<template>
  <div class="background">
    <div class="background-left">
      <el-card shadow="always" class="user-card">
        <div class="user-div">
          <el-image class="userAvatar" :src="getImageUrl('avatar.png')" :fit="'cover'"/>
          <div class="userInfo">
            <p class="important-font">{{ user.name }}</p>
            <p class="secondary-font">{{ user.description }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="table-card">
        <div slot="header">
          <span class="important-font">系统通知</span>
        </div>
        <div class="card-table" v-if="!isEmpty(notifications)">
          <div v-for="(item,index) in notifications" :key="index" class="card-item">
            <el-card shadow="hover">
              <div class="card-top">
                <div class="card-top-left">
                  {{item.title}}
                </div>
                <div class="card-top-right">
                  {{parseDate(item.createTime)}}
                </div>
              </div>
              <div class="card-bottom">{{item.content}}</div>
            </el-card>
          </div>
        </div>
        <div v-else class="visitor-card">
          暂无通知
        </div>
      </el-card>
    </div>
    <div class="background-right">
      <div class="background-rightTop">
        <el-card style="width: 34%;height: 100%;margin-right: 1%">
          <div style="width: 100%;height: 100%;" id="pieEcharts" ref="pieEcharts"></div>
        </el-card>
      </div>
      <div class="background-rightBottom">
        <el-card style="width:100%;height: 100%">
          <div style="height: 100%">
            <el-calendar class="calendar" v-model="dateValue"></el-calendar>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>

import {inject, onMounted, reactive, ref} from "vue";
import {userInfo} from "@/layout/user.js";
import {isEmpty, parseDate} from "@/utils/commons.js";
import request from "@/request/index.js";
import {getImageUrl} from "@/utils/resource.js";

defineOptions({
  name: 'people'
})

var myChart

//判断用户是否登录
let loginFlag=ref(false)
//系统通知
let notifications=ref([])

const echarts = inject('echarts');

const dateValue = ref(new Date())
const user = ref({
  name: ""
})

//初始化用户数据
const initUser=()=>{
  if(loginFlag.value)
  {
    user.value.name = userInfo.baseInfo.user_name
    request.post("/user/querymsssage", user.value).then(res => {
      user.value = res.data
    }).catch(error => {
      console.log(error)
    })
  }
  else
  {
    user.value.name = "游客"
    user.value.description = "暂无描述"
  }
}

//初始化系统通知
const initSystemNotice=()=>{
  if(loginFlag.value)
  {
    request.post("/notification/query").then(res => {
      notifications.value = res.data
    }).catch(err=>{

    })
  }
}

//饼图
const initPieEcharts = () => {
  let dom = document.getElementById('pieEcharts');
  myChart = echarts.init(dom);
  let option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '0%',
      left: 'left'
    },
    series: [
      {
        name: '订单信息',
        type: 'pie',
        radius: ['20%', '65%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'left'
        },
        labelLine: {
          show: false,
        },
        data: [
          {value: 1048, name: '成交订单量'},
          {value: 735, name: '退款订单量'},
          {value: 580, name: '浏览量'},
          {value: 484, name: '加购量'},
          {value: 300, name: '预购量'}
        ]
      }
    ]
  }
  myChart.setOption(option);
}


onMounted(() => {
  userInfo.baseInfo = JSON.parse(localStorage.getItem("User_Info"))
  loginFlag.value = !isEmpty(userInfo.baseInfo);

  initUser()
  initSystemNotice()
  initPieEcharts()

  // 在事件触发时，调用图表实例的 resize 方法
  window.addEventListener("resize", function () {
    myChart.resize();
  });
})


</script>

<style scoped>
.background {
  width: 100%;
  height: 100%;
  display: flex;
}

.background-left {
  height: 99%;
  width: 35%;
  float: left;
  margin-right: 1%;
}

.background-right {
  height: 99%;
  width: 73%;
  float: right;
}

.user-card {
  height: 25%;
}

.user-div {
  height: 100%;
  display: flex;
  border-bottom: 2px solid #DCDFE6;
  border-radius: 4px;
}

.important-font {
  font-weight: 900;
  font-size: 25px;
}

.secondary-font {
  color: #909399;
}

.userAvatar {
  width: 30%;
  height: 90%;
  border-radius: 50%;
}

.userInfo {
  width: 70%;
  padding: 6% 0 0 6%;
  overflow: hidden;
}

.table-card {
  margin-top: 3%;
  height: 72%;
}

.card-table {
  margin-top: 20px;
  height: 90%;
  overflow: hidden;
  overflow-y: auto;
}

.card-item{
  margin-bottom: 10px;
  margin-top: 5px;
  margin-left: 2%;
  width: 96%;
  border-bottom: 2px solid #e7eaef;
  cursor: pointer;
}

.card-top{
  margin-bottom: 5px;
  display: flex;
}

.card-top-left{
  overflow: hidden;
  font-weight: bold;
  font-size: 20px;
  color: #28292b;
  width: 70%;
}

.card-top-right{
  overflow: hidden;
  font-weight: lighter;
  font-size: 12px;
  color: #4d5559;
  width: 30%;
}

.card-bottom{
  font-size: 15px;
  color: #343839;
  overflow: hidden;
}

.visitor-card{
  height: 90%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 25px;
  color: #79868f;
}

.background-rightTop {
  width: 100%;
  height: 30%;
}

.background-rightBottom {
  width: 100%;
  margin-top: 1%;
  height: 68%;
}

.calendar {
  height: 200px;
  width: 100%;
}
</style>