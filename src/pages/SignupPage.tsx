import {signup} from "../services/registerService.ts";
import {useNavigate} from "react-router-dom";
import {otpVerificationPageUrl} from "../routes/routes.tsx";
import type {SignupRequest} from "../types/registrationTypes.ts";
import {type SubmitHandler, useForm} from "react-hook-form";

export default function SignupPage() {
    const navigate = useNavigate()
    const {register, handleSubmit} = useForm<SignupRequest>();

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
                <input type="text" placeholder="Firstname" {...register("firstName", {required: true})} />
                <input type="text" placeholder="Lastname" {...register("lastName", {required: true})} />
                <input type="email" placeholder="Email" {...register("email", {required: true})} />
                <input type="password" placeholder="Password" {...register("password", {required: true})} />
                <input type="tel" placeholder="Phone Number" {...register("phoneNumber", {required: true})} />

                <button type="submit">Signup</button>
            </form>
        </div>
    )
}