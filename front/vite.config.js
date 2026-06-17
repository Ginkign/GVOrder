// export default {
//   server: {
//     proxy: {
//       '/api/users': 'http://8.137.189.172:8081',
//       '/api/products': 'http://8.137.189.172:8082',
//       '/api/orders': 'http://8.137.189.172:8083'
//     }
//   }
// };

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    host: '0.0.0.0', // 允许外部访问
    port: 5173,      // 指定端口（可选，默认也是5173）
    open: true       // 启动时自动打开浏览器（可选）
  }
})