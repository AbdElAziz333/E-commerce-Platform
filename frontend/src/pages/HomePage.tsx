import {Link} from "react-router-dom";
import {loginPageUrl, productsPageUrl, productsSearchPageUrl, signupPageUrl} from "../routes/routes.tsx";

export default function HomePage() {
    return (
        <div>
            <h1>Welcome to da Homepage!</h1>
            <Link to={signupPageUrl}>Signup</Link>
            <br />
            <Link to={loginPageUrl}>Login</Link>
            <br />
            <Link to={productsPageUrl}>All Products</Link>
            <br />
            <Link to={productsSearchPageUrl}>Search Products</Link>
        </div>
    )
}