package com.example.template

import aster.amo.hykot.KotlinPlugin
import aster.amo.hykot.dsl.command
import aster.amo.hykot.dsl.events
import aster.amo.hykot.dsl.on
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.event.PlayerConnectEvent
import com.hypixel.hytale.server.core.plugin.JavaPluginInit

/**
 * Template mod - run `./gradlew hydrate` to customize this project.
 */
class TemplateMod(init: JavaPluginInit) : KotlinPlugin(init) {

    override fun setup() {
        super.setup()

        events {
            on<PlayerConnectEvent> { event ->
                logger.info { "Player connected: ${event.playerRef.uuid}" }
            }
        }

        command("hello", "Say hello") {
            executes { ctx ->
                ctx.sendMessage(Message.raw("Hello from TemplateMod!"))
            }
        }
    }

    override fun start() {
        super.start()
        logger.info { "TemplateMod started!" }
    }

    override fun shutdown() {
        logger.info { "TemplateMod shutting down..." }
        super.shutdown()
    }
}
