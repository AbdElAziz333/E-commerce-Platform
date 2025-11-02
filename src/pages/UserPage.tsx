import {useEffect, useState} from "react";
import type {UserDto} from "../types/userTypes.ts";
import {getUserById} from "../services/userService.ts";
import {useParams} from "react-router-dom";

export default function UserPage() {
    const {userId} = useParams()

    const [userData, setUserData] = useState<UserDto>({
        id: 0,
        firstName: "",
        lastName: ""
    })

    useEffect(() => {
        async function fetchUserData() {
            try {
                setUserData(await getUserById(Number(userId)))
            } catch (err) {
                console.error(`Error fetching user: ${err}`)
            }
        }

        fetchUserData()
    }, [userId]);

    return(
        <div>
            <h3>ID: {userData.id}</h3>
            <h3>Firstname: {userData.firstName}</h3>
            <h3>Lastname: {userData.lastName}</h3>
        </div>
    )
}