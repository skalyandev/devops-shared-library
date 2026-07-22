def call(String service) {

    def buildPath = ""

    if (service == "frontend") {
        buildPath = "projects/boutique-microservices/frontend"
    } else {
        buildPath = "projects/boutique-microservices/backend/services/${service}"
    }

    withCredentials([
        usernamePassword(
            credentialsId: 'dockerhub-creds',
            usernameVariable: 'DOCKER_USERNAME',
            passwordVariable: 'DOCKER_PASSWORD'
        )
    ]) {

        sh '''
            echo "$DOCKER_PASSWORD" | docker login \
                -u "$DOCKER_USERNAME" \
                --password-stdin
        '''

        sh """
            docker build \
                -t ${DOCKER_USERNAME}/boutique-${service}:${env.GIT_COMMIT} \
                ${buildPath}
        """
    }
}
