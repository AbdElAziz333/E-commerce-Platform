import axios from "axios";

const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL;

export const userServiceApi = axios.create({
    baseURL: `${API_GATEWAY_URL}/api/v1`,
    withCredentials: true
})

export const registrationServiceApi = axios.create({
    baseURL: `${API_GATEWAY_URL}/api/v1/registration`,
    withCredentials: true
})

export const loginServiceApi = axios.create({
    baseURL: `${API_GATEWAY_URL}/api/v1/auth`,
    withCredentials: true
})

export const productServiceApi = axios.create({
    baseURL: `${API_GATEWAY_URL}/api/v1/products`,
    withCredentials: true
});

export const cartServiceApi = axios.create({
    baseURL: `${API_GATEWAY_URL}/api/v1/carts`,
    withCredentials: true
});

export const orderServiceApi = axios.create({
    baseURL: `${API_GATEWAY_URL}/api/v1/orders`,
    withCredentials: true
});