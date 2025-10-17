import type {OtpRequest, SignupRequest} from "../types/registrationTypes.ts";
import {registrationServiceApi} from "./api.ts";

export async function signup(request: SignupRequest) {
    return (await registrationServiceApi.post("", request)).data.data
}

export async function verifyOtp(request: OtpRequest) {
    return (await registrationServiceApi.post("/verify-otp", request)).data.data
}