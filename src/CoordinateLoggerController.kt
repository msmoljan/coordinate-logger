import CoordinateLogger.Listener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import java.io.File
import java.net.URL
import java.util.*


class CoordinateLoggerController : Initializable, Listener {
  
    interface Listener {
        fun onImageHoverCoordinatesReceived(x: Int, y: Int)
    }

    val supportedExtensions = arrayOf(".png", ".jpeg", ".jpg")

    val dragHoverStyle = "-fx-border-color: #1565c0;-fx-border-width: 3;-fx-border-style: solid;-fx-background: transparent;"
    val coordinateLogger = CoordinateLogger()
    var scrollPaneDefaultStyle: String? = null

    var imageViewDefaultStyle: String? = null
    var listener: Listener? = null

    @FXML var scrollPane: ScrollPane? = null
    @FXML var imageView: ImageView? = null
    @FXML var instructionView: Label? = null
    @FXML var logView: TextArea? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupDragAndDrop()
        setupCoordinateClicks()
        coordinateLogger.listener = this
    }

    override fun onCoordinatesUpdated() {
        logView?.text = coordinateLogger.toString()
    }

    private fun setupDragAndDrop() {
        scrollPaneDefaultStyle = scrollPane?.style
        imageViewDefaultStyle = imageView?.style

        scrollPane?.setOnDragEntered {
            handleAcceptedFileOnDrag(it, {
                showDragOverFeedback()
            })
        }

        scrollPane?.setOnDragOver {
            handleAcceptedFileOnDrag(it, {
                it.acceptTransferModes(TransferMode.COPY)
            })
        }

        scrollPane?.setOnDragDropped {
            handleAcceptedFileOnDrag(it, {
                setImage(it.dragboard.files[0].toURI().toString())
                hideDragOverFeedback()
                instructionView?.isVisible = false
                coordinateLogger.clear()
            })
        }

        scrollPane?.setOnDragDone { hideDragOverFeedback() }
        scrollPane?.setOnDragExited { hideDragOverFeedback() }
    }

    private fun setupCoordinateClicks() {
        imageView?.setOnMouseMoved {
            listener?.onImageHoverCoordinatesReceived(it.x.toInt(), it.y.toInt())
        }

        imageView?.setOnMouseClicked {
            coordinateLogger.log(Coordinate(it.x.toInt(), it.y.toInt()))
        }
    }

    private fun showDragOverFeedback() {
        scrollPane?.style = dragHoverStyle
        imageView?.style = "-fx-opacity: 0.8;"
    }

    private fun hideDragOverFeedback() {
        scrollPane?.style = scrollPaneDefaultStyle
        imageView?.style = imageViewDefaultStyle
    }

    private fun handleAcceptedFileOnDrag(event: DragEvent, handleFunction: (DragEvent) -> Unit) {
        val db = event.dragboard
        val file = db.files[0]

        if (db.hasFiles() && isFileAccepted(file)) {
            handleFunction(event)
        } else {
            event.consume()
        }
    }

    private fun setImage(url: String?) {
        if (url != null) {
            imageView?.image = Image(url)
            scrollPane?.opacity = 1.0
        } else {
            imageView?.image = null
            scrollPane?.opacity = 0.0
        }
    }

    private fun isFileAccepted(file: File): Boolean {
        supportedExtensions.forEach {
            if (file.name.toLowerCase().endsWith(it)) {
                return true
            }
        }

        return false
    }
}
