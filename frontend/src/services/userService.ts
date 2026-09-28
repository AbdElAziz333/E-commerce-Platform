import {userServiceApi} from "./api.ts";
import type {CurrentUserDto, UserDto, UserUpdateRequest} from "../types/userTypes.ts";

export async function getCurrentUser(): Promise<CurrentUserDto> {
    return (await userServiceApi.get("/user/current")).data.data
}

export async function getAllUsers(): Promise<UserDto[]> {
    return (await userServiceApi.get("/users")).data.data
}

export async function getUserById(id: number): Promise<UserDto> {
    return (await userServiceApi.get(`/users/${id}`)).data.data
}

export async function updateUser(request: UserUpdateRequest) {
    return (await userServiceApi.patch("", request)).data.data
}

export async function deleteUser(id?: number) {
    const params: Record<string, any> = {}

    if (id) params.id = id

    try {
        return (await userServiceApi.delete("/", {params})).data
    } catch (err) {
        console.error("Failed to delete user: " + err)
        throw err;
    }
}