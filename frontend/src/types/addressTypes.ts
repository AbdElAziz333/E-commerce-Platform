import type {City} from "./commonTypes.ts";

export interface Address {
    addressId: number
    streetLine1: string,
    streetLine2?: string
    city: City
    state: string
    postalCode: string
    isDefaultShipping: boolean
    createdAt: Date
    lastModifiedAt: Date
    userId: number
}

export interface AddressDto {
    addressId: number
    streetLine1: string,
    streetLine2?: string
    city: City
    state: string
    postalCode: string
    isDefaultShipping: boolean
}

export interface AddressRegisterRequest {
    streetLine1: string
    streetLine2: string
    city: City
    state: string
    postalCode: string
    isDefaultShipping: boolean
}

export interface AddressUpdateRequest {
    streetLine1: string
    streetLine2: string
    city: City
    state: string
    postalCode: string
    isDefaultShipping: boolean
}