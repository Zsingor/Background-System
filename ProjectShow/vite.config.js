import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server:{
    host: "0.0.0.0", // ip
    port: 7052,  // 端口号
    open: false,  // 是否自动在浏览器打开
    https: false, // 是否开启 http
    // 跨域代理配置
    proxy: {
      '/api': {
        target: 'http://localhost:7050',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  },
})
