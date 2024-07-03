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
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import {onBeforeUnmount, ref, shallowRef} from "vue";

defineOptions({
  name: 'textEditor'
})

const mode='default'

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref("")

const toolbarConfig = {}
const editorConfig = { placeholder: '请输入内容...' }

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例
}

const sendHtml=()=>{
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