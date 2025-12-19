run-dev:
	docker compose --profile dev up
down-dev:
	docker compose --profile dev down
clear-dev:
	docker compose --profile dev down -v
status:
	docker compose ps
run-prod:
	docker compose --profile prod up
down-prod:
	docker compose --profile prod down
clear-prod:
	docker compose --profile prod down -v
build:
	docker compose --profile prod build config-server-svc discovery-server-svc auth-service-svc user-service-svc product-service-svc order-service-svc cart-service-svc notification-service-svc payment-service-svc api-gateway-svc