import axios from 'axios';

const userApi = axios.create({ baseURL: import.meta.env.VITE_USER_API });
const productApi = axios.create({ baseURL: import.meta.env.VITE_PRODUCT_API });
const orderApi = axios.create({ baseURL: import.meta.env.VITE_ORDER_API });

export { userApi, productApi, orderApi };