package $plugin_package$

import aster.amo.hexweave.enableHexweave
import aster.amo.kytale.KotlinPlugin
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.plugin.JavaPluginInit

/**
 * $plugin_name$ - A Kytale mod for Hytale.
 */
class $plugin_name$(init: JavaPluginInit) : KotlinPlugin(init) {

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
                        sendMessage(Message.raw("Hello from $plugin_name$!"))
                    }
                }
            }
        }
    }

    override fun start() {
        super.start()
        logger.atInfo().log("$plugin_name$ started!")
    }

    override fun shutdown() {
        logger.atInfo().log("$plugin_name$ shutting down...")
        super.shutdown()
    }
}
