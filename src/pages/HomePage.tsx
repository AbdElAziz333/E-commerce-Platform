import {Link} from "react-router-dom";
import {loginPageUrl, signupPageUrl} from "../routes/routes.tsx";

export default function HomePage() {
    return (
        <div>
            <h1>Welcome to da Homepage!</h1>
            <Link to={signupPageUrl}>Signup</Link>
            <br />
            <Link to={loginPageUrl}>Login</Link>
        </div>
    )
}