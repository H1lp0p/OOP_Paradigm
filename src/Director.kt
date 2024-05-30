import java.util.*

class Director(private var zoo : Zoo?){

    fun setZoo(zoo : Zoo){
        this.zoo = zoo
    }

    fun getZoo() : Zoo?{
        return this.zoo
    }

    fun clear(){
        this.zoo = null
    }

    fun add(newEntity : Entity){
        if (this.zoo != null){
            this.zoo!!.add(newEntity)
        }
    }

    fun delete(name: String){
        if (this.zoo != null){
            val entity = this.find(name)
            if (entity != null){
                this.zoo!!.delete(entity)
            }
            else{
                println("$name not found")
            }
        }
    }

    fun _getFullInfo(): Map<Any, Any> {
        return if (this.zoo != null) this.zoo!!._getAllInfo() else mapOf()
    }

    fun getInfo() : Map<Any, Any> {
        return if (this.zoo != null) this.zoo!!.getInfo() else mapOf()
    }

    fun getEntity(id : UUID?) : Entity? {
        if (id == null) {
            return null
        }
        if (this.zoo != null){
            return this.zoo!!.entityList.find { it.id == id }
        }
        return null
    }

    fun find(name: String) : UUID? {
       if (this.zoo != null) {
           return zoo!!.entityList.firstOrNull { it ->
               when (it) {
                   is Animal -> {
                       it.name == name
                   }

                   is Person -> {
                       it.name == name
                   }

                   is Cage -> {
                       it.name == name
                   }

                   else -> false
               }
           }?.id
       }
        return null
    }
}