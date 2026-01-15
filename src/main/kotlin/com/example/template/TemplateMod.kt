package com.example.template

import aster.amo.hexweave.enableHexweave
import aster.amo.kytale.KotlinPlugin
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.plugin.JavaPluginInit

/**
 * Template mod - run `./gradlew hydrate` to customize this project.
 */
class TemplateMod(init: JavaPluginInit) : KotlinPlugin(init) {

    override fun setup() {
        super.setup()

        enableHexweave {
            events {
                listen<PlayerConnectEvent> {
                    plugin.logger.atInfo().log("Player connected: ${event.playerRef.uuid}")
                }
            }

            commands {
                literal("hello", "Say hello") {
                    executesPlayer {
                        sendMessage(Message.raw("Hello from TemplateMod!"))
                    }
                }
            }
        }
    }

    override fun start() {
        super.start()
        logger.atInfo().log("TemplateMod started!")
    }

    override fun shutdown() {
        logger.atInfo().log("TemplateMod shutting down...")
        super.shutdown()
    }
}
