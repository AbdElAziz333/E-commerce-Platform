import {signup} from "../services/registerService.ts";
import {useNavigate} from "react-router-dom";
import {otpVerificationPageUrl} from "../routes/routes.tsx";
import type {SignupRequest} from "../types/registrationTypes.ts";
import {type SubmitHandler, useForm} from "react-hook-form";

export default function SignupPage() {
    const navigate = useNavigate()
    const {register, handleSubmit, formState: {errors}} = useForm<SignupRequest>();

    const onSubmit: SubmitHandler<SignupRequest> = async (data: SignupRequest) => {
        try {
            const verificationId = await signup(data);
            navigate(otpVerificationPageUrl, {state: {verificationId, email: data.email}})
        } catch (err) {
            console.error(`Error adding user: ${err}`)
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <input type="text" placeholder="Firstname" {...register("firstName", {required: "Firstname is required"})} />
                {errors.firstName && <div>{errors.firstName.message}</div>}

                <input type="text" placeholder="Lastname" {...register("lastName", {required: "Lastname is required"})} />
                {errors.lastName && <div>{errors.lastName.message}</div>}

                <input type="email" placeholder="Email" {...register("email", {required: "Email is required"})} />
                {errors.email && <div>{errors.email.message}</div>}

                <input type="password" placeholder="Password" {...register("password", {required: "Password is required"})} />
                {errors.password && <div>{errors.password.message}</div>}

                <input type="tel" placeholder="Phone Number" {...register("phoneNumber", {required: "Phone Number is required"})} />
                {errors.phoneNumber && <div>{errors.phoneNumber.message}</div>}

                <button type="submit">Signup</button>
            </form>
        </div>
    )
}