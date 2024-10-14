<template>
  <div>
    <RichEditor ref="editorRef" v-model="valueHtml" :width="calculatedWidth" @getHtml="getHtml"></RichEditor>
    <div class="btn">
      <el-button @click="copyHtml" type="primary" plain>复制</el-button>
      <el-button @click="clearHtml" type="warning" plain>清空</el-button>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";

defineOptions({
  name: 'textEditor'
})

//要传给子组件的宽度
const calculatedWidth = 'calc(100vw-335px)';

//ref
const editorRef = ref(null)
// 内容 HTML
let valueHtml = ref("")
//text
let valueText = ""

//输入回调
const getHtml = (text, html) => {
  valueText = text
}

const copyHtml = () => {
  navigator.clipboard.writeText(valueText + "\n------------权限管理系统/富文本编辑器------------")
}

//清空编辑器内容
const clearHtml = () => {
  valueHtml.value = ""
}

onMounted(() => {
  if (editorRef.value) {
    const editorElement = editorRef.value.$el
    editorElement.addEventListener('copy', (e) => {
      e.preventDefault()
      navigator.clipboard.writeText(valueText + "\n------------权限管理系统/富文本编辑器------------")
    })
  }
})

</script>

<style scoped>
.btn {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
</style>
