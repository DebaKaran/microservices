# build-and-run.ps1

$AppName = "cards-ms"
$ImageName = "cards-service"

# Stop and remove old container
docker rm -f $AppName 2>$null

# Remove image if --clean passed
if ($args.Count -gt 0 -and $args[0] -eq "--clean") {
    Write-Host "🧽 Cleaning image..."
    docker rmi -f $ImageName 2>$null
}

# Build Spring Boot JAR
./mvnw.cmd clean package -DskipTests

# Build Docker image
docker build -t $ImageName .

# Run the container
docker run -d --name $AppName -p 9000:9000 $ImageName

Write-Host "🚀 $AppName is running at http://localhost:9000"
