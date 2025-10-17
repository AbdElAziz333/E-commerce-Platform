import {useEffect, useState} from "react";
import type {UserTypes} from "../types/userTypes.ts";
import {getUserByEmail, getUserById} from "../services/userService.ts";
import {useParams, useSearchParams} from "react-router-dom";

export default function UserPage() {
    const {userId} = useParams()
    const [searchParams] = useSearchParams()
    const email = searchParams.get("email")

    const [userData, setUserData] = useState<UserTypes>({
        id: 0,
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        phoneNumber: "",
        preferredLanguage: "ARABIC"
    })

    useEffect(() => {
        async function fetchUserData() {
            try {
                if (userId) {
                    setUserData(await getUserById(Number(userId)))
                } else if (email) {
                    setUserData(await getUserByEmail(email))
                } else {
                    console.error("No ID or email provided in the URL.")
                }
            } catch (err) {
                console.error(`Error fetching user: ${err}`)
            }
        }

        fetchUserData()
    }, [userId, email]);

    return(
        <div>
            <h3>ID: {userData.id}</h3>
            <h3>Firstname: {userData.firstName}</h3>
            <h3>Lastname: {userData.lastName}</h3>
            <h3>Email: {userData.email}</h3>
            <h3>PhoneNumber: {userData.phoneNumber}</h3>
        </div>
    )
}