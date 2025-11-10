import {useEffect} from "react";
import type {OtpRequest} from "../types/registrationTypes.ts";
import {verifyOtp} from "../services/registerService.ts";
import {useLocation, useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl, signupPageUrl} from "../routes/routes.tsx";
import {type SubmitHandler, useForm} from "react-hook-form";

export default function OtpVerificationPage() {
    const location = useLocation();
    const navigate = useNavigate();

    const state = location.state as { verificationId?: string, email: string};

    const {register, handleSubmit, setValue} = useForm<OtpRequest>();

    useEffect(() => {
        if (!state?.verificationId || !state?.email) {
            console.error("No verificationId or email found — user must signup first.");
            navigate(signupPageUrl);
        } else {
            setValue("verificationId", state.verificationId);
            setValue("email", state.email);
        }
    }, [state, navigate, setValue]);

    const onSubmit: SubmitHandler<OtpRequest> = async (data: OtpRequest) => {
        try {
            await verifyOtp(data)
            // after OTP verification, JWT set in a cookie
            navigate(authenticatedUserPageUrl)
        } catch (err) {
            console.error(`Error adding user: ${err}`)
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <p>Please check your email and Enter the OTP</p>
                <input type="text" placeholder="Enter OTP" maxLength={6} {...register("otp", {required: true})} />
                <input type="hidden" {...register("verificationId")} />
                <input type="hidden" {...register("email")} />

                <button type="submit">Verify OTP</button>
            </form>
        </div>
    )
}