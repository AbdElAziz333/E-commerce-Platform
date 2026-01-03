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
	docker compose --profile prod build config-server discovery-server auth-service user-service product-service order-service cart-service payment-service notification-service api-gateway