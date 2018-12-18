package com.matkosmoljan.coordinate_logger

class CoordinateLogger {

    interface Listener {
        fun onCoordinatesUpdated()
    }

    var listener: Listener? = null

    val coordinates: MutableList<Coordinate> = mutableListOf()

    fun log(coordinate: Coordinate) {
        coordinates.add(coordinate)
        notifyListener()
    }

    fun clear() {
        coordinates.clear()
        notifyListener()
    }

    /**
     * @return string containing all coordinates currently stored in the logger
     */
    override fun toString(): String {
        val coordinatesBuilder = StringBuilder()

        coordinates.forEach {
            coordinatesBuilder.append(it).append("\n")
        }

        return coordinatesBuilder.toString()
    }

    private fun notifyListener() = listener?.onCoordinatesUpdated()
}