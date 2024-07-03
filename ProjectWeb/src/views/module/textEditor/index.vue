<template>
  <div>
    <el-button @click="sendHtml">确定</el-button>
    <div style="border: 1px solid #ccc">
      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="editorRef"
          :defaultConfig="toolbarConfig"
          :mode="mode"
      />
      <Editor
          style="height: 500px; overflow-y: hidden;"
          v-model="valueHtml"
          :defaultConfig="editorConfig"
          :mode="mode"
          @onCreated="handleCreated"
      />
    </div>
  </div>
</template>

<script setup>
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import {onBeforeUnmount, ref, shallowRef} from "vue";

defineOptions({
  name: 'textEditor'
})

const mode = 'default'

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref("")


const toolbarConfig = {
  // insertKeys: {
  //   index: 0, // 自定义插入的位置
  //   keys: ["uploadAttachment"], // “上传附件”菜单
  // },
}


const editorConfig = {placeholder: '请输入内容...', MENU_CONF: {}}

editorConfig.MENU_CONF['uploadImage'] = {
  server: import.meta.env.VITE_BASE_API + '/files/uploadEditor',
  fieldName: 'file',
  onSuccess(file, res) {

  },
  // 用户自定义插入图片
  customInsert(res, insertFn) {
    const imgInfo  = res.data || ""
    const { url, alt } = imgInfo
    if (!url) throw new Error(`Image url is empty`)
    // 自己插入图片
    insertFn(url, alt)
  },
}

editorConfig.MENU_CONF["uploadVideo"] = {
  // 最多可上传几个文件，默认为 5
  maxNumberOfFiles: 1,
  server: import.meta.env.VITE_BASE_API + '/files/uploadEditor',
  fieldName: 'file',
  onSuccess(file, res) {

  },
  // 用户自定义插入图片
  customInsert(res, insertFn) {
    const videoInfo  = res.data || ""
    const { url, alt } = videoInfo
    if (!url) throw new Error(`Image url is empty`)
    // 自己插入图片
    insertFn(url, alt)
  },
}

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例
}

const sendHtml = () => {
  console.log(valueHtml.value)
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

</script>

<style scoped>

</style>