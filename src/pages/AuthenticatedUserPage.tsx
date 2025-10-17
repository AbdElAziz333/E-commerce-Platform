import {useEffect, useState} from "react";
import type {UserDto} from "../types/userTypes.ts";
import {getCurrentUser} from "../services/userService.ts";
import {loginPageUrl} from "../routes/urls.ts";
import {useNavigate} from "react-router-dom";
import {logout} from "../services/authService.ts";

export default function AuthenticatedUserPage() {
    const navigate = useNavigate();

    const [userData, setUserData] = useState<UserDto>({
        id: 0,
        firstName: "",
        lastName: "",
        phoneNumber: "",
        preferredLanguage: "ARABIC",
        addresses: []
    });

    useEffect(() => {
        async function fetchUserData() {
            const currentUser = await getCurrentUser();
            setUserData(currentUser)
        }

        fetchUserData()
    }, []);

    async function logoutUser() {
        await logout();
        navigate(loginPageUrl)
    }

    return (
        <>
            <div>
                <p>Id: {userData.id}</p>
                <p>Firstname: {userData.firstName}</p>
                <p>Lastname: {userData.lastName}</p>
                <p>Phone Number: {userData.phoneNumber}</p>
                <p>Preferred Language: {userData.preferredLanguage}</p>
                <button onClick={logoutUser}>Logout</button>
            </div>
        </>
    )
}