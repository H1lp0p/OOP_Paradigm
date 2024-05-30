class Bear(cage : ICage? = null) : Animal(AnimalTypes.BEAR, cage){
    override var maxHunger: Int = 50
    override var hungerBorder: Int = 10
    override var hunger: Int = maxHunger
    override var sound: String = "RRRRRROAR!"
    override var moveBorder: Int = IntRange(1, 10).random()
    override val typesOfFoodToEat: List<TypesOfFood> =
        listOf(TypesOfFood.STARDUST, TypesOfFood.BURGAR)


    override fun voice() {
        println(this.sound)
    }
}