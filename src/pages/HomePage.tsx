import {Link} from "react-router-dom";
import {signupPageUrl} from "../routes/urls.ts";

export default function HomePage() {
    return (
        <div>
            <h1>Welcome to da Homepage!</h1>
            <Link to={signupPageUrl}>Signup</Link>
        </div>
    )
}