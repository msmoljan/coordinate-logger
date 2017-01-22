import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.util.*


class CoordinateLoggerApplication : Application(), CoordinateLoggerController.Listener {
    val initialWidth = 640.0

    val initialHeight = 480.0
    val minStageWidth = 460.0
    val minStageHeight = 300.0
    val resBundle = "bundles/CoordinateLogger"
    val initialFxml = "CoordinateLogger.fxml"

    var appName: String? = null
    var stage: Stage? = null

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            launch(CoordinateLoggerApplication::class.java)
        }
    }

    override fun start(stage: Stage?) {
        val fxmlLoader = FXMLLoader()
        val languageBundle = ResourceBundle.getBundle(resBundle)
        fxmlLoader.resources = languageBundle

        val root = fxmlLoader.load<Parent>(javaClass.getResource(initialFxml).openStream())
        val scene = Scene(root, initialWidth, initialHeight)

        appName = languageBundle.getString("app_name")

        stage?.minWidth = minStageWidth
        stage?.minHeight = minStageHeight
        stage?.title = appName
        stage?.scene = scene
        stage?.show()

        this.stage = stage
        val controller: CoordinateLoggerController = fxmlLoader.getController()
        controller.listener = this
    }

    override fun onImageHoverCoordinatesReceived(x: Double, y: Double) {
        stage?.title = "$appName ($x, $y)"
    }
}
