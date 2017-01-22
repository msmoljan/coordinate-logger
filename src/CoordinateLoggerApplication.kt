import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import java.util.*


class CoordinateLoggerApplication : Application() {
    val initialWidth = 640.0
    val initialHeight = 480.0
    val minStageWidth = 460.0
    val minStageHeight = 300.0

    val resBundle = "bundles/CoordinateLogger"
    val initialFxml = "CoordinateLogger.fxml"

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

        stage?.minWidth = minStageWidth
        stage?.minHeight = minStageHeight
        stage?.title = languageBundle.getString("app_name")
        stage?.setScene(scene)
        stage?.show()
    }
}
