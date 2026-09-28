import {signup} from "../services/registerService.ts";
import {Link, useNavigate} from "react-router-dom";
import {homePageUrl, loginPageUrl, otpVerificationPageUrl} from "../routes/routes.tsx";
import type {SignupRequest} from "../types/registrationTypes.ts";
import {type SubmitHandler, useForm} from "react-hook-form";
import {useOtpSession} from "../hooks/useOtpSession.ts";

export default function SignupPage() {
    const navigate = useNavigate()
    const { saveOtpData } = useOtpSession();
    const {register, handleSubmit, formState: {errors}} = useForm<SignupRequest>();

    const onSubmit: SubmitHandler<SignupRequest> = async (data) => {
        try {
            const verificationId = await signup(data);
            saveOtpData({verificationId, email: data.email})
            navigate(otpVerificationPageUrl)
        } catch (err) {
            console.error(`Signup Error: ${err}`)
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <input type="text" placeholder="Firstname" {
                    ...register("firstName", {
                        required: "Firstname is required",
                        maxLength: {
                            value: 30,
                            message: "Firstname length must be less than 30 characters"
                        }
                    })} />

                {errors.firstName && <div>{errors.firstName.message}</div>}

                <input type="text" placeholder="Lastname" {
                    ...register("lastName", {
                        required: "Lastname is required",
                        maxLength: {
                            value: 30,
                            message: "Lastname length must be less than 30 characters"
                        }
                    })} />

                {errors.lastName && <div>{errors.lastName.message}</div>}

                <input type="email" placeholder="Email" {
                    ...register("email", {
                        required: "Email is required",
                        validate: (value) => {
                            if (!value.includes("@")) {
                                return "Email should be valid";
                            }

                            return true;
                        }
                    })} />

                {errors.email && <div>{errors.email.message}</div>}

                <input type="password" placeholder="Password" {
                    ...register("password", {
                        required: "Password is required",
                        minLength: {
                            value: 8,
                            message: "Password must be at least 8 characters"
                        }
                    })} />

                {errors.password && <div>{errors.password.message}</div>}

                <input type="tel" placeholder="Phone Number" {
                    ...register("phoneNumber", {
                        required: "Phone Number is required",
                        pattern: {
                            value: /^[0-9]{10,12}$/,
                            message: "Phone Number should be valid"
                        }
                    })} />

                {errors.phoneNumber && <div>{errors.phoneNumber.message}</div>}

                <button type="submit">Signup</button>
            </form>
            <br />
            <Link to={loginPageUrl}>have an account? login!</Link>
            <br />
            <Link to={homePageUrl}>Home page</Link>
        </div>
    )
}