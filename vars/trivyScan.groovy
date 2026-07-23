def call(Map config = [:]) {

    String image = config.image

    if (!image) {
        error "Docker image is mandatory for Trivy scan"
    }

    String severity = config.severity ?: "CRITICAL,HIGH"
    String exitCode = config.exitCode ?: "1"

    echo "=========================================="
    echo "Running Trivy Image Scan"
    echo "Image    : ${image}"
    echo "Severity : ${severity}"
    echo "=========================================="

    sh """
        trivy image \
        --severity ${severity} \
        --exit-code ${exitCode} \
        --no-progress \
        ${image}
    """

    echo "=========================================="
    echo "Trivy Scan Completed Successfully"
    echo "=========================================="
}
