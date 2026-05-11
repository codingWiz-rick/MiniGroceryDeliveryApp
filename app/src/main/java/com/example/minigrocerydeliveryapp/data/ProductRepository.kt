package com.example.minigrocerydeliveryapp.data

import com.example.minigrocerydeliveryapp.model.Product

object ProductRepository {
    val dummyProducts = listOf(
        Product(
            id = 1,
            name = "Fresh Red Apples",
            description = "Crispy and sweet red apples imported from the best orchards. Perfect for snacks or baking.",
            price = 2.99,
            imageUrl = "https://images.unsplash.com/photo-1570913149827-d2ac84ab3f9a?q=80&w=500&auto=format&fit=crop",
            category = "Fruits"
        ),
        Product(
            id = 2,
            name = "Organic Bananas",
            description = "Rich in potassium and naturally sweet. These organic bananas are a great energy booster.",
            price = 1.49,
            imageUrl = "https://images.unsplash.com/photo-1603833665858-e61d17a86224?q=80&w=500&auto=format&fit=crop",
            category = "Fruits"
        ),
        Product(
            id = 3,
            name = "Whole Milk",
            description = "Fresh farm milk, rich in calcium and vitamins. 1 Liter bottle.",
            price = 3.50,
            imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?q=80&w=500&auto=format&fit=crop",
            category = "Dairy"
        ),
        Product(
            id = 4,
            name = "Brown Bread",
            description = "Healthy multi-grain brown bread, freshly baked every morning.",
            price = 2.20,
            imageUrl = "https://images.unsplash.com/photo-1509440159596-0249088772ff?q=80&w=500&auto=format&fit=crop",
            category = "Bakery"
        ),
        Product(
            id = 5,
            name = "Fresh Spinach",
            description = "Green leafy spinach, washed and ready to cook. High in iron content.",
            price = 1.80,
            imageUrl = "https://images.unsplash.com/photo-1576045057995-568f588f82fb?q=80&w=500&auto=format&fit=crop",
            category = "Vegetables"
        ),
        Product(
            id = 6,
            name = "Free Range Eggs",
            description = "Pack of 12 fresh brown eggs from free-range chickens.",
            price = 4.99,
            imageUrl = "https://images.unsplash.com/photo-1518562180175-34a163b1a9a6?q=80&w=500&auto=format&fit=crop",
            category = "Dairy"
        ),
        Product(
            id = 7,
            name = "Fresh Strawberries",
            description = "Juicy and sweet strawberries, perfect for desserts or salads.",
            price = 5.99,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?q=80&w=500&auto=format&fit=crop",
            category = "Fruits"
        ),
        Product(
            id = 8,
            name = "Organic Broccoli",
            description = "Fresh green broccoli crowns, nutrient-dense and delicious.",
            price = 2.50,
            imageUrl = "https://images.unsplash.com/photo-1584270354949-c26b0d5b4a0c?q=80&w=500&auto=format&fit=crop",
            category = "Vegetables"
        )
    )

    fun getProductById(id: Int): Product? {
        return dummyProducts.find { it.id == id }
    }
}
