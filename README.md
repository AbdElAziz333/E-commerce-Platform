# Ecommerce Web Platform

A simple e-commerce web platform written in spring boot.

#

### Technologies Used

- Programming Language: Java
- Backend: Spring Boot
- Databases: PostgreSQL, Mongo, Redis
- Security: JWT
- AI: RAG and Chroma Database

#

### How To Run

#### Locally

- First, run make run-dev in cmd (runs docker compose dev and infra.dev)
- Second, run services in-order (config-server, discovery-server, auth-service, product-service, order-service, cart-service, payment-service, notification-service, api-gateway)
check the Makefile for the commands (run-(service-name))

#### Docker Compose

- First, run make docker-build in cmd (builds docker images)
- Second, run make run-prod in cmd and volah

#### Kubernetes