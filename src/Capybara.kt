class Capybara(cage : ICage? = null) : Animal(AnimalTypes.CAPYBARA, cage){
    override var maxHunger: Int = 10
    override var hungerBorder: Int = 6
    override var hunger: Int = maxHunger
    override val sound: String = "...yo..."
    override val moveBorder: Int = IntRange(3, 4).random()

    override fun voice() {
        println(this.sound)
    }
}