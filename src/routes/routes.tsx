import {createBrowserRouter} from "react-router-dom";
import NotFoundPage from "../pages/NotFoundPage.tsx";
import HomePage from "../pages/HomePage.tsx";
import UsersPage from "../pages/UsersPage.tsx";
import UserPage from "../pages/UserPage.tsx";
import OtpVerificationPage from "../pages/OtpVerificationPage.tsx";
import AuthenticatedUserPage from "../pages/AuthenticatedUserPage.tsx";
import LoginPage from "../pages/LoginPage.tsx";
import SignupPage from "../pages/SignupPage.tsx";

export const homePageUrl = "/"
export const usersPageUrl = "/users"
export const signupPageUrl = "/signup"
export const otpVerificationPageUrl = "/signup/verify-otp"
export const loginPageUrl = "/login"
export const authenticatedUserPageUrl = "/user/my-profile"

export const router = createBrowserRouter([
    {
        path: homePageUrl,
        element: <HomePage />,
        errorElement: <NotFoundPage />

    },
    {
        path: usersPageUrl,
        element: <UsersPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: "/user/:userId?",
        element: <UserPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: signupPageUrl,
        element: <SignupPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: otpVerificationPageUrl,
        element: <OtpVerificationPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: loginPageUrl,
        element: <LoginPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: authenticatedUserPageUrl,
        element: <AuthenticatedUserPage />,
        errorElement: <NotFoundPage />
    }
])