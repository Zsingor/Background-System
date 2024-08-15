<template>
  <div class="background">
    <el-card class="card-item">
      <template #header>
        <div class="card-header">
          <span>单文件的上传与移除</span>
        </div>
      </template>
      <el-upload
        :action="$baseUrl + '/files/upload'"
        :headers="{ token: userInfo.baseInfo.token }"
        :on-success="handleAddSuccess"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
        :limit="1"
        list-type="picture"
      >
        <el-button type="primary">点击上传</el-button>
        <template #tip>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
        </template>
      </el-upload>
    </el-card>

    <el-card class="card-item">
      <template #header>
        <div class="card-header">
          <span>文件的分片上传</span>
        </div>
      </template>
      <FileUpload :draggable="true">
        <template #content>
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </template>
      </FileUpload>
    </el-card>

    <el-card class="card-item">
      <template #header>
        <div class="card-header">
          <span>文件的手动上传</span>
        </div>
      </template>
      <el-upload
        ref="uploadRef"
        :action="$baseUrl + '/files/upload'"
        :headers="{ token: userInfo.baseInfo.token }"
        :on-success="handleAddSuccess"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
        :limit="1"
        :auto-upload="false"
        list-type="picture"
      >
        <template #trigger>
          <el-button type="primary">选择文件</el-button>
        </template>
        <el-button class="btn-upload" type="success" @click="submitUpload">
          上传文件
        </el-button>

        <template #tip>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
        </template>
      </el-upload>
    </el-card>

    <el-card class="card-item">
      <template #header>
        <div class="card-header">
          <span>文件的拖拽上传</span>
        </div>
      </template>
      <el-upload
        drag
        multiple
        :action="$baseUrl + '/files/upload'"
        :headers="{ token: userInfo.baseInfo.token }"
        :on-success="handleAddSuccess"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">拖拽文件到此或 <em>点击选择文件</em></div>
        <template #tip>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
        </template>
      </el-upload>
    </el-card>

    <el-card class="card-item">
      <template #header>
        <div class="card-header">
          <span>覆盖前一个文件</span>
        </div>
      </template>
      <el-upload
        ref="uploadRef1"
        :action="$baseUrl + '/files/upload'"
        :headers="{ token: userInfo.baseInfo.token }"
        :on-success="handleAddSuccess"
        :on-remove="handleRemove"
        :before-upload="beforeUpload"
        :limit="1"
        :auto-upload="false"
        :on-exceed="handleExceed"
      >
        <template #trigger>
          <el-button type="primary">选择文件</el-button>
        </template>
        <el-button class="btn-upload" type="success" @click="submitUpload1">
          上传文件
        </el-button>

        <template #tip>
          <div class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
        </template>
      </el-upload>
    </el-card>

    <el-card class="card-item">
      <template #header>
        <div class="card-header">
          <span>自定义缩略图</span>
        </div>
      </template>
      <el-button
        class="btn-upload"
        type="success"
        @click="submitUpload2"
        style="margin-bottom: 10px"
      >
        上传文件
      </el-button>
      <el-upload
        ref="uploadRef2"
        :file-list="fileList"
        :action="$baseUrl + '/files/upload'"
        :headers="{ token: userInfo.baseInfo.token }"
        :auto-upload="false"
        list-type="picture-card"
        :on-change="imgHandleChange"
      >
        <el-icon><Plus /></el-icon>
        <template #file="{ file }">
          <div>
            <img
              class="el-upload-list__item-thumbnail"
              :src="file.url"
              alt=""
            />
            <span class="el-upload-list__item-actions">
              <span
                class="el-upload-list__item-preview"
                @click="handlePictureCardPreview(file)"
              >
                <el-icon><zoom-in /></el-icon>
              </span>
              <span
                class="el-upload-list__item-delete"
                @click="handleDownload(file)"
              >
                <el-icon><Download /></el-icon>
              </span>
              <span
                class="el-upload-list__item-delete"
                @click="handleRemoveFile(file)"
              >
                <el-icon><Delete /></el-icon>
              </span>
            </span>
          </div>
        </template>
      </el-upload>
    </el-card>

    <el-dialog v-model="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="Preview Image" />
    </el-dialog>
  </div>
</template>

<script setup>
import { message } from '@/utils/message.js'
import { userInfo } from '@/layout/user.js'
import request from '@/request/index.js'
import { ref } from 'vue'
import { genFileId } from 'element-plus'

defineOptions({
  name: 'filesUpload',
})

const uploadRef = ref(null)
const uploadRef1 = ref(null)
const uploadRef2 = ref(null)

let dialogVisible = ref(false)
let dialogImageUrl = ref('')
let fileList = ref([])

const submitUpload = () => {
  uploadRef.value.submit()
}

const submitUpload1 = () => {
  uploadRef1.value.submit()
}

const submitUpload2 = () => {
  uploadRef2.value.submit()
}

const imgHandleChange = (file, filelist) => {
  fileList.value = filelist
}

//预览图片
const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

const handleDownload = (file) => {
  let a = document.createElement('a')
  let event = new MouseEvent('click')
  a.download = file.name
  a.href = file.url
  a.dispatchEvent(event)
}

//删除图片
const handleRemoveFile = (file) => {
  request
    .post(`/files/deletefile/${file.response.data}`)
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
  uploadRef2.value.handleRemove(file)
}

//文件替换的回调
const handleExceed = (files) => {
  uploadRef1.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  uploadRef1.value.handleStart(file)
}

//文件上传成功回调
const handleAddSuccess = (res) => {
  if (res.code === 1) {
    message('文件上传成功')
  }
}

//移除文件列表的回调
const handleRemove = (file, fileLists) => {
  console.log(file)

  request
    .post(`/files/deletefile/${file.response.data}`)
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

//上传文件前的校验
const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 10

  if (!isJPG && !isPNG) {
    message('上传图片只能是 JPG或PNG 格式!', 'error')
  }
  if (!isLt2M) {
    message('上传头像图片大小不能超过 10MB!', 'error')
  }
  return (isJPG || isPNG) && isLt2M
}
</script>

<style scoped>
.background {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  width: 100%;
}

.card-item {
  margin-top: 10px;
  margin-bottom: 15px;
  width: 90%;
}

.card-header {
  display: flex;
  justify-content: center;
}

.btn-upload {
  margin-left: 10px;
}
</style>