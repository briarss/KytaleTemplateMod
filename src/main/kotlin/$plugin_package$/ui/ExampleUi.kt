package $plugin_package$.ui

import aster.amo.kytale.ui.dsl.*

@UiDefinition("$plugin_name$/ExamplePage")
fun examplePage() = interactiveUiPage("example-page") {
    group {
        layoutMode = LayoutMode.Top
        padding = UiPadding(top = 20, left = 20, right = 20, bottom = 20)

        label {
            text = "$plugin_name$ UI"
            style = UiLabelStyle().apply {
                fontSize = 24
                textColor = "#ffffff"
                renderBold = true
            }
        }

        textButton("click-me") {
            primaryButton("Click Me!")
            onClick = {
                // Handle button click
                // 'player' and 'playerRef' are available in this context
            }
        }
    }
}
