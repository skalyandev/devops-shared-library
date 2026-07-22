def call(String service) {

    def buildPath = ""

    if (service == "frontend") {
        buildPath = "projects/boutique-microservices/frontend"
    } else {
        buildPath = "projects/boutique-microservices/backend/services/${service}"
    }

    echo "================================="
    echo "Building ${service}"
    echo "Path : ${buildPath}"
    echo "================================="

    sh """
        docker build \
        -t boutique-${service}:${env.GIT_COMMIT} \
        ${buildPath}
    """
}
