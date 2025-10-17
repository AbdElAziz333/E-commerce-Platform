import axios from "axios";

const USERS_SERVICE_URL = import.meta.env.VITE_USERS_SERVICE_URL;

export const userServiceApi = axios.create({
    baseURL: `${USERS_SERVICE_URL}/api/v1/users`,
    withCredentials: true
})

export const registrationServiceApi = axios.create({
    baseURL: `${USERS_SERVICE_URL}/api/v1/registration`,
    withCredentials: true
})

export const loginServiceApi = axios.create({
    baseURL: `${USERS_SERVICE_URL}/api/v1/login`,
    withCredentials: true
})