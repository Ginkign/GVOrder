<template>
  <div class="register-container">
    <div class="register-box">
      <h2 class="title">用户注册</h2>
      
      <form @submit.prevent="handleRegister">
        <!-- 用户名 -->
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="registerForm.username" 
            placeholder="请输入用户名 (唯一)" 
            required
          />
        </div>

        <!-- 邮箱 -->
        <div class="form-group">
          <label for="email">邮箱</label>
          <input 
            type="email" 
            id="email" 
            v-model="registerForm.email" 
            placeholder="请输入邮箱" 
            required
          />
        </div>

        <!-- 手机号 -->
        <div class="form-group">
          <label for="phone">手机号</label>
          <input 
            type="tel" 
            id="phone" 
            v-model="registerForm.phone" 
            placeholder="请输入手机号" 
            required
          />
        </div>

        <!-- 密码 -->
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="registerForm.password" 
            placeholder="请设置密码 (至少6位)" 
            minlength="6"
            required
          />
        </div>

        <!-- 确认密码 -->
        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="confirmPassword" 
            placeholder="请再次输入密码" 
            required
          />
        </div>

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="error-msg">
          {{ errorMessage }}
        </div>

        <!-- 成功提示 -->
        <div v-if="successMessage" class="success-msg">
          {{ successMessage }}
        </div>

        <button type="submit" :disabled="isLoading" class="register-btn">
          {{ isLoading ? '注册中...' : '立即注册' }}
        </button>
      </form>

      <div class="footer-links">
        <span>已有账号？</span>
        <router-link to="/login">去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { userApi } from '../utils/api';

const router = useRouter();

// 表单数据
const registerForm = reactive({
  username: '',
  password: '',
  email: '',
  phone: ''
});

// 确认密码字段（不提交给后端，仅用于前端校验）
const confirmPassword = ref('');

// 状态控制
const isLoading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

// 注册处理函数
const handleRegister = async () => {
  // 重置消息
  errorMessage.value = '';
  successMessage.value = '';

  // 1. 前端基础校验
  if (registerForm.password !== confirmPassword.value) {
    errorMessage.value = '两次输入的密码不一致';
    return;
  }

  if (registerForm.password.length < 6) {
    errorMessage.value = '密码长度不能少于6位';
    return;
  }

  isLoading.value = true;

  try {
    // 2. 调用后端注册接口
    const response = await userApi.post('/api/users/register', {
      username: registerForm.username,
      password: registerForm.password,
      email: registerForm.email,
      phone: registerForm.phone
    });

    // 3. 注册成功
    successMessage.value = '注册成功！即将跳转到登录页...';
    
    // 延迟跳转，让用户看到成功提示
    setTimeout(() => {
      router.push('/login');
    }, 1500);

  } catch (error) {
    console.error('Register error:', error);
    
    // 4. 处理错误信息
    if (error.response && error.response.data) {
      // 后端返回的具体错误消息，如 "用户名已存在"
      errorMessage.value = typeof error.response.data === 'string' 
        ? error.response.data 
        : '注册失败，请检查输入';
    } else {
      errorMessage.value = '网络错误，请稍后重试';
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.register-box {
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
  margin-bottom: 15px;
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
  box-sizing: border-box;
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
  background-color: #fff1f0;
  padding: 8px;
  border-radius: 4px;
}

.success-msg {
  color: #52c41a;
  font-size: 14px;
  margin-bottom: 15px;
  text-align: center;
  background-color: #f6ffed;
  padding: 8px;
  border-radius: 4px;
}

.register-btn {
  width: 100%;
  padding: 12px;
  background-color: #52c41a; /* 绿色代表注册/成功 */
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.register-btn:hover {
  background-color: #73d13d;
}

.register-btn:disabled {
  background-color: #b7eb8f;
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