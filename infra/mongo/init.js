db = db.getSiblingDB('product');
db.items.insertOne({ name: "Initial Product", stock: 10 });