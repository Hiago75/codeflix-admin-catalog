docker-compose -f services/docker-compose.yml down
docker-compose -f elk/docker-compose.yml down
docker-compose -f app/docker-compose.yml down

echo "Stoping containers..."
sleep 5