import java.util.UUID

interface Entity{
    fun tickUpdate()
    fun getInfo(): Map<Any, Any>
    fun _getAllInfo(): Map<Any, Any>
    val id : UUID
}