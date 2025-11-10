import type {LoginRequest} from "../types/loginTypes.ts";
import {login} from "../services/authService.ts";
import {useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl} from "../routes/routes.tsx";
import {type SubmitHandler, useForm} from "react-hook-form";

export default function LoginPage() {
    const navigate = useNavigate();
    const { register, handleSubmit, formState: {errors} } = useForm<LoginRequest>()

    const onSubmit: SubmitHandler<LoginRequest> = async (data) => {
        try {
            await login(data)
            navigate(authenticatedUserPageUrl)
        } catch (err) {
            console.error("Login error: ", err);
        }
    }

    return (
        <>
            <div>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <input type="email" placeholder="Enter email" {
                        ...register("email", {
                            required: "Email is required",
                            pattern: {
                                value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                                message: "Invalid email format",
                            },
                        })} />

                    {errors.email && <div>{errors.email.message}</div>}

                    <input type="password" placeholder="Enter password" {
                        ...register("password", {
                            required: "Password is required",
                            minLength: {
                                value: 8,
                                message: "Password must be at least 8 characters"
                            }
                        })} />

                    {errors.password && <div>{errors.password.message}</div>}

                    <button type="submit">Login</button>
                </form>
            </div>
        </>
    )
}