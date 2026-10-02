def call(String credentialsId = "docker-creds") {

    echo """
==========================================
Docker Registry Login
==========================================
Registry     : Docker Hub
Credentials  : ${credentialsId}
==========================================
"""

    withCredentials([
        usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'DOCKER_USERNAME',
            passwordVariable: 'DOCKER_PASSWORD'
        )
    ]) {

        sh '''
            echo "$DOCKER_PASSWORD" | docker login \
                --username "$DOCKER_USERNAME" \
                --password-stdin
        '''
    }

    echo "Docker Hub login successful"
}
