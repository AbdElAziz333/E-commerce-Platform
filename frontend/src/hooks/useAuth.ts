import {useEffect, useState} from "react";
import {getCurrentUser} from "../services/userService.ts";
import type {CurrentUserDto} from "../types/userTypes.ts";

export function useAuth() {
    const [user, setUser] = useState<CurrentUserDto | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchUserData() {
            try {
                const currentUser = await getCurrentUser();
                setUser(currentUser);
            } catch (err) {
                setUser(null);
            } finally {
                setLoading(false);
            }
        }

        fetchUserData();
    }, [])

    return {user, loading, isAuthenticated: !!user}
}