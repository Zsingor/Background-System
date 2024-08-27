<template>
  <div>
    <RichEditor v-model="valueHtml" :width="calculatedWidth"></RichEditor>
    <div class="btn">
      <el-button @click="sendHtml" type="primary" plain>显示</el-button>
      <el-button @click="clearHtml" type="warning" plain>清空</el-button>
    </div>
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

const clearHtml=()=>{
  valueHtml.value=""
}

</script>

<style scoped>
.btn{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}
</style>
