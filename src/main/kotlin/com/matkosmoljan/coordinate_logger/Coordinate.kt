package com.matkosmoljan.coordinate_logger

data class Coordinate(val x: Float, val y: Float) {
    fun normalize(width: Float, height: Float): Coordinate {

        if (width <= 0 || height <= 0) {
            throw IllegalArgumentException("Both the width and the height have to be larger than zero")
        }

        return Coordinate(x / width, y / height)
    }
}