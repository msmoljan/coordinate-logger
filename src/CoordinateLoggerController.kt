import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.net.URL
import java.util.*
import java.io.File
import javafx.scene.input.Dragboard
import javafx.scene.input.TransferMode


class CoordinateLoggerController : Initializable {

    @FXML
    var scrollPane: ScrollPane? = null

    @FXML
    var imageView: ImageView? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {

        scrollPane?.setOnDragOver {
            val db = it.dragboard

            val isAccepted = db.files[0].name.toLowerCase().endsWith(".png")
                    || db.files[0].name.toLowerCase().endsWith(".jpeg")
                    || db.files[0].name.toLowerCase().endsWith(".jpg");

            if (db.hasFiles()) {
                if (isAccepted) {
                    imageView?.style = "-fx-border-color: red;-fx-border-width: 5;-fx-background-color: #C6C6C6;-fx-border-style: solid;"
                    it.acceptTransferModes(TransferMode.COPY);
                }
            } else {
                it.consume();
            }
        }

        scrollPane?.setOnDragDropped {
            val db = it.dragboard
            val isAccepted = db.files[0].name.toLowerCase().endsWith(".png")
                    || db.files[0].name.toLowerCase().endsWith(".jpeg")
                    || db.files[0].name.toLowerCase().endsWith(".jpg")

            if (db.hasFiles() && isAccepted) {
                setImage(db.files[0].toURI().toString())
            } else {
                it.consume()
            }
        }
    }

    fun setImage(url: String?) {
        if (url != null) {
            imageView?.image = Image(url)
            scrollPane?.opacity = 1.0
        } else {
            imageView?.image = null
            scrollPane?.opacity = 0.0
        }
    }
}