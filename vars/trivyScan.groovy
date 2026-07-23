def call(Map config = [:]) {

    def image = config.image

    def severity = config.severity ?: "CRITICAL,HIGH"

    if (!image) {
        error "Trivy scan failed: Image name is required"
    }

    echo "=========================================="
    echo "Running Trivy Image Scan"
    echo "Image    : ${image}"
    echo "Severity : ${severity}"
    echo "Exit Code: ${exit-code} "
    echo "=========================================="


    sh """
        trivy image \
        --severity ${severity} \
        --exit-code ${exit-code} \
        --no-progress \
        ${image}
    """

    echo "Trivy scan completed successfully"
}
