# Kytale

Kotlin Hytale Mod Template powered by [HyKot](https://github.com/AmoAster/HyKot).

## Quick Start

1. Clone or download this template
2. Run the hydrate task to customize:
   ```bash
   ./gradlew hydrate
   ```
3. Follow the prompts to set your mod name, package, and author
4. Build your mod:
   ```bash
   ./gradlew build
   ```

## Development Setup

This template uses Gradle composite builds to include HyKot for development.

By default, `settings.gradle.kts` includes:
```kotlin
includeBuild("../HyKot") {
    dependencySubstitution {
        substitute(module("aster.amo:HyKot")).using(project(":"))
    }
}
```

**For development:** Clone HyKot to `../HyKot` (sibling directory).

**For distribution:** Comment out the `includeBuild` block and use CurseMaven:
```kotlin
dependencies {
    compileOnly("curse.maven:hykot-PROJECTID:FILEID")
}
```

## Manual Setup

If you prefer to set up manually, edit these files:

- `gradle.properties` - Set your mod name, author, package, etc.
- `settings.gradle.kts` - Update `rootProject.name`
- Rename/move `src/main/kotlin/com/example/template/` to your package
- Rename `TemplateMod.kt` to your mod class name

## Project Structure

```
src/main/kotlin/
└── com/example/template/
    └── TemplateMod.kt      # Main plugin class

src/main/resources/
└── manifest.json           # Hytale mod manifest (auto-populated)

gradle.properties           # Mod configuration
build.gradle.kts            # Build configuration with hydrate task
settings.gradle.kts         # Gradle settings with HyKot composite build
```

## Features (via HyKot)

- Kotlin stdlib, reflect, and coroutines
- kotlinx.serialization for JSON configuration
- Event DSL with reified types
- Command DSL with async/coroutine support
- Config DSL with property delegates
- Scheduler DSL for coroutine-based tasks

## Example

```kotlin
class MyMod(init: JavaPluginInit) : KotlinPlugin(init) {

    val config by jsonConfig<MyConfig>("config") { MyConfig() }

    override fun setup() {
        super.setup()

        events {
            on { event: PlayerConnectEvent ->
                logger.info { "Welcome ${event.playerRef.uuid}!" }
            }
        }

        command("greet", "Greet command") {
            executes { ctx ->
                ctx.sendMessage(Message.raw(config.welcomeMessage))
            }
        }
    }

    override fun start() {
        super.start()
        logger.info { "MyMod started!" }
    }
}

@Serializable
data class MyConfig(
    val welcomeMessage: String = "Hello!"
)
```

## License

MIT License
