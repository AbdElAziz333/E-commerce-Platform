import type {ReactNode} from "react";
import {useAuth} from "../hooks/useAuth.ts";
import {useNavigate} from "react-router-dom";
import {homePageUrl} from "./routes.tsx";

interface ProtectRouteProps {
    children: ReactNode;
}

export function ProtectedRoute({children}: ProtectRouteProps) {
    const navigate = useNavigate();
    const {user, loading} = useAuth();

    if (loading) return <div>Loading...</div>

    if (!user) {
        navigate(homePageUrl);
        return;
    }

    return <>{children}</>
}