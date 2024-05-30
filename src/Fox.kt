class Fox(cage : ICage? = null) : Animal(AnimalTypes.FOX, cage){
    override var maxHunger: Int = 30
    override var hungerBorder: Int = 10
    override var hunger: Int = maxHunger
    override var sound: List<String> = listOf("Hah", "Heh")
    override val moveBorder: Int = IntRange(1, 5).random()
    override val typesOfFoodToEat: List<TypesOfFood> =
        listOf(TypesOfFood.MANGA, TypesOfFood.KITTYCAT)

    override fun voice() {
        println(this.sound.random())
    }
}