def call(Map config = [:]) {

    String status   = config.status ?: currentBuild.currentResult
    String image    = config.image ?: "N/A"
    String branch   = env.BRANCH_NAME ?: env.GIT_BRANCH ?: "N/A"
    String commit   = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : "N/A"
    String jobName  = env.JOB_NAME ?: "N/A"
    String buildNo  = env.BUILD_NUMBER ?: "N/A"
    String buildUrl = env.BUILD_URL ?: ""
    String buildBy  = env.BUILD_USER ?: "Jenkins"

    String color = "0078D4"

    switch (status.toUpperCase()) {
        case "SUCCESS":
            color = "2EB886"
            break
        case "FAILURE":
            color = "E81123"
            break
        case "UNSTABLE":
            color = "FFB900"
            break
        case "ABORTED":
            color = "808080"
            break
    }

    withCredentials([
        string(
            credentialsId: 'microsoft-teams-webhook',
            variable: 'TEAMS_WEBHOOK'
        )
    ]) {

        sh """
        curl -s -H "Content-Type: application/json" \
        -d '{
            "@type":"MessageCard",
            "@context":"https://schema.org/extensions",
            "themeColor":"${color}",
            "summary":"Jenkins Build",
            "title":"🚀 Jenkins Pipeline Notification",
            "sections":[
                {
                    "facts":[
                        {
                            "name":"Status",
                            "value":"${status}"
                        },
                        {
                            "name":"Job",
                            "value":"${jobName}"
                        },
                        {
                            "name":"Build",
                            "value":"#${buildNo}"
                        },
                        {
                            "name":"Branch",
                            "value":"${branch}"
                        },
                        {
                            "name":"Commit",
                            "value":"${commit}"
                        },
                        {
                            "name":"Docker Image",
                            "value":"${image}"
                        },
                        {
                            "name":"Triggered By",
                            "value":"${buildBy}"
                        }
                    ]
                }
            ],
            "potentialAction":[
                {
                    "@type":"OpenUri",
                    "name":"View Build",
                    "targets":[
                        {
                            "os":"default",
                            "uri":"${buildUrl}"
                        }
                    ]
                }
            ]
        }' \
        \$TEAMS_WEBHOOK
        """
    }

    echo "Microsoft Teams notification sent."
}
