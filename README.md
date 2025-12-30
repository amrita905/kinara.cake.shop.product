# 🧁 Product Service – Cake & Cupcake Inventory
📌 Overview

Product Service is a core microservice in the Cake Shop platform.
It manages cakes and cupcakes, owns inventory & pricing, and ensures concurrency-safe stock handling to prevent overselling when multiple users place orders simultaneously.

This service is designed with production-grade patterns such as:

Optimistic locking

Distributed stock reservation

Event-driven rollback

Caching for read-heavy APIs

🎯 Responsibilities

Maintain product catalog (Cakes & Cupcakes)

Own stock & price (single source of truth)

Prevent race conditions / overselling

Expose stock reserve & release APIs

React to payment failure events via Kafka

Provide fast reads using Redis cache

🧱 Tech Stack

Java 21

Spring Boot

Spring Data JPA

Spring Cache + Redis

Spring Kafka

Spring Security

Hibernate Optimistic Locking

Maven

🧁 Product Categories

The service supports two fixed categories:

CAKE
CUPCAKE


Implemented using a Java Enum to ensure type safety.

🗂️ Project Structure
product-service
└── src/main/java/com/demo/product
    ├── controller
    ├── service
    ├── repository
    ├── model
    ├── dto
    ├── exception
    ├── advice
    └── kafka

🧾 Domain Model
Product Entity

Key fields:

name

price

category (CAKE / CUPCAKE)

availableStock

active

version (for optimistic locking)

Optimistic locking ensures only one request can reserve the last item.

🔐 Concurrency & Overselling Prevention
Problem

Two customers attempt to buy the last available cake at the same time.

Solution

Stock is modified only inside Product Service

Uses @Version (optimistic locking)

One transaction succeeds, the other fails with a conflict

✔ No global locks
✔ Scales well in distributed systems

🔄 Stock Reservation Flow
Order Service
   ↓ (Feign)
POST /products/{id}/reserve
   ↓
Product Service (Transactional)
   ↓
Stock decremented atomically


If reservation fails → order creation fails immediately.

🔁 Stock Rollback (Kafka-based)

If payment fails:

Payment Service
   ↓
Kafka Event (PAYMENT_FAILED)
   ↓
Product Service
   ↓
Stock released


This ensures eventual consistency.

🌐 APIs
Public APIs
Method	Endpoint	Description
GET	/products	List all active products
GET	/products?category=CAKE	Filter by category
GET	/products/{id}	Product details
Admin APIs
Method	Endpoint	Description
POST	/admin/products	Create product
Internal APIs (Used by Order Service)
Method	Endpoint	Description
POST	/products/{id}/reserve	Reserve stock
POST	/products/{id}/release	Release stock
⚠️ Error Handling

Centralized using @RestControllerAdvice.

Scenario	HTTP Status
Product not found	404
Product inactive	409
Out of stock	409
Concurrent reservation	409

Consistent error response format:

{
  "code": "OUT_OF_STOCK",
  "message": "Insufficient stock"
}

🚀 Caching Strategy
Data	Cache
Product list	Redis
Products by category	Redis
Product by ID	Redis

Cache is evicted automatically on product creation/update.

🔐 Security

Public APIs are open for read

Admin APIs protected with ROLE_ADMIN

Stock APIs intended for internal service calls only


