<template>
  <div :style="{ width: props.divWidth }">
    <!-- 进度显示 -->
    <slot name="progress">
      <el-progress
          class="progress-line"
          :text-inside="true"
          :percentage="percent.toFixed()"
          :stroke-width="20"
          :format="progressFormat"
      />
    </slot>

    <slot name="buttons">
      <div class="buttons">
        <el-button type="success" @click="downloadFile" :disabled="btnFlag">下 载</el-button>
        <el-button
            type="primary"
            v-show="download"
            @click="handlePause"
        >暂 停
        </el-button
        >
        <el-button
            type="primary"
            v-show="!download"
            @click="handleContinue"
        >继 续
        </el-button
        >
      </div>
    </slot>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import http from "@/request/http.js";

const props = defineProps({
  //组件的宽度
  divWidth: {
    type: String,
    default: '360px',
  },
  downloadUrl: {
    type: String,
    default: '/files/downloadChunk',
  },
  percent:{
    type: Number,
    default: 0,
  },
})

//下载进度
let percent = ref(props.percent);
const emit = defineEmits(['handleProcess', 'handleComplete','update:percent'])

//请求的列表
let requestList = ref([])
//接收的文件分片
let chunks = ref([])
//分片的开始字节
let rangeStart = ref(0)
//分片的结束字节
let rangeEnd = ref(0)
//发送的次数
let sendNum = ref(0)
//文件的名称
let fileName = ref("")
//文件的大小
let fileSize = ref(0)
//分片的大小
let chunkSize = ref(0)
//判断下载的暂停与继续
let download = ref(true)
//判断当前是否在下载
let btnFlag = ref(false)
//每一个分片的大小占比
let percentCount = ref(0)

//进度条的文字显示
const progressFormat = (percentage) => (percentage === '100' ? '下载完成' : `${percentage}%`)

const downloadFile = async () => {
  if (btnFlag.value) return
  btnFlag.value = true
  const url = import.meta.env.VITE_BASE_API + props.downloadUrl;
  percent.value = 0
  // 获得文件的大小，名字，分片大小
  const response = await http.get(url);
  fileName.value = response.headers['content-disposition'].match(/filename=([^;]+)/)[1];
  let parts = response.headers['content-length'].split("-");
  fileSize.value = parseInt(parts[0])
  chunkSize.value = parseInt(parts[1])
  rangeStart.value = 0;
  rangeEnd.value = chunkSize.value - 1;

  // 计算分片的数量
  const numChunks = Math.ceil(fileSize.value / chunkSize.value);
  percentCount.value = 100 / numChunks
  if (fileSize.value < chunkSize.value) {
    rangeEnd.value = fileSize.value - 1;
  }

  // 下载分片
  for (let i = 0; i < numChunks; i++) {
    const fn = () => {
      const range = `bytes=${rangeStart.value}-${rangeEnd.value}`;
      const config = {
        headers: {
          Range: range
        },
        responseType: 'arraybuffer'
      };
      return http.get(props.downloadUrl, config).then(res => {
        chunks.value.push(res.data);
        rangeStart.value = rangeEnd.value + 1;
        rangeEnd.value = Math.min(rangeEnd.value + chunkSize.value, fileSize.value - 1)
        percent.value = Math.min(percent.value + percentCount.value, 100)
        emit('update:percent',percent.value)
      }).catch(() => {
        download.value = false
      })
    }
    requestList.value.push(fn);
  }
  emit('handleProcess', fileSize.value, chunkSize.value, fileName.value)
  send()
}

const send = async () => {
  if (!download.value || requestList.value.length === 0) return
  if (sendNum.value >= requestList.value.length) {
    complete()
    return
  }
  await requestList.value[sendNum.value]()
  sendNum.value++
  send()
}

const complete = () => {
  btnFlag.value = false
  emit('handleComplete', fileName.value, chunks.value)
  // 合并分片
  const blob = new Blob(chunks.value)
  const link = document.createElement('a')
  link.href = window.URL.createObjectURL(blob)
  link.download = fileName.value
  link.click()
  window.URL.revokeObjectURL(blob)
  resetStatus()
}

//重置状态
const resetStatus = () => {
  requestList.value.length = 0;
  chunks.value.length = 0
  rangeStart.value = 0
  rangeEnd.value = 0
  sendNum.value = 0
  percentCount.value = 0
}

// 按下暂停按钮
const handlePause = () => {
  if (requestList.value.length > 0) {
    download.value = false
  }
}

// 按下继续按钮
const handleContinue = () => {
  if (requestList.value.length > 0) {
    download.value = true
    send()
  }
}

//将组件的下载，暂停，继续方法暴露出去
defineExpose({
  downloadFile,
  handlePause,
  handleContinue,
})
</script>

<style scoped lang='scss'>
.buttons {
  margin-top: 10px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
}
</style>