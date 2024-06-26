import java.util.UUID
import kotlin.math.max
import kotlin.math.min

class Cage(
    var size : Int = 5,
    val name : String,
    override val id : UUID = UUID.randomUUID()) : ICage{

    private var animals : MutableList<Animal> = mutableListOf()
    private var openSpace : MutableList<Animal> = mutableListOf()
    private var safeSpace : MutableList<Animal> = mutableListOf()

    private var food : Food = Food(TypesOfFood.entries.random(), 50)

    private val weights : Map<TypesOfFood, Int> =
        mapOf(
            TypesOfFood.KITTYCAT to 200 ,
            TypesOfFood.BURGAR to 50 ,
            TypesOfFood.STARDUST to 1000
        )

    override fun hasSpace() : Boolean{
        return this.animals.size < this.size
    }

    override fun add(animal : Animal) {
        if (this.hasSpace()){
            if (this.animals.size == 0 || animal.getType() == this.animals[0].getType()){
                this.animals.add(animal)
                this.safeSpace.add(animal)
                if (this.animals.size == 1){
                    this.food = Food(animal.checkFoodToEat().random(), 50)
                }
            }
        }
    }

    override fun move(animal : Animal) {
        if (this.openSpace.contains(animal)){
            this.openSpace.remove(animal)
            this.safeSpace.add(animal)
        }
        else if (this.safeSpace.contains(animal)){
            this.safeSpace.remove(animal)
            this.openSpace.add(animal)
        }
    }

    private fun isAnimalVisible(animal : Animal) : Boolean {
        return this.openSpace.contains(animal)
    }

    override fun openSpace() : List<Animal>{
        return this.openSpace
    }

    override fun fill(food : Food){
        if (this.food > 0 && this.food == food){
            val weightToAdd = weights[this.food.type]!! - this.food.weight
            this.food += weightToAdd
        }
        else{
            this.food = Food(food.type, min(food.weight, this.weights[food.type]!!))
        }
    }

    override fun delete(name: String) {
        val animal = this.find(name)
        if (animal != null) {
            this.animals.remove(animal)
            if (this.isAnimalVisible(animal)){
                this.openSpace.remove(animal)
            }
            else{
                this.safeSpace.remove(animal)
            }
        }

    }

    override fun find(name: String) : Animal? {
        for (animal in this.animals){
            if (animal.name == name){
                return animal
            }
        }
        return null
    }

    override fun feed(animal : Animal, food: Food) : Boolean{
        if (this.animals.contains(animal) && this.isAnimalVisible(animal)){
            return animal.eat(food)
        }
        return false
    }

    override fun getType() : AnimalTypes{
        return if (this.animals.size > 0) animals[0].getType() else AnimalTypes.NONE
    }

    override fun askForTypeOfFood() : TypesOfFood{
        if (this.animals.size > 0){
            if (this.food > 0){
                return this.food.type
            }
            return this.animals[0].checkFoodToEat().random()
        }
        return TypesOfFood.entries.random()
    }

    override fun foodState() : Food{
        return this.food
    }

    override fun tickUpdate() {
        for (animal in this.animals){
            animal.tickUpdate()
        }
        for (animal in this.openSpace){
            if (this.food > 0){
                if (animal.eat(this.food)){
                    food -= 1
                }
            }
        }
    }

    override fun getInfo(): Map<Any, Any> {
        val res : MutableMap<Any, Any> = mutableMapOf()
        res["name"] = this.name
        res["animals"] = this.animals.map { it.getInfo() }
        res["food"] = this.food.getInfo()

        return res
    }

    override fun _getAllInfo(): Map<Any, Any> {
        val res : MutableMap<Any, Any> = mutableMapOf()
        res["name"] = this.name
        res["animals"] = this.animals.map { it._getAllInfo() }
        res["food"] = this.food.getInfo()

        return res
    }
}
