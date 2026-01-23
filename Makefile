#.PHONY: build

# gradle commands to run the services

config-server:
	./gradlew :config-server:bootRun --args='--spring.profiles.active=dev' --console=plain
discovery-server:
	./gradlew :discovery-server:bootRun --args='--spring.profiles.active=dev' --console=plain
auth-service:
	./gradlew :auth-service:bootRun --args='--spring.profiles.active=dev' --console=plain
product-service:
	./gradlew :product-service:bootRun --args='--spring.profiles.active=dev' --console=plain
cart-service:
	./gradlew :cart-service:bootRun --args='--spring.profiles.active=dev' --console=plain
order-service:
	./gradlew :order-service:bootRun --args='--spring.profiles.active=dev' --console=plain
payment-service:
	./gradlew :payment-service:bootRun --args='--spring.profiles.active=dev' --console=plain
notification-service:
	./gradlew :notification-service:bootRun --args='--spring.profiles.active=dev' --console=plain
api-gateway:
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
build:
	#docker compose -f docker-compose.prod.yaml build --no-cache config-server discovery-server auth-service product-service cart-service order-service payment-service notification-service api-gateway
	docker compose -f docker-compose.prod.yaml build config-server discovery-server auth-service product-service cart-service order-service payment-service notification-service api-gateway