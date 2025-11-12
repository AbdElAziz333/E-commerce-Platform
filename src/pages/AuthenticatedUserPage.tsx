import {useNavigate} from "react-router-dom";
import {logout} from "../services/authService.ts";
import {loginPageUrl} from "../routes/routes.tsx";
import {useAuth} from "../hooks/useAuth.ts";

export default function AuthenticatedUserPage() {
    const navigate = useNavigate();
    const { user: userData, loading } = useAuth();

    async function logoutUser() {
        try {
            await logout();
            navigate(loginPageUrl)
        } catch (err) {
            console.error(`Error logging out: ${err}`)
        }
    }

    if (loading) return <div>Loading...</div>;

    if (!userData) {
        navigate(loginPageUrl);
        return null;
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