import com.build.boutique.Constants

def call(Map config = [:]) {

    String service = config.service

    String buildPath

    if (service == "frontend") {
        buildPath = "projects/boutique-microservices/frontend"
    } else {
        buildPath = "projects/boutique-microservices/backend/services/${service}"
    }

    String imageTag = env.GIT_COMMIT.take(7)

    String image = "${env.DOCKER_REGISTRY}/${env.PROJECT_NAME}-${service}:${imageTag}"

    

    echo "=========================================="
    echo "Building Docker Image"
    echo "Service    : ${service}"
    echo "Image      : ${image}"
    echo "Build Path : ${buildPath}"
    echo "=========================================="

    sh """
        docker build \
            -t ${image} \
            ${buildPath}
    """

    return image
}
