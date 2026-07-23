def call() {

    echo "========================================"
    echo "Running SonarQube Scan"
    echo "========================================"

    withSonarQubeEnv('SonarQube') {

        sh '''
            sonar-scanner
        '''
    }

}
