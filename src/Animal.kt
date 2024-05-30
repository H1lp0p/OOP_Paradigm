import java.util.*
import kotlin.math.max
import kotlin.math.min

abstract class Animal(private val type: AnimalTypes, private var cage : ICage?) : Entity{
    var name: String = type.toString()
    protected abstract val sound: Any

    override val id : UUID = UUID.randomUUID()

    protected abstract var hungerBorder: Int
    protected abstract var maxHunger: Int
    protected abstract var hunger: Int

    protected abstract val typesOfFoodToEat: List<TypesOfFood>

    var moveCount: Int = 0
    protected abstract val moveBorder: Int

    var state: AnimalStates = AnimalStates.WELLFED

    abstract fun voice()

    fun checkFoodToEat() : List<TypesOfFood>{
        return this.typesOfFoodToEat
    }

    fun eat(food : Food) : Boolean{
        if (this.state == AnimalStates.HUNGRY && food.type in this.typesOfFoodToEat){
            this.hunger += min(food.weight, 1)
            this.state = if (this.hunger >= this.maxHunger) AnimalStates.WELLFED else AnimalStates.HUNGRY
            return true
        }
        return false
    }

    private fun move(){
        if (this.moveCount >= this.moveBorder){
            this.cage!!.move(this)
            this.moveCount = 0
            //println("${this.name} moved")
        }
        else{
            this.moveCount += 1
        }
    }

    fun setCage(cage: ICage){
        this.cage = cage
    }

    fun getCage() : ICage {
        return this.cage!!
    }

    fun change(newMaxHunger: Int = this.maxHunger, newHungerBorder: Int = this.hungerBorder,
               newHunger: Int = this.hunger, newState: AnimalStates = AnimalStates.WELLFED) {
        this.maxHunger = newMaxHunger
        this.hungerBorder = newHungerBorder
        this.hunger = newHunger
        this.state = newState
    }

    fun getType(): AnimalTypes {
        return this.type
    }

    override fun tickUpdate() {
        this.hunger -= 1
        this.hunger = max(this.hunger, 0)
        if (this.hunger < this.maxHunger) {
            this.state = AnimalStates.HUNGRY
        }
        this.move()
    }

    override fun getInfo(): Map<Any, Any> {
        return mapOf("name" to this.name, AnimalKeys.TYPE to this.type, AnimalKeys.STATE to this.state,
            AnimalKeys.HUNGER to this.hunger, "types of food" to this.typesOfFoodToEat)
    }

    override fun _getAllInfo(): Map<Any, Any> {
        return mapOf("name" to this.name, AnimalKeys.TYPE to this.type, AnimalKeys.STATE to this.state,
            AnimalKeys.HUNGER to this.hunger, AnimalKeys.HUNGERBORDER to this.hungerBorder,
            AnimalKeys.SOUND to this.sound, "types of food" to this.typesOfFoodToEat)
    }
}