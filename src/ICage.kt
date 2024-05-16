interface ICage : Entity {
    fun hasSpace() : Boolean
    fun add(animal : Animal)
    fun move(animal : Animal)
    fun openSpace() : List<Animal>
    fun fill(food : Int)
    fun delete(name: String)
    fun find(name: String) : Animal?
    fun feed(animal : Animal, food: Int) : Boolean
    fun getId() : Int
    fun getType() : AnimalTypes
    fun foodState() : Int
}