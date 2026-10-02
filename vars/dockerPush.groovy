def call(String image) {

    call(
        image: image,
        registryType: "docker"
    )
}


def call(Map config = [:]) {

    String image = config.image
    String registryType = config.registryType ?: "docker"

    if (!image) {
        error "Docker push failed: Image name is required"
    }

    if (!(registryType in ["docker", "ecr"])) {
        error "Unsupported registry type: ${registryType}. Supported values: docker, ecr"
    }

    echo """
==========================================
Pushing Docker Image
==========================================
Registry : ${registryType}
Image    : ${image}
==========================================
"""

    if (registryType == "ecr") {

        if (!env.AWS_REGION) {
            error "AWS_REGION is not configured"
        }

        if (!env.ECR_REGISTRY) {
            error "ECR_REGISTRY is not configured"
        }

        echo "Logging into AWS ECR..."

        sh """
            aws ecr get-login-password \
                --region ${env.AWS_REGION} \
            | docker login \
                --username AWS \
                --password-stdin ${env.ECR_REGISTRY}
        """

        echo "AWS ECR login successful"
    }

    sh """
        docker push ${image}
    """

    echo """
==========================================
Docker Image Push Successful
==========================================
Registry : ${registryType}
Image    : ${image}
==========================================
"""
}
