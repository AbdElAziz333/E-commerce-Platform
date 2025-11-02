import {Link} from "react-router-dom";
import {createProductPageUrl, loginPageUrl, productsPageUrl, signupPageUrl} from "../routes/routes.tsx";

export default function HomePage() {
    return (
        <div>
            <h1>Welcome to da Homepage!</h1>
            <Link to={signupPageUrl}>Signup</Link>
            <br />
            <Link to={loginPageUrl}>Login</Link>
            <br />
            <Link to={productsPageUrl}>Products</Link>
            <br />
            <Link to={createProductPageUrl}>Create Product</Link>
        </div>
    )
}