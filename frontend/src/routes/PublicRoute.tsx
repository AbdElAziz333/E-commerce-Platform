import {useNavigate} from "react-router-dom";
import {useAuth} from "../hooks/useAuth.ts";
import {authenticatedUserPageUrl} from "./routes.tsx";
import type {ReactNode} from "react";

interface PublicRouteProps {
    children: ReactNode;
}

export function PublicRoute({ children }: PublicRouteProps) {
    const navigate = useNavigate()
    const { user, loading } = useAuth();

    if (loading) return <div>Loading...</div>;

    if (user) {
        navigate(authenticatedUserPageUrl);
        return;
    }

    return <>{children}</>;
}