import {signup} from "../services/registerService.ts";
import {useNavigate} from "react-router-dom";
import {otpVerificationPageUrl} from "../routes/urls.ts";
import {useState} from "react";
import type {SignupRequest} from "../types/registrationTypes.ts";

export default function SignupPage() {
    const [formData, setFormData] = useState<SignupRequest>({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        phoneNumber: ""
    })

    const navigate = useNavigate()

    function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    }

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();

        try {
            const verificationId = await signup(formData)
            navigate(otpVerificationPageUrl, {state: {verificationId}})
        } catch (err) {
            console.error(`Error adding user: ${err}`)
        }
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input type="text" name="firstName" value={formData.firstName} placeholder="Firstname" onChange={handleChange} required />
                <input type="text" name="lastName" value={formData.lastName} placeholder="Lastname" onChange={handleChange} required />
                <input type="email" name="email" value={formData.email} placeholder="Email" onChange={handleChange} required />
                <input type="password" name="password" value={formData.password} placeholder="Password" onChange={handleChange} required />
                <input type="tel" name="phoneNumber" value={formData.phoneNumber} placeholder="Phone Number" onChange={handleChange} required />

                <button type="submit">Signup</button>
            </form>
        </div>
    )
}