import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue(),
  ],

  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },

  server:{
    host: "0.0.0.0", // ip
    port: 5100,  // 端口号
    open: false,  // 是否自动在浏览器打开
    https: false, // 是否开启 http
    // 跨域代理配置
    proxy: {
      '/api': {
        target: 'http://localhost:7070',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  },

  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@import "./src/styles/layout.scss";`
      }
    }
  }
})
