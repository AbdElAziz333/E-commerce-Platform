import {type SubmitHandler, useForm} from "react-hook-form";
import type {ProductCreationRequest} from "../types/productTypes.ts";
import {createProduct} from "../services/productService.ts";
import {useState} from "react";
import {Link} from "react-router-dom";
import {authenticatedUserPageUrl} from "../routes/routes.tsx";

export default function ProductCreationPage() {
    const {register, handleSubmit, formState: {errors}, reset} = useForm<ProductCreationRequest>()
    const [isLoading, setLoading] = useState(false);
    const [variantInput, setVariantInput] = useState("")
    const [variantAttributes, setVariantAttributes] = useState<string[]>([])

    const onSubmit: SubmitHandler<ProductCreationRequest> = async (data) => {
        try {
            setLoading(true);
            await createProduct({ ...data, variantAttributes });
            reset();
            setVariantAttributes([]);
            setVariantInput("");
            alert("Product created successfully!");
        } catch (err) {
            console.error("Product Creation Error:", err);
            alert("Failed to create product.");
        } finally {
            setLoading(false);
        }
    };

    const handleAddVariant = () => {
        if (variantInput.trim() && !variantAttributes.includes(variantInput.trim())) {
            setVariantAttributes([...variantAttributes, variantInput.trim()]);
            setVariantInput("");
        }
    };

    const handleRemoveVariant = (variant: string) => {
        setVariantAttributes(variantAttributes.filter(v => v !== variant));
    };

    return (
        <div>
            <h1>Create Product</h1>
            <form onSubmit={handleSubmit(onSubmit)}>
                {}

                <input
                    type="text"
                    placeholder="Product Name"
                    {...register("name", { required: "Product name is required", maxLength: { value: 300, message: "Max 300 characters" } })}
                />
                {errors.name && errors.name.message}

                <textarea
                    placeholder="Product Description"
                    {...register("description", { required: "Description is required", maxLength: { value: 30000, message: "Max 30000 characters" } })}
                />
                {errors.description && errors.description.message}

                <textarea
                    placeholder="Product Short Description"
                    {...register("shortDescription", { required: "Short description is required", maxLength: { value: 3000, message: "Max 3000 characters" } })}
                />
                {errors.shortDescription && errors.shortDescription.message}

                <input
                    type="text"
                    placeholder="SKU"
                    {...register("sku", { required: "SKU is required", maxLength: { value: 25, message: "Max 25 characters" } })}
                />
                {errors.sku && errors.sku.message}

                <input
                    type="number"
                    step="0.01"
                    placeholder="Price"
                    {...register("price", { required: "Price is required", min: { value: 0, message: "Price must be >= 0" } })}
                />
                {errors.price && errors.price.message}

                <input
                    type="number"
                    placeholder="Stock Quantity"
                    {...register("stockQuantity", { required: "Stock quantity is required", min: { value: 0, message: "Stock must be >= 0" } })}
                />
                {errors.stockQuantity && errors.stockQuantity.message}

                <div>
                    <input
                        type="text"
                        placeholder="Add Variant (e.g., Color: Red)"
                        value={variantInput}
                        onChange={(e) => setVariantInput(e.target.value)}
                    />
                    <button type="button" onClick={handleAddVariant}>Add Variant</button>
                    <div>
                        {variantAttributes.map(v => (
                            <span key={v}>
                                {v} <button type="button" onClick={() => handleRemoveVariant(v)}>x</button>
                            </span>
                        ))}
                    </div>
                </div>

                <button type="submit" disabled={isLoading}>
                    {isLoading ? "Creating..." : "Create Product"}
                </button>
            </form>
            <br />
            <Link to={authenticatedUserPageUrl}>My Page</Link>
        </div>
    );
}