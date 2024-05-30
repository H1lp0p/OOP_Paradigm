import java.util.*

class Zoo(private var personalCount: Int, private var visitorsCount: Int, private var animalCount: Int = 15) : Entity {
    private val animalTypes = listOf("FOX", "BEAR", "CAPYBARA")

    public var entityList : MutableList<Entity> = mutableListOf(this)

    override val id : UUID = UUID.randomUUID()
        get() = field


    private var typesCount: MutableMap<String, Int> = mutableMapOf()

    init {
        for (type in animalTypes) {
            this.typesCount[type] = 0
        }

        for (i in IntRange(1, this.animalCount)){
            val type = animalTypes[IntRange(0, animalTypes.size - 1).random()]
            when(type){
                "FOX" -> {
                    val cage = this.findSpaceForAnimal(type)
                    val fox = Fox(cage)
                    fox.name += "_${typesCount[type]}"
                    cage.add(fox)
                }
                "BEAR" -> {
                    val cage = this.findSpaceForAnimal(type)
                    val bear = Bear(cage)
                    bear.name += "_${typesCount[type]}"
                    cage.add(bear)
                }
                "CAPYBARA" -> {
                    val cage = this.findSpaceForAnimal(type)
                    val cap = Capybara(cage)
                    cap.name += "_${typesCount[type]}"
                    cage.add(cap)
                }
                else -> {
                    println("$type not found")
                }
            }
            this.typesCount[type] = this.typesCount[type]!! + 1
        }

        for (i in IntRange(1, this.visitorsCount)){
            entityList.add(Visitor("Ivan_$i", Gender.HELICOPTER, this))
        }

        for (i in IntRange(1, this.personalCount)){
            val newWorker = Worker("Bob_$i", Gender.MALE, "feeder")
            newWorker.addCage(this.entityList.filterIsInstance<Cage>().random())
            entityList.add(newWorker)
        }
    }

    private fun findSpaceForAnimal(type: String) : ICage{
        for (cage in this.entityList.filter { it is Cage }.map { it as Cage }){
            if (cage.getType().toString() == type && cage.hasSpace()){
                return cage
            }
        }
        val newCage = Cage(5, "cage_${this.entityList.filterIsInstance<Cage>().size + 1}")
        this.entityList.add(newCage)
        return entityList.find { it-> it.id == newCage.id }!! as ICage
    }

    fun findAnimal(name: String) : Animal?{
        val cage = this.findICageByAnimal(name)
        if (cage != null){
            return cage.find(name)!!
        }
        return null
    }

    private fun findICageByAnimal(name: String) : ICage?{
        for (cage in this.entityList.filterIsInstance<Cage>()){
            if (cage.find(name) != null){
                return cage
            }
        }
        return null
    }

    fun getVisibleAnimals() : List<Animal>{
        val res = entityList.filterIsInstance<Cage>().map { (it as Cage).openSpace() }.flatten()
        return res
    }

    fun add(newEntity : Entity) {
        when (newEntity) {
            is ICage -> {
                this.entityList.filterIsInstance<Worker>().random().addCage(newEntity)
            }

            is Animal -> {
                val cage = this.findSpaceForAnimal(newEntity.getType().toString())
                cage.add(newEntity)
                newEntity.setCage(cage)
                when (newEntity){
                    is Fox ->{
                        newEntity.name += "_${this.typesCount["FOX"]}"
                        this.typesCount["FOX"] = this.typesCount["FOX"]!! + 1
                    }
                    is Bear ->{
                        newEntity.name += "_${this.typesCount["BEAR"]}"
                        this.typesCount["BEAR"] = this.typesCount["BEAR"]!! + 1
                    }
                    is Capybara ->{
                        newEntity.name += "_${this.typesCount["CAPYBARA"]}"
                        this.typesCount["CAPYBARA"] = this.typesCount["CAPYBARA"]!! + 1
                    }
                }
                this.animalCount++
            }

            is Worker -> {
                newEntity.addCage(this.entityList.filterIsInstance<Cage>().random())
                this.personalCount++
            }

            is Visitor -> {
                this.animalCount++
            }
        }
        this.entityList.add(newEntity)
    }

    fun delete(id : UUID){
        var deleted = false

        deleted = this.entityList.removeIf { it -> it.id == id }


        if (deleted) println("entity with UUID ${id} deleted")
    }

    override fun getInfo(): Map<Any, Any> {
        return mapOf(
            "cages" to entityList.filterIsInstance<Cage>().map{ el -> el.getInfo() },
            "personal" to this.entityList.count { it is Worker },
            "visitors" to this.entityList.count { it is Visitor })
    }

    override fun _getAllInfo(): Map<Any, Any> {
        val res : MutableMap<Any, Any> = mutableMapOf()

        res["Cages"] = entityList.filterIsInstance<Cage>().map { el-> el._getAllInfo() }
        res["personal"] = entityList.filterIsInstance<Worker>().map { el-> el._getAllInfo() }
        res["visitors"] = entityList.filterIsInstance<Visitor>().map { el-> el._getAllInfo() }

        return res
    }

    override fun tickUpdate() {
        for (entity in this.entityList){
            if (entity !is Zoo) {
                entity.tickUpdate()
            }
        }
    }
}