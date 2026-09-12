rootProject.name = "StudyWeek3"

pluginManagement {
    repositories {
        maven {
            url = uri("https://artifactory.raiffeisen.ru/artifactory/plugins-gradle")
            credentials {
                username = settings.extra.properties["artifactoryUser"] as String?
                password = settings.extra.properties["artifactoryPassword"] as String?
            }
        }
    }
}