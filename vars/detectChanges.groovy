import com.build.boutique.Constants

def call() {

    def changedFiles = sh(
        script: "git diff --name-only HEAD~1 HEAD",
        returnStdout: true
    ).trim()

    def changedServices = []

    /*
     * Detect changed backend microservices
     */
    Constants.BACKEND_SERVICES.each { service ->

        if (changedFiles.readLines().any {
            it.startsWith(
                "projects/boutique-microservices/backend/services/${service}/"
            )
        }) {
            changedServices.add(service)
        }
    }

    /*
     * Detect frontend changes
     */
    if (changedFiles.readLines().any {
        it.startsWith(
            "projects/boutique-microservices/frontend/"
        )
    }) {
        changedServices.add("frontend")
    }

    echo """
==========================================
Change Detection
==========================================
Changed Files:
${changedFiles ?: "No changes detected"}

Changed Services:
${changedServices}
==========================================
"""

    return changedServices
}


