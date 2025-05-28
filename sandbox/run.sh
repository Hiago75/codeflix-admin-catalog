docker network create catalog_adm_services
docker network create catalog_adm_network

mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak

docker-compose -f services/docker-compose.yml up -d

echo "Starting containers..."
sleep 5