import com.build.boutique

def call() {

    def changedFiles = sh(
        script: "git diff --name-only HEAD~1 HEAD",
        returnStdout: true
    ).trim()

    def changedServices = []

    Constants.BACKEND_SERVICES.each { service ->
        if (changedFiles.readLines().any {
            it.startsWith("projects/boutique-microservices/backend/services/${service}/")
        }) {
            changedServices.add(service)
        }
    }

    if (changedFiles.readLines().any {
        it.startsWith("projects/boutique-microservices/frontend/")
    }) {
        changedServices.add("frontend")
    }

    echo "Changed Services: ${changedServices}"

    return changedServices
}
