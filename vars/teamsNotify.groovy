def call(Map config = [:]) {

    String status   = config.status ?: currentBuild.currentResult
    String image    = config.image ?: "N/A"
    String branch   = env.BRANCH_NAME ?: env.GIT_BRANCH ?: "N/A"
    String commit   = env.GIT_COMMIT ? env.GIT_COMMIT.take(7) : "N/A"
    String jobName  = env.JOB_NAME ?: "N/A"
    String buildNo  = env.BUILD_NUMBER ?: "N/A"
    String buildUrl = env.BUILD_URL ?: ""
    String buildBy  = env.BUILD_USER ?: "Jenkins"

    withCredentials([
        string(
            credentialsId: 'microsoft-teams-webhook',
            variable: 'TEAMS_WEBHOOK'
        )
    ]) {

        def payload = """
        {
          "type": "message",
          "attachments": [
            {
              "contentType": "application/vnd.microsoft.card.adaptive",
              "content": {
                "\\\$schema": "http://adaptivecards.io/schemas/adaptive-card.json",
                "type": "AdaptiveCard",
                "version": "1.4",
                "body": [
                  {
                    "type": "TextBlock",
                    "text": "🚀 Jenkins Pipeline Notification",
                    "weight": "Bolder",
                    "size": "Medium"
                  },
                  {
                    "type": "FactSet",
                    "facts": [
                      {
                        "title": "Status",
                        "value": "${status}"
                      },
                      {
                        "title": "Job",
                        "value": "${jobName}"
                      },
                      {
                        "title": "Build",
                        "value": "#${buildNo}"
                      },
                      {
                        "title": "Branch",
                        "value": "${branch}"
                      },
                      {
                        "title": "Commit",
                        "value": "${commit}"
                      },
                      {
                        "title": "Docker Image",
                        "value": "${image}"
                      },
                      {
                        "title": "Triggered By",
                        "value": "${buildBy}"
                      }
                    ]
                  },
                  {
                    "type": "ActionSet",
                    "actions": [
                      {
                        "type": "Action.OpenUrl",
                        "title": "View Build",
                        "url": "${buildUrl}"
                      }
                    ]
                  }
                ]
              }
            }
          ]
        }
        """

        sh """
        curl -s -i -X POST \
        -H "Content-Type: application/json" \
        -d '${payload}' \
        "\$TEAMS_WEBHOOK"
        """
    }

    echo "Microsoft Teams Adaptive Card notification sent."
}
