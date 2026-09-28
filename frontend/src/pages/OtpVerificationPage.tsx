import {useEffect} from "react";
import type {OtpRequest} from "../types/registrationTypes.ts";
import {verifyOtp} from "../services/registerService.ts";
import {useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl, signupPageUrl} from "../routes/routes.tsx";
import {type SubmitHandler, useForm} from "react-hook-form";
import {useOtpSession} from "../hooks/useOtpSession.ts";

export default function OtpVerificationPage() {
    const navigate = useNavigate();
    const { ensureOtpData, clearOtpData } = useOtpSession();

    const {register, handleSubmit, formState: {errors} ,setValue} = useForm<OtpRequest>();

    useEffect(() => {
        const otpData = ensureOtpData(signupPageUrl);

        if (otpData) {
            setValue("verificationId", otpData.verificationId);
            setValue("email", otpData.email);
        }

    }, [navigate, setValue]);

    const onSubmit: SubmitHandler<OtpRequest> = async (data) => {
        try {
            await verifyOtp(data)
            clearOtpData();
            navigate(authenticatedUserPageUrl)
        } catch (err) {
            console.error(`Error verifying OTP: ${err}`)
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <p>Please check your email and Enter the OTP</p>
                <input type="text" placeholder="Enter OTP" maxLength={6} {
                    ...register("otp", {
                        required: "OTP is required",
                        minLength: {value: 6, message: "OTP must be 6 digits"},
                        maxLength: {value: 6, message: "OTP must be 6 digits"}
                    })} />
                {errors.otp && <div>{errors.otp.message}</div>}

                <input type="hidden" {...register("verificationId")} />
                <input type="hidden" {...register("email")} />

                <button type="submit">Verify OTP</button>
            </form>
        </div>
    )
}