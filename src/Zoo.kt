class Zoo(private var personalCount: Int, private var visitorsCount: Int, private var animalCount: Int = 15) : Entity {
    private val animalTypes = listOf("FOX", "BEAR", "CAPYBARA")

    var cages : MutableList<ICage> = mutableListOf()
        get() = field
    var personal: MutableList<Worker> = mutableListOf()
        get() = field
    var visitors: MutableList<Visitor> = mutableListOf()
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
            visitors.add(Visitor("Ivan_$i", Gender.HELICOPTER, this))
        }

        for (i in IntRange(1, this.personalCount)){
            personal.add(Worker("Bob_$i", Gender.MALE, "feeder"))
            personal.last().addCage(this.cages[i%this.cages.size])
        }
    }

    private fun findSpaceForAnimal(type: String) : ICage{
        for (cage in this.cages){
            if (cage.getType().toString() == type && cage.hasSpace()){
                return cage
            }
        }
        this.cages.add(Cage(5, this.cages.size))
        return cages.last()
    }

    fun findAnimal(name: String) : Animal?{
        val cage = this.findICageByAnimal(name)
        if (cage != null){
            return cage.find(name)!!
        }
        return null
    }

    private fun findICageByAnimal(name: String) : ICage?{
        for (cage in this.cages){
            if (cage.find(name) != null){
                return cage
            }
        }
        return null
    }

    fun getVisibleAnimals() : List<Animal>{
        var res = mutableListOf<Animal>()
        for (cage in this.cages){
            res.addAll(cage.openSpace())
        }
        return res
    }

    fun add(newEntity : Entity) {
        when (newEntity) {
            is ICage -> {
                this.cages.add(newEntity)
                this.personal.random().addCage(newEntity)
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
                this.personal.add(newEntity)
                personal.last().addCage(this.cages.random())
                this.personalCount++
            }

            is Visitor -> {
                this.visitors.add(newEntity)
                this.animalCount++
            }
        }
    }

    fun delete(entity : Entity){
        var deleted = false
        when (entity){
            is Animal -> {
                val cage = this.findICageByAnimal(entity.name)
                if (cage != null){
                    cage.delete(entity.name)
                    deleted = true
                }
                else{
                    deleted = false
                }
            }
            is Worker -> {
                deleted = this.personal.remove(entity)
            }
            is Visitor -> {
                deleted = this.visitors.remove(entity)
            }
            is ICage -> {
                this.cages.remove(entity)
                for (worker in personal){
                    worker.removeCage(entity)
                }
            }
        }
        if (deleted) println("${entity} deleted")
    }

    override fun getInfo(): Map<Any, Any> {
        return mapOf("cages" to cages.map{ el -> el.getInfo() }, "personal" to personal.size, "visitors" to visitors.size)
    }

    override fun _getAllInfo(): Map<Any, Any> {
        val res : MutableMap<Any, Any> = mutableMapOf()

        res["Cages"] = cages.map { el-> el._getAllInfo() }
        res["personal"] = personal.map { el-> el._getAllInfo() }
        res["visitors"] = visitors.map { el-> el._getAllInfo() }

        return res
    }

    override fun tickUpdate() {

        for (cage in this.cages){
            cage.tickUpdate()
        }
        for (personal in this.personal){
            personal.tickUpdate()
        }

        for (visitor in this.visitors){
            visitor.tickUpdate()
        }
    }
}