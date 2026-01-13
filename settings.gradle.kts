pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.hytale-modding.info/releases") {
            name = "HytaleModdingReleases"
        }
    }
}

rootProject.name = "template-mod"

// Include Kytale for development (comment out for distribution)
includeBuild("../Kytale") {
    dependencySubstitution {
        substitute(module("aster.amo:Kytale")).using(project(":"))
    }
}
