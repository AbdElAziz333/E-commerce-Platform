import {useState} from "react";
import type {LoginRequest} from "../types/loginTypes.ts";
import {login} from "../services/authService.ts";
import * as React from "react";
import {useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl} from "../routes/urls.ts";

export default function LoginPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState<LoginRequest>({
        email: "",
        password: ""
    })

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = e.target;
        setFormData(prev => ({...prev, [name]: value}))
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()

        try {
            await login(formData)
            navigate(authenticatedUserPageUrl)
        } catch (err) {
            console.error("Error: ", err)
        }
    }

    return (
        <>
            <div>
                <form onSubmit={handleSubmit}>
                    <input type="email" name="email" value={formData.email} placeholder="Enter email" onChange={handleChange} required />
                    <input type="password" name="password" value={formData.password} placeholder="Enter password" onChange={handleChange} required />

                    <button type="submit">Login</button>
                </form>
            </div>
        </>
    )
}