<!--  -->
<template>
  <div class="">
    <button @click="downloadFile">下载文件</button>
  </div>
</template>

<script setup>
import request from '@/request';
import axios from 'axios'
import { ref } from 'vue';

let requestList=ref([])

const downloadFile=async ()=>{
      const url = 'http://localhost:7050/files/downloadChunk';
      let start = 0;
      let end = 0;
      let chunks = [];
      let chunkSize = 0;
      let fileSize = 0;
      let fileName=""
      

      // 获得文件的大小，名字，分片大小
      const response = await axios.head(url);
      
      fileName = response.headers['content-disposition'].match(/filename=([^;]+)/)[1];
      let parts = response.headers['content-length'].split("-");
      fileSize=parseInt(parts[0])
      chunkSize=parseInt(parts[1])
      start = 0;
      end = chunkSize - 1;

      // 计算分片的数量
      const numChunks = Math.ceil(fileSize / chunkSize);
      if (fileSize < chunkSize) {
        end = fileSize - 1;
      }
      console.log("numChunks ",numChunks);
      console.log(fileSize,chunkSize,numChunks);
      
      // 下载分片
      for (let i = 0; i < numChunks; i++) {

        const range = `bytes=${start}-${end}`;
        console.warn(`分片====${range}`);
        const config = {
          headers: {
            Range: range
          },
          responseType: 'arraybuffer'
        };
        const response = await axios.get(url, config);
        chunks.push(response.data);
        start = end + 1;
        end = Math.min(end + chunkSize, fileSize - 1);
      }

      // 合并分片
      const blob = new Blob(chunks);
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      window.URL.revokeObjectURL(blob);
}
</script>

<style scoped lang='scss'>
</style>