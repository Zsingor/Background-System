<template>
  <div :style="{ width: props.divWidth }">
    <div class="uploadWrapper">
      <el-upload
        ref="uploadRef"
        action="#"
        :drag="props.draggable"
        v-model:file-list="fileList"
        :accept="props.fileAccept"
        :disabled="disable"
        :auto-upload="false"
        :show-file-list="true"
        :on-change="handleChange"
        :on-remove="handleRemove"
      >
        <slot name="content">
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </slot>

        <template #tip>
          <div class="el-upload__tip">
            {{ props.tips }}
          </div>
        </template>
      </el-upload>
    </div>

    <!-- 进度显示 -->
    <slot name="progress">
      <el-progress
        class="progress-line"
        :text-inside="true"
        :percentage="process.toFixed()"
        :stroke-width="20"
        :format="progressFormat"
      />
    </slot>

    <!-- 底部操作区域 -->
    <slot name="box">
      <div class="progress-box">
        <span>上传进度：{{ percent.toFixed() }}%</span>
        <div>
          <el-button
            type="success"
            size="small"
            @click="sendRequest"
            :disabled="!btnFlag || autoUpload"
            >上传</el-button
          >
          <el-button
            type="primary"
            size="small"
            v-show="upload"
            @click="handlePause"
            >暂停</el-button
          >
          <el-button
            type="primary"
            size="small"
            v-show="!upload"
            @click="handleContinue"
            >继续</el-button
          >
          <el-button
            type="warning"
            size="small"
            @click="clearUpload"
            :disabled="!btnFlag"
            >清空</el-button
          >
        </div>
      </div>
    </slot>
  </div>
</template>
  
<script setup>
import request from '@/request'
import { message } from '@/utils/message.js'
import { computed, onMounted, ref } from 'vue'
import Worker from './worker?worker&inline'

const props = defineProps({
  //组件的宽度
  divWidth: {
    type: String,
    default: '360px',
  },
  //文字提示
  tips: {
    type: String,
    default: '',
  },
  //是否可拖拽上传
  draggable: {
    type: Boolean,
    default: true,
  },
  //是否自动上传
  autoUpload: {
    type: Boolean,
    default: false,
  },
  //分片文件大小，单位MB
  chunkSize: {
    type: Number,
    default: 2,
  },
  //允许上传的文件类型，格式".csv,.doc,.png"
  fileAccept: {
    type: String,
    default: '',
  },
  //允许上传的文件最大大小
  fileSize: {
    type: Number,
    default: 0,
  },
  //发送分片的请求url
  sendUrl: {
    type: String,
    default: '/files/chunk',
  },
  //发送合并的请求url
  completeUrl: {
    type: String,
    default: '/files/merge',
  },
  //删除文件的请求url
  removeUrl: {
    type: String,
    default: '/files/delChunkFile',
  },
  //处理的进度
  process: {
    type: Number,
    default: 0,
  },
})

//处理文件的进度
let process = ref(props.process)
//上传文件的进度
const percent = defineModel('percent', { type: Number, default: 0 })

const emit = defineEmits([
  'handleRemove',
  'handleComplete',
  'handleProcess',
  'handleUpload',
  'handleClear',
  'update:process',
])

const uploadRef = ref(null)

//上传成功的文件列表
let fileList = ref([])
//每一个分片的大小占比
let percentCount = ref(0)
//控制上传的暂停与继续
let upload = ref(true)
//控制是否禁止上传
let disable = ref(false)
//控制是否允许按钮操作
let btnFlag = ref(false)
//当前正在上传的文件
let curFile = ref(null)
//文件名
let filename = ref('')
//存放每个分片
let chunkList = ref([])
// 存放文件的MD5哈希值列表
let hashList = ref([])
// 记录发送的请求个数
let sendNum = ref(0)
// 存放每个分片的请求
let requestList = ref([])
//存放允许的后缀集合
let acceptedExtensions = ref(null)

//进度条的文字显示
const progressFormat = (percentage) =>
  percentage === '100' ? '处理完毕' : `${percentage}%`

//选择文件后的回调
const handleChange = async (file) => {
  if (!file) return
  if (!checkFile(file.raw.name)) {
    message('所选文件格式不正确', 'error')
    return
  }
  if (props.fileSize !== 0) {
    if (file.raw.size > props.fileSize * 1024 * 1024) {
      message('所选文件过大', 'error')
      return
    }
  }

  curFile.value = file
  disable.value = true
  percent.value = 0
  process.value = 0
  percentCount.value = 0

  const fileObj = file.raw
  filename.value = fileObj.name
  const chunkSize = props.chunkSize * 1024 * 1024

  //计算每一个分片的百分比
  const chunkListLength = Math.ceil(fileObj.size / chunkSize)
  if (percentCount.value === 0) {
    percentCount.value = 100 / chunkListLength
  }

  //获得文件分片列表
  chunkList.value = await getChunkList(fileObj)

  //允许按钮操作
  btnFlag.value = true
  //文件分片成功的回调,chunkList分片文件列表
  emit('handleProcess', chunkList.value)
  if (props.autoUpload) {
    sendRequest()
  }
}

//发送请求
const sendRequest = async () => {
  if (!btnFlag.value) {
    return
  }
  btnFlag.value = false
  chunkList.value.forEach((item, index) => {
    const fn = () => {
      const formData = new FormData()
      formData.append('chunk', item.chunk)
      formData.append('filename', item.fileName)
      hashList.value.push(item.fileName)
      return request
        .post(props.sendUrl, formData)
        .then((res) => {
          if (res.code === 1) {
            percent.value = Math.min(percent.value + percentCount.value, 99)
            chunkList.value.splice(index, 1) // 一旦上传成功就删除这一个 chunk，方便断点续传
          }
        })
        .catch(() => {
          upload.value = false
        })
    }
    requestList.value.push(fn)
  })
  send() // 发送请求
}

//递归调用每个分片的发送请求
const send = async () => {
  if (!upload.value || requestList.value.length === 0) return
  // 发送完毕
  if (sendNum.value >= requestList.value.length) {
    //分片文件上传成功的回调,filename文件名,hashList文件的哈希列表
    emit('handleUpload', filename.value, hashList.value)
    complete()
    return
  }
  await requestList.value[sendNum.value]()
  sendNum.value++
  send()
}

// 文件切片全部发送完毕后,把文件的 hash 传递给服务器,让服务器合并文件
const complete = () => {
  const data = {
    filename: filename.value,
    chunkList: hashList.value,
  }

  request({
    timeout: 20000,
    url: props.completeUrl,
    method: 'post',
    data: data,
  })
    .then((res) => {
      if (res.code === 1) {
        curFile.value.response = res
        message('文件上传成功')
      } else {
        message('文件上传失败', 'error')
      }
    })
    .catch((err) => {
      message('文件上传失败', 'error')
    })
    .finally(() => {
      percent.value = 100
      //分片文件合并成功的回调,curFile合并成功的文件信息,fileList当前的文件列表
      emit('handleComplete', curFile.value, fileList.value)
      resetStatus()
    })
}

//重置上传状态
const resetStatus = () => {
  requestList.value.length = 0
  chunkList.value.length = 0
  percentCount.value = 0
  sendNum.value = 0
  disable.value = false
  curFile.value = null
  hashList.value.length = 0
}

//使用多线程获取分片列表
const getChunkList = (file) => {
  let reso, reje
  const p = new Promise((resolve, reject) => {
    reso = resolve
    reje = reject
  })

  let result = []
  let finishCount = 0
  //根据cpu内核数的3/4分配线程数
  const THREAD_COUNT = Math.ceil((navigator.hardwareConcurrency * 3) / 4) || 4
  //分片大小
  const chunkSize = props.chunkSize * 1024 * 1024
  //分片数量
  const chunkCount = Math.ceil(file.size / chunkSize)
  //每个线程的任务数
  const threadChunkCount = Math.ceil(chunkCount / THREAD_COUNT)
  //每个线程的进度百分比
  let processCount = 100 / chunkCount
  //分配线程任务
  for (let i = 0; i < THREAD_COUNT; i++) {
    const worker = new Worker()
    const start = i * threadChunkCount
    const end = Math.min((i + 1) * threadChunkCount, chunkCount)
    worker.postMessage({ file, start, end, chunkSize })
    worker.onmessage = (e) => {
      if (typeof e.data !== 'object') {
        //更新文件处理的进度
        process.value = Math.min(process.value + processCount, 100)
        emit('update:process', process.value)
      } else {
        result[i] = e.data
        worker.terminate
        finishCount++
        if (finishCount === THREAD_COUNT) {
          reso(result.flat())
        }
      }
    }
  }

  return p
}

// 按下暂停按钮
const handlePause = () => {
  if (requestList.value.length > 0) {
    upload.value = false
  }
}

// 按下继续按钮
const handleContinue = () => {
  if (requestList.value.length > 0) {
    upload.value = true
    send()
  }
}

//按下清空按钮,清除处理完成但是未上传的文件
const clearUpload = () => {
  if (btnFlag.value) {
    fileList.value = fileList.value.filter((item) =>
      item.hasOwnProperty('response')
    )
    resetStatus()
    //清空列表成功的回调,fileList当前的文件列表
    emit('handleClear', fileList.value)
  }
}

//校验文件的类型是否正确
const checkFile = (mimeName) => {
  if (acceptedExtensions.value.size === 0) {
    return true
  }
  const extension = '.' + mimeName.split('.').pop().toLowerCase()

  // 检查扩展名是否在集合中
  return acceptedExtensions.value.has(extension)
}

//移除文件列表的回调
const handleRemove = (file, fileLists) => {
  request
    .post(props.removeUrl + `/${file.response.data}`)
    .then((res) => {
      if (res.code === 1) {
        //文件删除成功的回调,file删除的文件信息,fileLists删除后的文件列表
        emit('handleRemove', file, fileLists)
        message('移除文件成功')
      } else {
        message('移除文件失败', 'error')
      }
    })
    .catch((err) => {
      console.log(err)
      message('移除文件失败', 'error')
    })
}

//将组件的上传，暂停，清空方法暴露出去
defineExpose({
  sendRequest,
  handlePause,
  handleContinue,
  clearUpload,
})

onMounted(() => {
  if (props.fileAccept !== '') {
    acceptedExtensions.value = new Set(
      props.fileAccept.split(',').map((ext) => ext.trim().toLowerCase())
    )
  } else {
    acceptedExtensions.value = new Set()
  }
})
</script>

  
<style scoped lang='scss'>
.uploadWrapper {
  width: 100%;
}

.progress-line {
  width: 100%;
}

.progress-box {
  box-sizing: border-box;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  padding: 8px 10px;
  background-color: #ecf1ff;
  font-size: 14px;
  border-radius: 4px;
}
</style>