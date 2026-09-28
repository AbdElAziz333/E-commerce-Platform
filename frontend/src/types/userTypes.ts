import type {PreferredLanguage} from "./commonTypes.ts";
import type {AddressDto} from "./addressTypes.ts";

export interface CurrentUserDto {
    userId: number
    firstName: string
    lastName: string
    email: string
    phoneNumber: string
    preferredLanguage: PreferredLanguage
    addresses: AddressDto[]
}

export interface UserDto {
    userId: number
    firstName: string
    lastName: string
}

export interface UserUpdateRequest {
    userId: number
    firstName: string
    lastName: string
    password: string
    phoneNumber: string
}