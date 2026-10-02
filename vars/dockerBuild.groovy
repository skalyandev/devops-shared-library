import com.build.boutique.Constants

def call(Map config = [:]) {

    String service = config.service
    String registryType = config.registryType ?: "docker"

    if (!service) {
        error "Docker build failed: Service name is required"
    }

    if (!(registryType in ["docker", "ecr"])) {
        error "Unsupported registry type: ${registryType}. Supported values: docker, ecr"
    }

    String buildPath

    if (service == "frontend") {
        buildPath = "projects/boutique-microservices/frontend"
    } else {
        buildPath = "projects/boutique-microservices/backend/services/${service}"
    }

    String imageTag = env.GIT_COMMIT.take(7)

    String registry

    if (registryType == "docker") {
        registry = env.DOCKER_REGISTRY
    } else {
        registry = env.ECR_REGISTRY
    }

    if (!registry) {
        error "Registry is not configured for registry type: ${registryType}"
    }

    String image = "${registry}/${env.PROJECT_NAME}-${service}:${imageTag}"

    echo """
==========================================
Building Docker Image
==========================================
Registry   : ${registryType}
Service    : ${service}
Image      : ${image}
Build Path : ${buildPath}
==========================================
"""

    sh """
        docker build \
            -t ${image} \
            ${buildPath}
    """

    echo "Docker image built successfully: ${image}"

    return image
}
