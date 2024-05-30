import kotlin.math.max

class Food(val type: TypesOfFood, val weight : Int = 100) {

    fun getInfo(): Map<Any, Any> {
        return mapOf("type" to this.type, "weight" to this.weight)
    }

    operator fun inc(): Food{
        return Food(this.type, this.weight+100)
    }

    operator fun plus(increment : Int) : Food{
        return Food(this.type, this.weight+increment)
    }

    operator fun plus(increment : Food) : Food{
        if (this.type == increment.type){
            return this + increment.weight
        }
        return this
    }

    operator fun minus(increment : Int) : Food{
        if (this.weight >= increment){
            return Food(this.type, this.weight - increment)
        }
        return Food(this.type, 0)
    }

    operator fun minus(increment : Food) : Food{
        if (this.type == increment.type){
            return this - increment.weight
        }
        return this
    }

    override operator fun equals(other: Any?) : Boolean{
        if (other is Food){
            return this.type == other.type
        }
        return false
    }

    operator fun compareTo(other: Int) : Int{
        return this.weight - other
    }
}