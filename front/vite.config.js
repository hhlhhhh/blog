import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'


// https://vitejs.dev/config/
export default defineConfig({
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8090/',// 后端接口
                changeOrigin: true, // 是否跨域
                pathRewrite: {
                    '/api': ''
                }
            }
        }
    },
  plugins: [
      vue()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
    dedupe: [
	    'vue'
	  ]
  },
    define:{
      'process.env':{
          BASE_URL:"http://localhost:8090"
      }
    }
})
