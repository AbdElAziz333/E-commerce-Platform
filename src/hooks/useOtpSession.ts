import {useNavigate} from "react-router-dom";
import type {OtpData} from "../types/registrationTypes.ts";

const OTP_SESSION_KEY = "otpData";

export function useOtpSession() {
    const navigate = useNavigate();

    function saveOtpData(data: OtpData): void {
        sessionStorage.setItem(OTP_SESSION_KEY, JSON.stringify(data));
    }

    function getOtpData(): OtpData | null {
        try {
            const storedOtp = sessionStorage.getItem(OTP_SESSION_KEY);

            if (!storedOtp) {
                return null;
            }

            const parsed = JSON.parse(storedOtp);

            if (!parsed.verificationId || !parsed.email) {
                return null;
            }

            return parsed as OtpData;
        } catch (err) {
            return null;
        }
    }

    function clearOtpData() {
        sessionStorage.removeItem(OTP_SESSION_KEY);
    }

    function ensureOtpData(redirectTo: string): OtpData | null {
        const otpData = getOtpData();

        if (!otpData) {
            navigate(redirectTo);
        }

        return otpData;
    }

    return {saveOtpData, getOtpData, clearOtpData, ensureOtpData}
}