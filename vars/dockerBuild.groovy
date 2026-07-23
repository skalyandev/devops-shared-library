import com.build.boutique.Constants

def call(String service) {

    def buildPath = ""

    if (service == "frontend") {
        buildPath = "projects/boutique-microservices/frontend"
    } else {
        buildPath = "projects/boutique-microservices/backend/services/${service}"
    }

    String image = Constants.imageName(
        env.DOCKER_REGISTRY,
        service,
        env.GIT_COMMIT
    )
        
    sh """
        docker build \
            -t ${DOCKER_USERNAME}/boutique-${service}:${env.GIT_COMMIT} \
             ${buildPath}
        """

    return image

}
