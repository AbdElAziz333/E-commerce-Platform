# Run Locally

run_discovery:
	./gradlew :discovery-server:bootRun --args='--spring.profiles.active=dev' --console=plain
run_auth:
	./gradlew :auth-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_product:
	./gradlew :product-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_cart:
	./gradlew :cart-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_order:
	./gradlew :order-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_payment:
	./gradlew :payment-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_notification:
	./gradlew :notification-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_gateway:
	./gradlew :api-gateway:bootRun --args='--spring.profiles.active=dev' --console=plain

# Docker

#docker_build_discovery:
#	cd discovery-server && docker build -t abdelaziz333/discovery-server:0.0.1 .
#
#docker_build_auth:
#	cd auth-service && docker build -t abdelaziz333/auth-service:0.0.1 .
#
#docker_build_product:
#	cd auth-service && docker build -t abdelaziz333/product-service:0.0.1 .
#
#docker_build_cart:
#	cd product-service && docker build -t abdelaziz333/cart-service:0.0.1 .
#
#docker_build_order:
#	cd cart-service && docker build -t abdelaziz333/order-service:0.0.1 .
#
#docker_build_payment:
#	cd payment-service && docker build -t abdelaziz333/payment-service:0.0.1 .
#
#docker_build_notification:
#	cd notification-service && docker build -t abdelaziz333/notification-service:0.0.1 .
#
#docker_build_gateway:
#	cd api-gateway && docker build -t abdelaziz333/api-gateway:0.0.1 .

docker_build_discovery:
	docker compose -f docker-compose.dev.yaml build discovery-server

docker_build_auth:
	docker compose -f docker-compose.dev.yaml build auth-service

docker_build_product:
	docker compose -f docker-compose.dev.yaml build product-service

docker_build_cart:
	docker compose -f docker-compose.dev.yaml build cart-service

docker_build_order:
	docker compose -f docker-compose.dev.yaml build order-service

docker_build_payment:
	docker compose -f docker-compose.dev.yaml build payment-service

docker_build_notification:
	docker compose -f docker-compose.dev.yaml build notification-service

docker_build_gateway:
	docker compose -f docker-compose.dev.yaml build api-gateway

build_each_separately: docker_build_discovery docker_build_auth docker_build_product docker_build_cart docker_build_order docker_build_payment docker_build_notification docker_build_gateway

docker_run_apps:
	docker compose --profile app up

docker_dev_up:
	docker compose -f docker-compose.dev.yaml -f docker-compose.infra.dev.yaml --profile all up -d

docker_dev_down:
	docker compose -f docker-compose.dev.yaml -f docker-compose.infra.dev.yaml --profile all down -v

docker_prod_up:
	docker compose -f docker-compose.prod.yaml docker-compose.infra.prod.yaml --profile all up -d

docker_prod_down:
	docker compose -f docker-compose.prod.yaml docker-compose.infra.prod.yaml --profile all down -v