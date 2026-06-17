<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">电商平台登录</h2>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="loginForm.username" 
            placeholder="请输入用户名" 
            required
          />
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="loginForm.password" 
            placeholder="请输入密码" 
            required
          />
        </div>

        <div v-if="errorMessage" class="error-msg">
          {{ errorMessage }}
        </div>

        <button type="submit" :disabled="isLoading" class="login-btn">
          {{ isLoading ? '登录中...' : '立即登录' }}
        </button>
      </form>

      <div class="footer-links">
        <span>还没有账号？</span>
        <router-link to="/register">去注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { userApi } from '../utils/api'; // 引入之前封装的 api

const router = useRouter();

// 表单数据
const loginForm = reactive({
  username: '',
  password: ''
});

// 状态控制
const isLoading = ref(false);
const errorMessage = ref('');

// 登录处理函数
const handleLogin = async () => {
  // 重置错误信息
  errorMessage.value = '';
  isLoading.value = true;

  try {
    // 调用后端登录接口
    const response = await userApi.post('/api/users/login', {
      username: loginForm.username,
      password: loginForm.password
    });

    // 登录成功，后端返回用户对象
    const user = response.data;
    
    // 1. 保存用户信息到 LocalStorage (实际项目中建议保存 Token)
    localStorage.setItem('currentUser', JSON.stringify(user));
    localStorage.setItem('userId', user.id);

    // 2. 提示成功
    alert('登录成功！欢迎回来，' + user.username);

    // 3. 跳转到首页或商品页
    router.push('/');

  } catch (error) {
    console.error('Login error:', error);
    // 处理错误信息
    if (error.response && error.response.status === 401) {
      errorMessage.value = '用户名或密码错误';
    } else {
      errorMessage.value = '登录失败，请稍后重试';
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box; /* 确保padding不撑大宽度 */
}

.form-group input:focus {
  border-color: #409eff;
  outline: none;
}

.error-msg {
  color: #ff4d4f;
  font-size: 14px;
  margin-bottom: 15px;
  text-align: center;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #66b1ff;
}

.login-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.footer-links {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #666;
}

.footer-links a {
  color: #409eff;
  text-decoration: none;
  margin-left: 5px;
}

.footer-links a:hover {
  text-decoration: underline;
}
</style>