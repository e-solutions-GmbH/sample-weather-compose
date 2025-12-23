rootProject.name = "sample-weather-compose"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("${rootProject.projectDir}/local-repo")
        }
    }
}

include(":app")
include(":domain")
// Uncomment to be able to publish lib.
// Needs to be commented out.
// I our real projects, the code of the lib is in a dedicated repository and therefore not visible to Koin IDE Plugin.
//include(":someLib")
