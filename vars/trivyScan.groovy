def call(Map config = [:]) {

    def image = config.image

    def severity = config.severity ?: "CRITICAL,HIGH"

    //def exitCode = config.exitCode ?: 1


    if (!image) {
        error "Trivy scan failed: Image name is required"
    }

    echo "=========================================="
    echo "Running Trivy Image Scan"
    echo "Image     : ${image}"
    echo "Severity  : ${severity}"
    echo "=========================================="

    sh """
        trivy image \
        --severity ${severity} \
        --exit-code 0 \
        --no-progress \
        ${image}
    """

    echo "Trivy scan completed successfully"
}
