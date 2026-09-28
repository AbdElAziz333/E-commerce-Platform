# Gradle

gradle_build_discovery:
	./gradlew :discovery:bootRun --args='--spring.profiles.active=local' --console=plain
gradle_build_gateway:
	./gradlew :gateway:bootRun --args='--spring.profiles.active=local' --console=plain
gradle_build_product:
	./gradlew :product:bootRun --args='--spring.profiles.active=local' --console=plain
gradle_build_order:
	./gradlew :order:bootRun --args='--spring.profiles.active=local' --console=plain

# Run Locally

run_discovery_local:
	./gradlew :discovery:bootRun --args='--spring.profiles.active=local' --console=plain
run_product_local:
	./gradlew :product:bootRun --args='--spring.profiles.active=local' --console=plain
run_order_local:
	./gradlew :order:bootRun --args='--spring.profiles.active=local' --console=plain
run_gateway_local:
	./gradlew :gateway:bootRun --args='--spring.profiles.active=local' --console=plain

run_apps_local: run_discovery_local run_gateway_local run_product_local run_order_local

# Docker

docker_run_dbs_local:
	docker compose -f docker-compose.local.yaml --profile database up

docker_run_all_local:
	docker compose -f docker-compose.local.yaml --profile all up

docker_down_dbs_local:
	docker compose -f docker-compose.local.yaml --profile dbs down -v

docker_down_all_local:
	docker compose -f docker-compose.local.yaml --profile all down -v

# Docker Build

docker_build_discovery:
	docker compose -f docker-compose.yaml build discovery-server

docker_build_product:
	docker compose -f docker-compose.yaml build product-service

docker_build_order:
	docker compose -f docker-compose.yaml build order-service

docker_build_gateway:
	docker compose -f docker-compose.yaml build api-gateway

docker_build_all: docker_build_discovery docker_build_product docker_build_order docker_build_gateway

# Docker Run

docker_run_apps:
	docker compose -f docker-compose.yaml --profile app up

docker_run_dbs:
	docker compose -f docker-compose.yaml --profile database up

docker_run_all:
	docker compose -f docker-compose.yaml --profile all up

docker_apps_down:
	docker compose -f docker-compose.yaml --profile app down -v

docker_dbs_down:
	docker compose -f docker-compose.yaml --profile db down -v

docker_all_down:
	docker compose -f docker-compose.yaml --profile all down -v
