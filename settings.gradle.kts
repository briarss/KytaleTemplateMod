pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.hytale-modding.info/releases") {
            name = "HytaleModdingReleases"
        }
    }
}

rootProject.name = "template-mod"

// Include HyKot for development (comment out for distribution)
includeBuild("../HyKot") {
    dependencySubstitution {
        substitute(module("aster.amo:HyKot")).using(project(":"))
    }
}
