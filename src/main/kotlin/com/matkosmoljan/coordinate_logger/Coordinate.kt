package com.matkosmoljan.coordinate_logger

data class Coordinate(val x: Float, val y: Float) {

    /**
     * @return coordinate string in form "x y"
     */
    override fun toString(): String {
        return String.format("%.0f %.0f", x, y)
    }

    fun normalize(width: Float, height: Float): Coordinate {

        if (width <= 0 || height <= 0) {
            throw IllegalArgumentException("Both the width and the height have to be larger than zero")
        }

        return Coordinate(x / width, y / height)
    }
}