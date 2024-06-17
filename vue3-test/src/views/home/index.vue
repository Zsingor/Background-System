<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <!-- 首页user信息 -->
          <el-card shadow= 'always'>
            <div class="userCard">
              <el-avatar :size="150" :src=imgUrl></el-avatar>
              <div class="userInfo">
                <p class="important-font">Admin</p>
                <p class="secondary-font">管理员</p>
              </div>
            </div>
<!--            <div class="login-info">-->
<!--              <p>上次登录时间: 2022/07/06 18:16</p>-->
<!--            </div>-->
          </el-card>
          <!-- 首页表格 -->
          <el-card shadow= 'always' class="tableInfo">
            <div slot="header">
              <span class="important-font">客户信息</span>
            </div>
            <div>
              <el-table
                  :data="tableData"
                  stripe
                  border
                  height="450px"
                  style="width: 100%">
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
      </el-col>
      <el-col :span="16">
        <!-- 六个订单信息 -->
        <div class="num">
          <el-card shadow= 'hover' v-for="item in countData" :key="item.name" :body-style="{ display: 'flex',padding: 0 }" class="OrderCard">
            <el-icon class="icon" :style="{ background: item.color}"><SuccessFilled /></el-icon>
            <div class="OrderCard-item">
              <p class="important-font">￥{{item.value}}</p>
              <p class="secondary-font">{{item.name}}</p>
            </div>
          </el-card>
        </div>
        <!-- 柱状图 -->
        <el-card style="height: 280px">
          <div style="height:280px;" id="barEcharts" ref="barEcharts"></div>
        </el-card>
        <div class= "num graph">
          <el-card style="width: 34%;height: 265px;marginRight: 1%">
            <div style="width: 100%;height: 265px;" id="pieEcharts" ref="pieEcharts"></div>
          </el-card>
          <el-card style="width:65%;height: 265px"><div style="height: 265px"><el-calendar class="calendar" v-model="value"></el-calendar></div></el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>

import {inject, onMounted, ref} from "vue";

defineOptions({
  name: 'people'
})

const echarts = inject('echarts');

const value=new Date()

const tableData=ref([{
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
}])

const countData=ref([
  {
    name: '今日支付订单',
    value: 1200,
    icon: 'success',
    color: '#2ec7c9'
  },
  {
    name: '今日收藏订单',
    value: 1200,
    icon: 'star-on',
    color: '#ffb980'
  },
  {
    name: '今日取消订单',
    value: 1200,
    icon: 's-goods',
    color: '#5ab1ef'
  },
  {
    name: '今日退款订单',
    value: 1200,
    icon: 'success',
    color: '#2ec7c9'
  },
  {
    name: '本月支付订单',
    value: 1200,
    icon: 'star-on',
    color: '#ffb980'
  },
  {
    name: '本月退款订单',
    value: 1200,
    icon: 's-goods',
    color: '#5ab1ef'
  }
])


//柱状图
const initBarEcharts=()=>{
  let dom = document.getElementById('barEcharts');
  const myChart = echarts.init(dom);
  let option = {
    title: {
      text: '销售表'
    },
    tooltip: {},
    legend: {
      data: ['今日销量','昨日销量']
    },
    xAxis: {
      data: ['华为', 'vivo', 'oppo', 'ipone', '小米', '三星']
    },
    yAxis: {},
    series: [
      {
        name: '今日销量',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20]
      },
      {
        name: '昨日销量',
        type: 'bar',
        data: [10, 18, 34, 8, 12, 21]
      }
    ]
  }
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
}

//饼图
const initPieEcharts=()=>{
  let dom = document.getElementById('pieEcharts');
  const myChart = echarts.init(dom);
  let option= {
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
          { value: 1048, name: '成交订单量' },
          { value: 735, name: '退款订单量' },
          { value: 580, name: '浏览量' },
          { value: 484, name: '加购量' },
          { value: 300, name: '预购量' }
        ]
      }
    ]
  }
  myChart.setOption(option);
}


onMounted(()=>{
  initPieEcharts()
  initBarEcharts()
})
</script>

<style scoped>
.userCard{
  height: 180px;
  display: flex;
  border-bottom: 2px solid #DCDFE6;
  border-radius: 2px;
}
.userInfo{
  width: auto;
  padding: 6% 0 0 6%;
}
.important-font{
  font-weight: 900;
  font-size: 25px;
}
.secondary-font{
  color: #909399;
}
.login-info{
  height: 40px;
  text-align: left;
  color: #909399;
}
.tableInfo{
  margin: 20px 0 0 0;
}
.OrderCard{
  margin: 0 0 30px 30px;
  border: #DCDFE6 1px solid;
  border-radius: 10px;
  height: 100px;
  .icon{
    width: 30%;
    height:100%;
    line-height: 120px;
    font-size: 30px;
    color:#fff
  }
  .OrderCard-item{
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    width: 300px;
  }
}

.num{
  display: flex;
  flex-wrap: wrap;
}
.graph{
  margin: 15px 0 0 0;
}
.calendar{
  height: 265px ;
}
</style>