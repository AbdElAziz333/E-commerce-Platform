import type {OtpRequest, SignupRequest} from "../types/registrationTypes.ts";
import {registrationServiceApi} from "./api.ts";

export async function signup(request: SignupRequest): Promise<string> {
    // return verificationId
    return (await registrationServiceApi.post("", request)).data.data
}

export async function verifyOtp(request: OtpRequest): Promise<void> {
    // returns nothing, but sets JWT cookie
    await registrationServiceApi.post("/verify-otp", request)
}