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
          <span class="important-font">客户信息</span>
        </div>
        <div class="card-table">
          <el-table
              :data="tableData"
              stripe
              border
              style="width: 100%;height: 100%">
            <el-table-column
                prop="date"
                label="日期"
                width="120">
            </el-table-column>
            <el-table-column
                prop="name"
                label="姓名"
                width="80">
            </el-table-column>
            <el-table-column
                prop="address"
                label="地址">
            </el-table-column>
          </el-table>
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

import {inject, onMounted, ref} from "vue";
import {userInfo} from "@/layout/user.js";
import {isEmpty} from "@/utils/commons.js";
import request from "@/request/index.js";
import {message} from "@/utils/message.js";
import {getImageUrl} from "@/utils/resource.js";

defineOptions({
  name: 'people'
})

const echarts = inject('echarts');

const dateValue = ref(new Date())
const user = ref({
  name: ""
})

const isLogin = () => {
  userInfo.baseInfo = JSON.parse(localStorage.getItem("User_Info"))
  if (!isEmpty(userInfo.baseInfo)) {
    user.name = userInfo.baseInfo.user_name
    request.post("/user/querymsssage", user).then(res => {
      user.value = res.data
      console.log(user.value)
    }).catch(error => {
      message(error, "error")
    })
    return true
  } else {
    user.value.name = "游客"
    user.value.description = "暂无描述"
    return false
  }
}

const tableData = ref([{
  date: '2016-05-03',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-02',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-04',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-01',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-08',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}, {
  date: '2016-05-06',
  name: '王小虎',
  address: '上海市普陀区金沙江路 1518 弄'
}
])

//饼图
const initPieEcharts = () => {
  let dom = document.getElementById('pieEcharts');
  const myChart = echarts.init(dom);
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
  isLogin()
  initPieEcharts()
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


.userCard {
  height: 180px;
  display: flex;
  border-bottom: 2px solid #DCDFE6;
  border-radius: 2px;
}


.tableInfo {
  margin: 20px 0 0 0;
}

.num {
  display: flex;
  flex-wrap: wrap;
}

.graph {
  margin: 15px 0 0 0;
}

.calendar {
  height: 200px;
  width: 100%;
}

</style>