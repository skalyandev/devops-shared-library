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


    /*
     * =========================================================
     * BUILD PATH
     * =========================================================
     */

    String buildPath

    if (service == "frontend") {

        buildPath = "projects/boutique-microservices/frontend"

    } else {

        buildPath = "projects/boutique-microservices/backend/services/${service}"
    }


    /*
     * =========================================================
     * IMAGE TAG
     * =========================================================
     */

    String imageTag = env.GIT_COMMIT.take(7)


    /*
     * =========================================================
     * REGISTRY
     * =========================================================
     */

    String registry

    if (registryType == "docker") {

        registry = env.DOCKER_REGISTRY

    } else {

        registry = env.ECR_REGISTRY
    }


    if (!registry) {

        error "Registry is not configured for registry type: ${registryType}"
    }


    /*
     * =========================================================
     * IMAGE NAME
     * =========================================================
     *
     * Backend:
     *
     * boutique-backend/auth:<tag>
     * boutique-backend/cart:<tag>
     *
     * Frontend:
     *
     * boutique-frontend/frontend:<tag>
     */

    String repository

    if (service == "frontend") {

        repository = "${env.PROJECT_NAME}-frontend/frontend"

    } else {

        repository = "${env.PROJECT_NAME}-backend/${service}"
    }


    String image = "${registry}/${repository}:${imageTag}"


    /*
     * =========================================================
     * BUILD INFORMATION
     * =========================================================
     */

    echo """
==========================================
Building Docker Image
==========================================
Registry   : ${registryType}
Service    : ${service}
Image      : ${image}
Build Path : ${buildPath}
Git Commit : ${env.GIT_COMMIT}
==========================================
"""


    /*
     * =========================================================
     * DOCKER BUILD
     * =========================================================
     */

    sh """
        docker build \
            -t ${image} \
            ${buildPath}
    """


    echo """
==========================================
Docker Image Built Successfully
==========================================
Service : ${service}
Image   : ${image}
==========================================
"""


    return image
}


