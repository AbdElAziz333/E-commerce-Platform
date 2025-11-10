import {useEffect, useState} from "react";
import type {CurrentUserDto} from "../types/userTypes.ts";
import {useNavigate} from "react-router-dom";
import {logout} from "../services/authService.ts";
import {getCurrentUser} from "../services/userService.ts";
import {loginPageUrl} from "../routes/routes.tsx";

export default function AuthenticatedUserPage() {
    const navigate = useNavigate();

    const [userData, setUserData] = useState<CurrentUserDto | null>(null);

    useEffect(() => {
        async function fetchUserData() {
            try {
                const currentUser = await getCurrentUser();
                setUserData(currentUser)
            } catch (err) {
                console.error(`Error fetching user: ${err}`)
                // Redirect to login if unauthorized
                navigate(loginPageUrl)
            }
        }

        fetchUserData()
    }, [navigate]);

    async function logoutUser() {
        try {
            await logout();
            navigate(loginPageUrl)
        } catch (err) {
            console.error(`Error logging out: ${err}`)
        }
    }

    return (
        <>
            <div>
                <h1>My Profile</h1>
                <p>Id: {userData?.id}</p>
                <p>Firstname: {userData?.firstName}</p>
                <p>Lastname: {userData?.lastName}</p>
                <p>Email: {userData?.email}</p>
                <p>Phone Number: {userData?.phoneNumber}</p>
                <p>Preferred Language: {userData?.preferredLanguage}</p>

                {userData?.addresses && userData.addresses.length > 0 && (
                    <div>
                        <h3>Addresses:</h3>
                        <ul>
                            {userData.addresses.map((address) => (
                                <li key={address.addressId}>
                                    {address.streetLine1}, {address.city}, {address.postalCode}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                <button onClick={logoutUser}>Logout</button>
            </div>
        </>
    )
}