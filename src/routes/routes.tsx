import {createBrowserRouter} from "react-router-dom";
import NotFoundPage from "../pages/NotFoundPage.tsx";
import HomePage from "../pages/HomePage.tsx";
import UsersPage from "../pages/UsersPage.tsx";
import UserPage from "../pages/UserPage.tsx";
import OtpVerificationPage from "../pages/OtpVerificationPage.tsx";
import AuthenticatedUserPage from "../pages/AuthenticatedUserPage.tsx";
import LoginPage from "../pages/LoginPage.tsx";
import SignupPage from "../pages/SignupPage.tsx";
import {PublicRoute} from "./PublicRoute.tsx";
import {ProtectedRoute} from "./ProtectedRoute.tsx";
import ProductsPage from "../pages/ProductsPage.tsx";
import ProductPage from "../pages/ProductPage.tsx";
import ProductCreationPage from "../pages/ProductCreationPage.tsx";
import SearchProductsPage from "../pages/SearchProductsPage.tsx";
import AuthenticatedUserProductsPage from "../pages/AuthenticatedUserProductsPage.tsx";
import CartPage from "../pages/CartPage.tsx";
import OrdersPage from "../pages/OrdersPage.tsx";

export const homePageUrl = "/";
export const usersPageUrl = "/users";
export const signupPageUrl = "/signup";
export const otpVerificationPageUrl = "/signup/verify-otp";
export const loginPageUrl = "/login";
export const authenticatedUserPageUrl = "/user/my-profile";
export const productsPageUrl = "/products";
export const productCreationPageUrl = "/products/create";
export const authenticatedUserProductsPageUrl = "/products/my-products";
export const productsSearchPageUrl = "/products/search";
export const cartPageUrl = "/cart";
export const ordersPageUrl = "/orders";

export const router = createBrowserRouter([
    {
        path: homePageUrl,
        element: (
            <PublicRoute>
                <HomePage />
            </PublicRoute>
        ),
        errorElement: <NotFoundPage />

    },
    {
        path: signupPageUrl,
        element: (
            <PublicRoute>
                <SignupPage />
            </PublicRoute>
        ),
        errorElement: <NotFoundPage />
    },
    {
        path: otpVerificationPageUrl,
        element: (
            <PublicRoute>
                <OtpVerificationPage />
            </PublicRoute>
        ),
        errorElement: <NotFoundPage />
    },
    {
        path: loginPageUrl,
        element: (
            <PublicRoute>
                <LoginPage />
            </PublicRoute>
        ),
        errorElement: <NotFoundPage />
    },
    {
        path: authenticatedUserPageUrl,
        element: (
            <ProtectedRoute>
                <AuthenticatedUserPage />
            </ProtectedRoute>
        ),
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
        path: productsPageUrl,
        element: <ProductsPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: "/product/:slug",
        element: <ProductPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: productCreationPageUrl,
        element: <ProductCreationPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: productsSearchPageUrl,
        element: <SearchProductsPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: authenticatedUserProductsPageUrl,
        element: <AuthenticatedUserProductsPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: cartPageUrl,
        element: <CartPage />,
        errorElement: <NotFoundPage />
    },
    {
        path: ordersPageUrl,
        element: <OrdersPage />,
        errorElement: <NotFoundPage />
    }
]);