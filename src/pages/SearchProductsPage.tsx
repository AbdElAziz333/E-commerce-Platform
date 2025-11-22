import {useState} from "react";
import type {ProductDto} from "../types/productTypes.ts";
import {searchProducts} from "../services/productService.ts";
import {Link} from "react-router-dom";

export default function SearchProductsPage() {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState<ProductDto[]>([]);
    const [loading, setLoading] = useState(false);

    async function handleSearch() {
        if (!query.trim()) return;

        setLoading(true);

        try {
            const data = await searchProducts(query);
            setResults(data);
        } catch (e) {
            console.error("Search error:", e);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <h1>Search Products</h1>

            <input
                type="text"
                value={query}
                placeholder="Search products..."
                onChange={(e) => setQuery(e.target.value)}
            />
            <button onClick={handleSearch}>Search</button>

            {loading && <p>Searching...</p>}

            <ul>
                {results.map(product => (
                    <li key={product.productId}>
                        <Link to={`/product/${product.slug}`}>
                            {product.name} - {product.shortDescription} - ${product.price}
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
}