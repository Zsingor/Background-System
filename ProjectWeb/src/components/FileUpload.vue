<!--  -->
<template>
  <div :style="{ width: props.divWidth }">
    <div class="uploadWrapper">
      <el-upload
        ref="uploadRef"
        action="#"
        drag
        v-model:file-list="fileList"
        :accept="props.fileAccept"
        :disabled="disable"
        :auto-upload="false"
        :show-file-list="true"
        :on-change="handleChange"
        :on-remove="handleRemove"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>

        <template #tip>
          <div class="el-upload__tip">
            {{ props.tips }}
          </div>
        </template>
      </el-upload>
    </div>

    <!-- 进度显示 -->
    <el-progress
      class="progress-line"
      :text-inside="true"
      :percentage="percent.toFixed()"
      :stroke-width="20"
    />
    <div class="progress-box">
      <span>上传进度：{{ percent.toFixed() }}%</span>
      <div>
        <el-button
          type="success"
          size="small"
          @click="sendRequest"
          :disabled="!disable"
          >上传</el-button
        >
        <el-button type="primary" size="small" @click="handleClickBtn">{{
          upload ? '暂停' : '继续'
        }}</el-button>
        <el-button
          type="warning"
          size="small"
          @click="clearUpload"
          :disabled="!disable"
          >清空</el-button
        >
      </div>
    </div>
  </div>
</template>
  
<script setup>
import SparkMD5 from 'spark-md5'
import request from '@/request'
import { message } from '@/utils/message.js'
import { onMounted, ref } from 'vue'

const props = defineProps({
  //分片文件大小，单位MB
  chunkSize: {
    type: Number,
    default: 2,
  },
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
  //允许上传的文件类型
  fileAccept: {
    type: String,
    default: '.csv,.doc,.png',
  },
  //允许上传的文件最大大小
  fileSize: {
    type: Number,
    default: 0,
  },
})

const uploadRef = ref(null)

//上传成功的文件列表
let fileList = ref([])
//进度
let percent = ref(0)
//控制上传的暂停与继续
let upload = ref(true)
//控制是否能上传
let disable = ref(false)
//每一个分片的大小占比
let percentCount = ref(0)
//当前正在上传的文件
let curFile = ref(null)
//文件名
let filename = ref('')
//存放每个分片
let chunkList = ref([])
// 存放文件的MD5哈希值
let hash = ref('')
// 记录发送的请求个数
let sendNum = ref(0)
// 存放每个分片的请求
let requestList = ref([])
//存放允许的后缀集合
let acceptedExtensions = ref({})

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
  percentCount.value = 0

  // 获取文件并转成 ArrayBuffer 对象
  const fileObj = file.raw
  filename.value = fileObj.name
  let buffer
  try {
    buffer = await fileToBuffer(fileObj)
  } catch (e) {
    console.log(e)
    return
  }

  const chunkSize = props.chunkSize * 1024 * 1024
  const suffix = /\.([0-9A-z]+)$/.exec(fileObj.name)[1]
  const spark = new SparkMD5.ArrayBuffer()
  spark.append(buffer)
  const fileHash = spark.end()

  let curChunk = 0
  const chunkListLength = Math.ceil(fileObj.size / chunkSize)
  for (let i = 0; i < chunkListLength; i++) {
    const item = {
      chunk: fileObj.slice(curChunk, curChunk + chunkSize),
      fileName: `${fileHash}_${i}.${suffix}`,
    }
    curChunk += chunkSize
    chunkList.value.push(item)
  }
  hash.value = fileHash
  //sendRequest()
}

//发送请求
const sendRequest = async () => {
  chunkList.value.forEach((item, index) => {
    const fn = () => {
      const formData = new FormData()
      formData.append('chunk', item.chunk)
      formData.append('filename', item.fileName)
      return request
        .post('/files/chunk', formData)
        .then((res) => {
          if (res.code === 1) {
            // 成功
            if (percentCount.value === 0) {
              percentCount.value = 100 / chunkList.value.length
            }
            if (percent.value >= 100) {
              percent.value = 100
            } else {
              percent.value += percentCount.value // 改变进度
            }
            if (percent.value >= 100) {
              percent.value = 100
            }
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
  if (!upload.value) return
  if (sendNum.value >= requestList.value.length) {
    // 发送完毕
    complete()
    return
  }
  await requestList.value[sendNum.value]()
  sendNum.value++
  send()
}

//重置上传状态
const resetStatus = () => {
  requestList.value.length = 0
  chunkList.value.length = 0
  percentCount.value = 0
  sendNum.value = 0
  disable.value = false
  curFile.value = null
}

// 文件切片全部发送完毕后,把文件的 hash 传递给服务器,让服务器合并文件
const complete = () => {
  request
    .get('/files/merge', {
      params: {
        hash: hash.value,
        filename: filename.value,
      },
    })
    .then((res) => {
      if (res.code === 1) {
        curFile.value.response = res
        message('文件上传成功')
      }
    })
    .finally(() => {
      resetStatus()
    })
}

// 按下暂停按钮
const handleClickBtn = () => {
  if (requestList.value.length > 0) {
    upload.value = !upload.value
    // 如果不暂停则继续上传
    if (upload.value) {
      send()
    }
  }
}

//按下清空按钮
const clearUpload = () => {
  fileList.value = fileList.value.filter((item) =>
    item.hasOwnProperty('response')
  )
  resetStatus()
}

//校验文件的类型是否正确
const checkFile = (mimeName) => {
  if (acceptedExtensions.value.length === 0) {
    return true
  }
  const extension = '.' + mimeName.split('.').pop().toLowerCase()

  // 检查扩展名是否在集合中
  return acceptedExtensions.value.has(extension)
}

// 将 File 对象转为 ArrayBuffer
const fileToBuffer = (file) => {
  return new Promise((resolve, reject) => {
    const fr = new FileReader()
    fr.onload = (e) => {
      resolve(e.target.result)
    }
    fr.readAsArrayBuffer(file)
    fr.onerror = () => {
      reject(new Error('转换文件格式发生错误'))
    }
  })
}

//移除文件列表的回调
const handleRemove = (file, fileLists) => {
  request
    .post(`/files/delChunkFile/${file.response.data}`)
    .then((res) => {
      if (res.code === 1) {
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

.progress-line {
  width: 100%;
}
</style>