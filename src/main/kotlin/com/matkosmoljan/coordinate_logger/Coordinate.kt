package com.matkosmoljan.coordinate_logger

data class Coordinate(val x: Float, val y: Float) {

    /**
     * @return coordinate string in form "x y"
     */
    override fun toString(): String {
        return String.format("%.0f %.0f", x, y)
    }
}