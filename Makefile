# gradle commands to run the services

run-config-server:
	./gradlew :config-server:bootRun --args='--spring.profiles.active=dev' --console=plain
run-discovery-server:
	./gradlew :discovery-server:bootRun --args='--spring.profiles.active=dev' --console=plain
run-auth-service:
	./gradlew :auth-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run-product-service:
	./gradlew :product-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run-cart-service:
	./gradlew :cart-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run-order-service:
	./gradlew :order-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run-payment-service:
	./gradlew :payment-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run-notification-service:
	./gradlew :notification-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run-api-gateway:
	./gradlew :api-gateway:bootRun --args='--spring.profiles.active=dev' --console=plain

# docker commands

run-dev:
	docker compose -f docker-compose.dev.yaml up
down-dev:
	docker compose -f docker-compose.dev.yaml down
clear-dev:
	docker compose -f docker-compose.dev.yaml down -v
run-prod:
	docker compose -f docker-compose.prod.yaml up
down-prod:
	docker compose -f docker-compose.prod.yaml down
clear-prod:
	docker compose -f docker-compose.prod.yaml down -v
docker-build:
	docker compose -f docker-compose.prod.yaml build config-server discovery-server auth-service product-service cart-service order-service payment-service notification-service api-gateway