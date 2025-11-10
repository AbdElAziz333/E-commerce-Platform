import type {LoginRequest} from "../types/loginTypes.ts";
import {login} from "../services/authService.ts";
import {useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl} from "../routes/routes.tsx";
import {type SubmitHandler, useForm} from "react-hook-form";

export default function LoginPage() {
    const navigate = useNavigate();
    const { register, handleSubmit } = useForm<LoginRequest>()

    const onSubmit: SubmitHandler<LoginRequest> = async (data: LoginRequest) => {
        try {
            await login(data)
            navigate(authenticatedUserPageUrl)
        } catch (err) {
            console.error("Error: ", err);
        }
    }

    return (
        <>
            <div>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <input type="email" placeholder="Enter email" {...register("email", {required: true})} />
                    <input type="password" placeholder="Enter password" {...register("password", {required: true})} />

                    <button type="submit">Login</button>
                </form>
            </div>
        </>
    )
}