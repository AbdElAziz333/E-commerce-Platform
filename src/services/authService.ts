import {loginServiceApi} from "./api.ts";
import type {LoginRequest} from "../types/loginTypes.ts";

export async function login(data: LoginRequest) {
    return (await loginServiceApi.post("", data)).data.data
}

export async function logout() {
    return (await loginServiceApi.post("/logout")).data.data
}