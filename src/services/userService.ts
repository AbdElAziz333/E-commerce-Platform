import {userServiceApi} from "./api.ts";
import type {UserRegisterRequest, UserUpdateRequest} from "../types/userTypes.ts";

export async function getCurrentUser() {
    return (await userServiceApi.get("/current")).data.data
}

export async function getAllUsers() {
    return (await userServiceApi.get("")).data.data
}

export async function getUserById(id: number) {
    return (await userServiceApi.get(`/${id}`)).data.data
}

export async function getUserByEmail(email: string) {
    return (await userServiceApi.get("", {
        params: {email}
    })).data.data
}

export async function addUser(request: UserRegisterRequest) {
    return (await userServiceApi.post("", request)).data.data
}

export async function updateUser(request: UserUpdateRequest) {
    return (await userServiceApi.patch("", request)).data.data
}

export async function deleteUser(id?: number, email?: string) {
    if (!id && !email) {
        throw new Error("Either id or email should be provided.")
    }

    const params: Record<string, any> = {}

    if (id) params.id = id
    if (email) params.email = email

    try {
        return (await userServiceApi.delete("/", {params})).data
    } catch (err) {
        console.error("Failed to delete user: " + err)
        throw err;
    }
}