import java.util.*

interface ICage : Entity {
    fun hasSpace() : Boolean
    fun add(animal : Animal)
    fun move(animal : Animal)
    fun openSpace() : List<Animal>
    fun fill(food : Food)
    fun delete(name: String)
    fun find(name: String) : Animal?
    fun feed(animal : Animal, food: Food) : Boolean
    fun getType() : AnimalTypes
    fun foodState() : Food
    fun askForTypeOfFood() : TypesOfFood
}