package com.matkosmoljan.coordinate_logger

typealias CoordinateList = List<Coordinate>

class CoordinateLogger {

    interface Listener {
        fun onCoordinatesUpdated(coordinates: CoordinateList)
    }

    var listener: Listener? = null

    private val coordinates: MutableList<Coordinate> = mutableListOf()

    fun log(coordinate: Coordinate) {
        coordinates.add(coordinate)
        notifyListener()
    }

    fun clear() {
        coordinates.clear()
        notifyListener()
    }

    private fun notifyListener() = listener?.onCoordinatesUpdated(coordinates)
}

/**
 * @return string containing all coordinates currently stored in the logger
 */
fun CoordinateList.listInString(): String {
    val coordinatesBuilder = StringBuilder()

    forEach {
        coordinatesBuilder.append(it).append("\n")
    }

    return coordinatesBuilder.toString()
}
