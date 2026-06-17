<template>
  <div>
    <h2>商品中心</h2>
    <button @click="fetchProducts">刷新商品</button>
    <ul v-if="products.length">
      <li v-for="p in products" :key="p.id">
        {{ p.name }} - ¥{{ p.price }} (库存: {{ p.stock }})
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { productApi } from '../utils/api';

const products = ref([]);

const fetchProducts = async () => {
  try {
    const res = await productApi.get('/api/products');
    products.value = res.data;
  } catch (e) {
    alert('获取商品失败');
  }
};

// 页面加载时自动获取
fetchProducts();
</script>