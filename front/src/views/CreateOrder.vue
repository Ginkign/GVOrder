<template>
  <div>
    <h2>创建订单</h2>
    <button @click="submitOrder">立即购买 (iPhone 15)</button>
  </div>
</template>

<script setup>
import { orderApi } from '../utils/api';

const submitOrder = async () => {
  try {
    const payload = {
      userId: 1,       // 需先注册并登录获取真实ID
      addressId: 1,    // 需先添加地址获取真实ID
      items: [
        { productId: 1, quantity: 1 }
      ]
    };
    const res = await orderApi.post('/api/orders', payload);
    alert('下单成功！订单号: ' + res.data.id);
  } catch (e) {
    alert('下单失败: ' + (e.response?.data || e.message));
  }
};
</script>