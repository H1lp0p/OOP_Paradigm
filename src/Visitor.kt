class Visitor(name: String, gender: Gender, private val zoo: Zoo) : Person(name, gender){

    private var pocket : Int = IntRange(1,100).random()
    private var food: Food = Food(TypesOfFood.entries.random(), (1..10).random())

    override fun tickUpdate(){
        this.watchForAnimals()
    }

    private fun watchForAnimals(){
        if (food > 0){
            var visibleAnimals = zoo.getVisibleAnimals()
            if (visibleAnimals.isNotEmpty()){
                val animalToFeed = visibleAnimals.random()
                val foodToGive = IntRange(1, food.weight).random()
                val fed = animalToFeed.getCage().feed(animalToFeed, Food(food.type, foodToGive));
                if (fed){
                    food-=foodToGive
                    println("!!!$name fed ${animalToFeed.name}")
                }
            }
        }
        else{
            val buyPack = IntRange(1,10).random()
            pocket -= buyPack
            food += buyPack
        }
    }

    override fun getInfo(): Map<Any, Any> {
        return mapOf(PersonKeys.NAME to name, PersonKeys.GENDER to gender,
            "money" to this.pocket.toString(),
            "food" to this.food.getInfo())
    }

    override fun _getAllInfo(): Map<Any, Any> {
        return this.getInfo()
    }
}