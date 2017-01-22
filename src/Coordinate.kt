data class Coordinate(val x: Int, val y: Int) {

    /**
     * @return coordinate string in form "x y"
     */
    override fun toString(): String {
        return "$x $y"
    }
}