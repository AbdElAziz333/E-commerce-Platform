# Run Locally

run_product:
	./gradlew :product-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_order:
	./gradlew :order-service:bootRun --args='--spring.profiles.active=dev' --console=plain
run_gateway:
	./gradlew :api-gateway:bootRun --args='--spring.profiles.active=dev' --console=plain

# Docker

docker_build_product:
	docker compose -f docker-compose.dev.yaml build product-service

docker_build_order:
	docker compose -f docker-compose.dev.yaml build order-service

docker_build_gateway:
	docker compose -f docker-compose.dev.yaml build api-gateway

build_each_separately: docker_build_product docker_build_order docker_build_gateway

docker_run_apps_dev:
	docker compose -f docker-compose.dev.yaml --profile app up

docker_run_dbs_dev:
	docker compose -f docker-compose.dev.yaml --profile database up

docker_run_all_dev:
	docker compose -f docker-compose.dev.yaml --profile all up

docker_run_apps_prod:
	docker compose -f docker-compose.prod.yaml --profile app up

docker_run_dbs_prod:
	docker compose -f docker-compose.prod.yaml --profile database up

docker_run_all_prod:
	docker compose -f docker-compose.prod.yaml --profile all up

docker_dev_down:
	docker compose -f docker-compose.dev.yaml --profile all down -v

docker_prod_down:
	docker compose -f docker-compose.prod.yaml --profile all down -v