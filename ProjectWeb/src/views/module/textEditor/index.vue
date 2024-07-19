<template>
  <div>
    <el-button @click="sendHtml">确定</el-button>
    <RichEditor v-model="valueHtml" :width="calculatedWidth"></RichEditor>
  </div>
</template>

<script setup>
import {ref} from "vue";

defineOptions({
  name: 'textEditor'
})

//要传给子组件的宽度
const calculatedWidth = 'calc(100vw-335px)';

// 内容 HTML
let valueHtml = ref("")

const sendHtml = () => {
  console.log(valueHtml.value)
}

</script>

<style scoped>

</style>
