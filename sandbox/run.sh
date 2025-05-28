docker network create catalog_adm_services
docker network create elastic

sudo chown root app/filebeat/filebeat.docker.yml
mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak
mkdir -m 777 .docker/es01
mkdir -m 777 .docker/filebeat

docker-compose -f services/docker-compose.yml up -d
docker-compose -f elk/docker-compose.yml up -d

echo "Starting containers..."
sleep 5