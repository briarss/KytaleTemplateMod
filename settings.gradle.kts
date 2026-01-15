pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.hytale-modding.info/releases") {
            name = "HytaleModdingReleases"
        }
        maven("https://maven.pokeskies.com/releases") {
            name = "PokeSkies"
        }
    }
}

rootProject.name = "\$plugin_name\$"

// Uncomment for local development with Kytale source:
// includeBuild("../Kytale") {
//     dependencySubstitution {
//         substitute(module("aster.amo:kytale")).using(project(":"))
//         substitute(module("aster.amo:hexweave")).using(project(":hexweave"))
//     }
// }
