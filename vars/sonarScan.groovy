def call(Map config = [:]) {

    def scannerName = config.scanner ?: "SonarScanner"
    def serverName  = config.server ?: "SonarQube"
    def projectDir  = config.projectDir ?: "."

    echo "=========================================="
    echo "Running SonarQube Analysis"
    echo "Project Directory : ${projectDir}"
    echo "Sonar Server      : ${serverName}"
    echo "Scanner           : ${scannerName}"
    echo "=========================================="

    dir(projectDir) {

        def scannerHome = tool scannerName

        withSonarQubeEnv(serverName) {

            sh """
                ${scannerHome}/bin/sonar-scanner
            """
        }
    }

    timeout(time: 5, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: true
    }
}
