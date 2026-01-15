package $plugin_package$.ui

import aster.amo.kytale.ui.dsl.*
import com.hypixel.hytale.server.core.Message

/**
 * Example UI pages for $plugin_name$.
 */
@UiDefinition
object $plugin_name$Ui {

    fun registerAll() {
        InteractiveUiRegistry.register("$plugin_name$/ExamplePage", examplePage)
    }

    val examplePage = interactivePage("ExamplePage") {
        width = 400
        height = 300

        title {
            label {
                text = "$plugin_name$"
                style = UiLabelStyle(
                    fontSize = 24,
                    textColor = "#ffffff",
                    renderBold = true,
                    horizontalAlignment = HorizontalAlignment.Center
                )
            }
        }

        content {
            group("MainContent") {
                layoutMode = LayoutMode.Top
                padding = UiPadding(horizontal = 16, vertical = 12)

                label {
                    text = "Welcome to $plugin_name$!"
                    style = UiLabelStyle(fontSize = 14, textColor = "#ffffff")
                }

                group {
                    layoutMode = LayoutMode.Left
                    padding = UiPadding(top = 16)

                    textButton("click-me") {
                        primaryButton("Click Me!")
                        anchor = UiAnchor(width = 120, height = 36)
                        onClick = {
                            player.sendMessage(Message.raw("Button clicked!").color("55FF55"))
                        }
                    }
                }
            }
        }
    }
}
