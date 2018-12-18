package com.matkosmoljan.coordinate_logger

import com.matkosmoljan.coordinate_logger.CoordinateLogger.Listener
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Ellipse
import javafx.scene.text.Text
import java.io.File
import java.net.URL
import java.util.*


class CoordinateLoggerController : Initializable, Listener {

    interface Listener {
        fun onImageHoverCoordinatesReceived(x: Int, y: Int)
    }

    private val supportedExtensions = arrayOf(".png", ".jpeg", ".jpg")
    private val coordinateLogger = CoordinateLogger()
    private val dotRadius = 4.0
    private val dragHoverStyle = "-fx-border-color: #1565c0;" +
            "-fx-border-width: 3;" +
            "-fx-border-style: solid;" +
            "-fx-background: transparent;"

    private var scrollPaneDefaultStyle: String? = null
    private var imageViewDefaultStyle: String? = null
    var listener: Listener? = null

    @FXML
    var scrollPane: ScrollPane? = null
    @FXML
    var imageView: ImageView? = null
    @FXML
    var instructionView: Label? = null
    @FXML
    var logView: TextArea? = null
    @FXML
    var drawingPane: Pane? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        setupDragAndDrop()
        setupCoordinateClicks()
        coordinateLogger.listener = this
    }

    private fun resizeDrawingPane() {
        drawingPane?.prefWidth = imageView?.image?.width!!
        drawingPane?.prefHeight = imageView?.image?.height!!
    }

    override fun onCoordinatesUpdated(coordinates: List<Coordinate>) {
        logView?.text = coordinates.listInString()

        coordinates.forEachIndexed { index, coordinate ->
            val x = coordinate.x.toDouble()
            val y = coordinate.y.toDouble()
            val ellipse = Ellipse(x, y, dotRadius, dotRadius)
            val text = Text(x + dotRadius, y - dotRadius, "${positionFromIndex(index)}")
            val color = Color(1.0, 0.0, 0.0, 1.0)

            ellipse.fill = color
            text.fill = color

            drawingPane?.children?.add(ellipse)
            drawingPane?.children?.add(text)
        }
    }

    /**
     * @return string containing all coordinates currently stored in the logger
     */
    fun CoordinateList.listInString(): String {
        val coordinatesBuilder = StringBuilder()

        forEachIndexed { index, coordinate ->
            coordinatesBuilder
                .append(positionFromIndex(index))
                .append(": ")
                .append(coordinate)
                .append("\n")
        }

        return coordinatesBuilder.toString()
    }

    private fun positionFromIndex(index: Int) = index + 1

    private fun setupDragAndDrop() {
        scrollPaneDefaultStyle = scrollPane?.style
        imageViewDefaultStyle = imageView?.style

        scrollPane?.setOnDragEntered {
            handleAcceptedFileOnDrag(it) {
                showDragOverFeedback()
            }
        }

        scrollPane?.setOnDragOver { dragEvent ->
            handleAcceptedFileOnDrag(dragEvent) {
                it.acceptTransferModes(TransferMode.COPY)
            }
        }

        scrollPane?.setOnDragDropped { dragEvent ->
            handleAcceptedFileOnDrag(dragEvent) {
                setImage(it.dragboard.files[0].toURI().toString())
                hideDragOverFeedback()
                instructionView?.isVisible = false
                coordinateLogger.clear()
                resizeDrawingPane()
                drawingPane?.children?.clear()
            }
        }

        scrollPane?.setOnDragDone { hideDragOverFeedback() }
        scrollPane?.setOnDragExited { hideDragOverFeedback() }
    }

    private fun setupCoordinateClicks() {
        imageView?.setOnMouseMoved {
            listener?.onImageHoverCoordinatesReceived(it.x.toInt(), it.y.toInt())
        }

        imageView?.setOnMouseClicked {
            coordinateLogger.log(Coordinate(it.x.toFloat(), it.y.toFloat()))
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
