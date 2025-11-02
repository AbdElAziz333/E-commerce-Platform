import {userServiceApi} from "./api.ts";
import type {CurrentUserDto, UserDto, UserUpdateRequest} from "../types/userTypes.ts";

export async function getCurrentUser(): Promise<CurrentUserDto> {
    // maybe we should try /api/v1/user/current
    return (await userServiceApi.get("/user/current")).data.data
}

export async function getAllUsers(): Promise<UserDto[]> {
    return (await userServiceApi.get("/users")).data.data //users
}

export async function getUserById(id: number): Promise<UserDto> {
    return (await userServiceApi.get(`/users/${id}`)).data.data //users/id
}

// export async function addUser(request: UserRegisterRequest) {
//     return (await userServiceApi.post("", request)).data.data
// }

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