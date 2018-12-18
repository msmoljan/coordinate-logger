package com.matkosmoljan.coordinate_logger

typealias CoordinateList = List<Coordinate>

class CoordinateLogger {

    interface Listener {
        fun onCoordinatesUpdated(coordinates: CoordinateList)
    }

    var listener: Listener? = null

    private val mutableCoordinates: MutableList<Coordinate> = mutableListOf()
    val coordinates: List<Coordinate>
        get() = mutableCoordinates

    fun log(coordinate: Coordinate) {
        mutableCoordinates.add(coordinate)
        notifyListener()
    }

    fun clear() {
        mutableCoordinates.clear()
        notifyListener()
    }

    private fun notifyListener() = listener?.onCoordinatesUpdated(mutableCoordinates)
}
