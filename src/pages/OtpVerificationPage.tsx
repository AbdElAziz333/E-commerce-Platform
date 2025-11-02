import {useEffect, useState} from "react";
import type {OtpRequest} from "../types/registrationTypes.ts";
import {verifyOtp} from "../services/registerService.ts";
import {useLocation, useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl, signupPageUrl} from "../routes/routes.tsx";

export default function OtpVerificationPage() {
    const location = useLocation();
    const navigate = useNavigate();

    const state = location.state as { verificationId?: string, email: string};

    const [formData, setFormData] = useState<OtpRequest>({
        verificationId: state?.verificationId || "",
        email: state?.email || "",
        otp: ""
    })

    useEffect(() => {
        if (!state?.verificationId || !state?.email) {
            console.error("No verificationId or email found — user must signup first.");
            navigate(signupPageUrl);
        }
    }, [state, navigate]);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();

        try {
            await verifyOtp(formData)
            // after OTP verification, JWT set in a cookie
            navigate(authenticatedUserPageUrl)
        } catch (err) {
            console.error(`Error adding user: ${err}`)
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <p>Please check your email and Enter the OTP</p>
                <input type="text" name="otp" value={formData.otp} onChange={(e) => setFormData(prev => ({...prev, otp: e.target.value}))} placeholder="Enter OTP" maxLength={6} required />

                <button type="submit">Verify OTP</button>
            </form>
        </div>
    )
}