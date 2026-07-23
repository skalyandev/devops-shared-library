def call(String credentialsId = "docker-creds") {

    withCredentials([
        usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'DOCKER_USERNAME',
            passwordVariable: 'DOCKER_PASSWORD'
        )
    ]) {

       sh '''
          echo "$DOCKER_PASSWORD" | docker login \
             -u "$DOCKER_USERNAME" \
             --password-stdin
        '''
    }
}
