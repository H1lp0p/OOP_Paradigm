interface Entity{
    abstract fun tickUpdate()
    abstract fun getInfo(): Map<Any, Any>
    abstract fun _getAllInfo(): Map<Any, Any>
}