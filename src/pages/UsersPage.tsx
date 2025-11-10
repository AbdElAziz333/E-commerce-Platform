import {useEffect, useState} from "react";
import {getAllUsers} from "../services/userService.ts";
import type {UserDto} from "../types/userTypes.ts";
import {Link} from "react-router-dom";

export default function UsersPage() {
    const [users, setUsers] = useState<UserDto[]>([])

    useEffect(() => {
        async function fetchAllUsers() {
            try {
                setUsers(await getAllUsers())
            } catch (err) {
                console.error("Error fetching all users: " + err)
            }
        }

        fetchAllUsers()
    }, [])

    return(
        <div>
            <h1>All users</h1>
            <ul>
                {users.map((user) => (
                    <li key={user.id}>
                        <Link to={`/user/${user.id}`}>
                            {user.firstName} {user.lastName}
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    )
}