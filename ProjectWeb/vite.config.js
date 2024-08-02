import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

import Icons from 'unplugin-icons/vite'
import {FileSystemIconLoader} from "unplugin-icons/loaders";

// 引入 Icon自动引入解析器
import IconsResolver from 'unplugin-icons/resolver'
import Components from 'unplugin-vue-components/vite'

export default defineConfig({
  plugins: [
    vue(),
    // 使用自动引入插件
    Components({
      // 配置解析器
      resolvers: [
        // Icon自动引入解析器
        IconsResolver({
          // 自动引入的Icon组件统一前缀，默认为 i，设置false为不需要前缀
          prefix: 'icon',
          // 当图标集名字过长时，可使用集合别名
          alias: {
            system: 'system-uicons'
          },
          // 标识自定义图标集
          customCollections: ['home']
        })
      ]
    }),
    Icons({
      compiler: 'vue3',
      // 自动安装
      autoInstall: true,
      // 自定义图标加载
      customCollections: {
        // home图标集
        // 给svg文件设置fill="currentColor"属性，使图标的颜色具有适应性
        //home: FileSystemIconLoader('./src/assets/svg/home', svg => svg.replace(/^<svg /, '<svg fill="currentColor" ')),
      }
    }),
  ],

  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },

  server:{
    host: "0.0.0.0", // ip
    port: 7051,  // 端口号
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

  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@import "./src/styles/layout.scss";`
      }
    }
  }
})
