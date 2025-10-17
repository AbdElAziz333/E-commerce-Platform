import type {PreferredLanguage} from "./commonTypes.ts";
import type {AddressDto} from "./addressTypes.ts";

export interface UserTypes {
    id: number
    firstName: string
    lastName: string
    email: string
    password: string
    phoneNumber: string
    preferredLanguage: PreferredLanguage
}

export interface UserDto {
    id: number
    firstName: string
    lastName: string
    phoneNumber: string
    preferredLanguage: PreferredLanguage
    addresses: AddressDto[]
}

export interface UserRegisterRequest {
    firstName: string
    lastName: string
    email: string
    password: string
    phoneNumber: string
}

export interface UserUpdateRequest {
    firstName: string
    lastName: string
    password: string
    phoneNumber: string
}