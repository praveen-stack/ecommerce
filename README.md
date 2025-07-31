# Ecommerce Microservices Platform

A comprehensive microservices-based ecommerce platform built with Spring Boot, featuring multiple specialized services for different business domains.

## Architecture Overview

This platform follows a microservices architecture pattern with the following services:

### Core Services

#### 🛒 **Cart Service** (`cart-service/`)
- **Purpose**: Manages shopping cart functionality and checkout.
- **Key Features**:
  - Shopping cart management with Redis caching
  - Checkout cart.

#### 👥 **User Management Service** (`user-management-service/`)
- **Purpose**: Handles user registration, authentication, and profile management
- **Key Features**:
  - User registration and authentication
  - Profile management and address handling
  - JWT token management
  - Google OAuth

#### 📦 **Product Catalog Service** (`product-catalog-service/`)
- **Purpose**: Manages product information, categories, and inventory
- **Key Features**:
  - Product catalog management
  - Product search and filtering using Elasticsearch

#### 💳 **Payment Service** (`payment-service/`)
- **Purpose**: Handles payment processing and gateway integrations
- **Key Features**:
  - Multiple payment gateway support (Cashfree, etc.)
  - Payment processing and status tracking

#### 📋 **Order Service** (`order-service/`)
- **Purpose**: Manages order lifecycle and fulfillment
- **Key Features**:
  - Order creation and management
  - Order status tracking

#### 📧 **Notification Service** (`notification-service/`)
- **Purpose**: Handles email notifications and communication
- **Key Features**:
  - Email notifications for order confirmations
  - SendGrid integration
  - Kafka message processing

### Infrastructure & Utilities

#### 🚀 **Serverless Functions** (`serverless/`)
- **Purpose**: AWS Lambda functions for processing webhooks.
- **Key Features**:
  - Cashfree webhook handlers
  - Kafka message producers
  - API Gateway integration
- **Technologies**: Java, AWS Lambda, Kafka


## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL
- Redis
- Apache Kafka
- MongoDB
- Elasticsearch

## Service Communication

The services communicate through:
- **REST APIs**: For synchronous communication
- **Apache Kafka**: For asynchronous messaging

## Development

Each service follows a consistent structure:
```
service-name/
├── src/main/java/com/ecommerce/
│   ├── controllers/     # REST API endpoints
│   ├── services/        # Business logic
│   ├── repositories/    # Data access layer
│   ├── models/          # Entity classes
│   ├── dtos/           # Data transfer objects
│   ├── configuration/   # Spring configuration
│   ├── exceptions/      # Custom exceptions
│   └── utils/          # Utility classes
├── src/main/resources/
│   └── application.properties
└── pom.xml
```
