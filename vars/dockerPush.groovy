def call(String image) {

    if (!image) {
        error "Docker push failed: Image name is required"
    }

    echo """
==========================================
Pushing Docker Image
Image : ${image}
==========================================
"""

    sh """
        docker push ${image}
    """

    echo "Docker image pushed successfully"
}
