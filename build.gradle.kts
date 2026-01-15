plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hytale.mod)
    alias(libs.plugins.kytale.ui)
}

group = findProperty("plugin_group") as String
version = findProperty("plugin_version") as String
val javaVersion = 24

repositories {
    mavenCentral()
    maven("https://maven.pokeskies.com/releases") {
        name = "PokeSkies"
    }
    maven("https://maven.hytale-modding.info/releases") {
        name = "HytaleModdingReleases"
    }
}

dependencies {
    // Kytale - Kotlin language loader (runtime dependency, provided by Kytale mod)
    compileOnly(libs.kytale)

    // Hexweave - DSL framework for events, commands, tasks, and ECS systems
    compileOnly(libs.hexweave)

    // Compile-only annotations
    compileOnly(libs.jetbrains.annotations)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

kotlin {
    jvmToolchain(javaVersion)
}

// Exclude template placeholder directories from compilation (before hydration)
sourceSets {
    main {
        kotlin {
            exclude("**/\$plugin_package\$/**")
        }
    }
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "plugin_group" to findProperty("plugin_group"),
        "plugin_maven_group" to project.group,
        "plugin_name" to findProperty("plugin_name"),
        "plugin_version" to project.version,
        "server_version" to findProperty("server_version"),
        "plugin_description" to findProperty("plugin_description"),
        "plugin_website" to findProperty("plugin_website"),
        "plugin_main_entrypoint" to findProperty("plugin_main_entrypoint"),
        "plugin_author" to findProperty("plugin_author")
    )

    filesMatching("manifest.json") {
        expand(replaceProperties)
    }

    inputs.properties(replaceProperties)
}

hytale {

}

// Kytale UI Plugin Configuration
kytaleUi {
    // Package(s) to scan for @UiDefinition classes (faster builds)
    packages.set(listOf("\$plugin_package\$.ui"))

    // Output directory for generated .ui files (this is the default)
    // outputDir.set(file("src/main/resources/Common/UI/Custom/Pages"))
}

// =============================================================================
// Hydrate Task - Initialize project from template
// =============================================================================
// Usage:
// 1. Update gradle.properties with your mod details
// 2. Run: ./gradlew hydrate

tasks.register<HydrateTask>("hydrate") {
    pluginName = findProperty("plugin_name")?.toString() ?: ""
    pluginGroup = findProperty("plugin_group")?.toString() ?: ""
    pluginPackage = findProperty("plugin_package")?.toString() ?: ""
}
