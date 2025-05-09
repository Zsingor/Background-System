<template>
  <div class="background">
    <div class="background-left">
      <el-card shadow="always" class="user-card">
        <div class="user-div">
          <context-menu
              :menu="[
              { id: 1, label: '下载' },
              { id: 2, label: '获取信息' },
              { id: 3, label: '获取指纹' },
              { id: 4, label:'获取网络状态'}
            ]"
              @select="select"
          >
            <img
                style="width: 200px; height: 140px"
                src="/public/static/work.png"
            />
          </context-menu>
          <div class="userInfo">
            <p class="important-font">{{ user.name }}</p>
            <p class="secondary-font">{{ user.description }}</p>
          </div>
        </div>
      </el-card>
      <el-card class="table-card">
        <template #header>
          <div
              class="card-header"
              style="
              display: flex;
              justify-content: space-between;
              padding: 0;
            "
          >
            <span class="important-font">{{ $t('home.notify') }}</span>
            <el-button
                :icon="RefreshRight"
                circle
                :title="$t('home.notify')"
                @click="initSystemNotice"
            />
          </div>
        </template>

        <div
            v-loading="notifyLoading"
            class="card-table"
            v-if="!isEmpty(notifications)"
        >
          <div
              v-for="(item, index) in notifications"
              :key="index"
              class="card-item"
          >
            <el-card shadow="hover">
              <div class="card-top">
                <div class="card-top-left">
                  {{ item.title }}
                </div>
                <div class="card-top-right">
                  {{ parseDate(item.createTime) }}
                </div>
              </div>
              <div class="card-bottom">{{ item.content }}</div>
            </el-card>
          </div>
        </div>
        <div v-else class="visitor-card">
          {{ $t('home.noNotify') }}
        </div>
      </el-card>
    </div>
    <div class="background-right">
      <div class="background-rightTop">
        <el-card style="width: 32%; height: 100%">
          <CommonChart
              :tooltip="option.tooltip"
              :series="option.series"
              :legend="option.legend"
          ></CommonChart>
        </el-card>

        <el-card style="width: 32%; height: 100%">
          <a href="https://github.com/Zsingor" target="_blank">
            <div class="card-image">
              <div
                  style="
                  width: 100%;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "
              >
                <img
                    style="width: 100px; height: 100px"
                    src="@/assets/github.jpg"
                />
              </div>
              <div
                  style="
                  width: 100%;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "
              >
                <p style="font-size: 20px">GitHub</p>
              </div>
            </div>
          </a>
        </el-card>

        <el-card style="width: 32%; height: 100%">
          <a href="https://cn.vuejs.org/" target="_blank">
            <div class="card-image">
              <div
                  style="
                  width: 100%;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "
              >
                <img
                    style="width: 100px; height: 100px"
                    src="@/assets/vue.png"
                />
              </div>
              <div
                  style="
                  width: 100%;
                  display: flex;
                  justify-content: center;
                  align-items: center;
                "
              >
                <p style="font-size: 20px">Vue</p>
              </div>
            </div>
          </a>
        </el-card>
      </div>
      <div class="background-rightBottom">
        <el-card style="width: 100%; height: 100%">
          <el-scrollbar wrap-class="scrollbar-wrapper">
            <div style="height: 100%">
              <el-calendar class="calendar" v-model="dateValue">
                <template #date-cell="{ data }">
                  <div :class="data.isSelected ? 'is-selected' : ''">
                    <div>
                      {{ data.day.split('-').slice(2).join('') }}
                      <span v-if="restDay(data)" class="rest">{{
                          $t('home.rest')
                        }}</span>
                    </div>
                    <div>
                      {{ data.isSelected ? '✔️' : '' }}
                    </div>
                  </div>
                </template>
              </el-calendar>
            </div>
          </el-scrollbar>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {userInfo} from '@/layout/user.js'
import {
  isEmpty,
  parseDate,
  getExplorerInfo,
  getuuid, doAnimation,
} from '@/utils/commons.js'
import request from '@/request/index.js'
import {downloadLocalTemplate} from '@/utils/resource.js'
import {RefreshRight} from '@element-plus/icons'
import useClipboard from 'vue-clipboard3'
import {message} from '@/utils/message'

const {toClipboard} = useClipboard()

defineOptions({
  name: 'home',
})

//判断用户是否登录
let loginFlag = ref(false)
//系统通知
let notifications = ref([])

//处理通知的加载
let notifyLoading = ref(false)

//时间
const dateValue = ref(new Date())

const user = ref({
  id: '',
  name: '游客',
  description: '暂无描述',
})

//右键菜单选择回调
const select = async (item) => {
  if (item.id === 1) {
    downloadLocalTemplate('/public/static/work.png', 'work.png')
  } else if (item.id === 2) {
    let msg = getExplorerInfo()
    await toClipboard(JSON.stringify(msg))
    message('浏览器信息已复制')
  } else if (item.id === 3) {
    let msg = getuuid()
    await toClipboard(JSON.stringify(msg))
    message('浏览器指纹已复制')
  }else if(item.id === 4){
    let online=navigator.onLine
    let msg=""
    if(!online){
      msg={"网络状态":"离线","带宽":"离线","延迟":"离线"}
    }else{
      let con=navigator.connection
      msg={"网络状态":con.effectiveType,"带宽":`${con.downlink}Mb/s`,"延迟":`${con.rtt}ms`}
    }
    await toClipboard(JSON.stringify(msg))
    message('网络状态已复制')
  }
}

//休息日
const restDay = (data) => {
  let date = new Date(data.day)
  let dayofWeek = date.getDay()
  return dayofWeek % 7 === 0 || dayofWeek % 7 === 6
}

const upload = (e) => {
  console.log('用户数据上传: ' + e.loaded, ' ', e.total)
}

const download = (e) => {
  console.log('用户数据下载: ' + e.loaded, ' ', e.total)
}

//初始化用户数据
const initUser = () => {
  if (loginFlag.value) {
    user.value.id = userInfo.baseInfo.user_id
    request
        .post('/user/querymsssage', user.value, {
          onUploadProgress: upload,
          onDownloadProgress: download,
        })
        .then((res) => {
          user.value = res.data
        })
        .catch((error) => {
          if (!isEmpty(error)) {
            message(error, 'error')
          }
        })
  } else {
    user.value.name = '游客'
    user.value.description = '暂无描述'
  }
}

//初始化系统通知
const initSystemNotice = () => {
  // doAnimation(1000,10,1000,(val)=>{console.log(val)})
  if (loginFlag.value) {
    notifyLoading.value = true
    const params = {
      currentPage: 1,
      pageSize: 20,
      queryForm: {},
    }
    request
        .post('/notification/query', params)
        .then((res) => {
          notifications.value = res.data.resultList
        })
        .catch((err) => {
        })
        .finally(() => {
          notifyLoading.value = false
        })
  }
}

let option = reactive({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {d}%',
  },
  legend: {
    data: ['Vue3', 'Java', 'JavaScript', 'Other'],
    top: '0%',
    left: 'left',
  },
  series: [
    {
      name: '技术栈',
      type: 'pie',
      radius: ['20%', '65%'],
      avoidLabelOverlap: false,
      label: {
        show: false,
        position: 'left',
      },
      labelLine: {
        show: false,
      },
      data: [
        {value: 48.1, name: 'Vue3'},
        {value: 29.9, name: 'Java'},
        {value: 21.6, name: 'JavaScript'},
        {value: 0.4, name: 'Other'},
      ],
    },
  ],
})

onMounted(() => {
  userInfo.baseInfo = JSON.parse(localStorage.getItem('User_Info'))
  loginFlag.value = !isEmpty(userInfo.baseInfo)

  initUser()
  initSystemNotice()
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
  align-items: center;
  border-bottom: 2px solid #dcdfe6;
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
  height: 100%;
  padding: 6% 0 0 6%;
  overflow: hidden;
}

.table-card {
  margin-top: 3%;
  height: 72%;
}

.card-table {
  height: 90%;
  overflow: hidden;
  overflow-y: auto;
}

.card-item {
  margin-bottom: 10px;
  margin-top: 5px;
  margin-left: 2%;
  width: 96%;
  border-bottom: 2px solid #e7eaef;
  cursor: pointer;
}

.card-top {
  margin-bottom: 5px;
  display: flex;
}

.card-top-left {
  overflow: hidden;
  font-weight: bold;
  font-size: 20px;
  color: #50565e;
  width: 70%;
}

.card-top-right {
  overflow: hidden;
  font-weight: lighter;
  font-size: 12px;
  color: #4d5559;
  width: 30%;
}

.card-bottom {
  font-size: 15px;
  color: #343839;
  overflow: hidden;
}

.visitor-card {
  height: 90%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 25px;
  color: #79868f;
}

.background-rightTop {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 30%;
}

.card-image {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  cursor: pointer;
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

.rest {
  color: #fff;
  background-color: #0ce18f;
  border-radius: 50%;
  padding: 3px;
  font-size: 10px;
  margin-left: 5px;
}
</style>
