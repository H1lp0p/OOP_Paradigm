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
            val Entity = this.find(name)
            if (Entity != null){
                this.zoo!!.delete(Entity)
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

    fun find(name: String) : Entity?{
        if (this.zoo != null){
            var animal : Animal? = zoo!!.findAnimal(name)
            if (animal != null){
                return animal
            }
            for (worker in this.zoo!!.personal){
                if (worker.name == name){
                    return worker
                }
            }
            for (visitor in this.zoo!!.visitors){
                if (visitor.name == name){
                    return visitor
                }
            }
            for (cage in this.zoo!!.cages){
                if (cage.getId() == name.toInt()){
                    return cage
                }
            }
        }
        return null
    }
}