import { createRoot } from 'react-dom/client'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import HomePage from "./pages/HomePage.tsx";
import NotFoundPage from "./pages/NotFoundPage.tsx";
import UserRegistrationPage from "./pages/SignupPage.tsx";
import UsersPage from "./pages/UsersPage.tsx";
import UserPage from "./pages/UserPage.tsx";
import OtpVerificationPage from "./pages/OtpVerificationPage.tsx";
import {
    authenticatedUserPageUrl,
    homePageUrl,
    loginPageUrl,
    otpVerificationPageUrl,
    signupPageUrl,
    usersPageUrl
} from "./routes/urls.ts";
import LoginPage from "./pages/LoginPage.tsx";
import AuthenticatedUserPage from "./pages/AuthenticatedUserPage.tsx";

const router = createBrowserRouter([
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
        element: <UserRegistrationPage />,
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

createRoot(document.getElementById('root')!).render(
    <RouterProvider router={router}></RouterProvider>
)