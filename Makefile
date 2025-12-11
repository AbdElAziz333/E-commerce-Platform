run:
	docker compose --profile dev up
down:
	docker compose --profile dev down
clear:
	docker compose down -v