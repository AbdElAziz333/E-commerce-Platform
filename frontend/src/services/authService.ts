import {loginServiceApi} from "./api.ts";
import type {LoginRequest} from "../types/loginTypes.ts";

export async function login(data: LoginRequest): Promise<string> {
    // Returns JWT token (though it's also in HttpOnly cookie)
    return (await loginServiceApi.post("/login", data)).data.data
}

export async function logout(): Promise<void> {
    await loginServiceApi.post("/logout")
}