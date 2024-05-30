class Worker(name: String, gender: Gender, private var job: String) : Person(name, gender){

    var cagesToFeed : MutableList<ICage> = mutableListOf()
    var bag : Food = Food(TypesOfFood.entries.random())

    override fun tickUpdate() {
        this.feed()
        bag = Food(TypesOfFood.entries.random())
    }

    override fun getInfo(): Map<Any, Any> {
        return mapOf(PersonKeys.NAME to this.name, PersonKeys.GENDER to gender,
            WorkerKeys.JOB to this.job, "food" to this.bag.getInfo())
    }

    override fun _getAllInfo(): Map<Any, Any> {
        return this.getInfo()
    }

    fun change(newName: String = this.name, newGender: Gender = this.gender, newJob: String = this.job){
        this.job = newJob
        this.name = newName
        this.gender = newGender
    }

    private fun bagUpdate(){
        this.bag++
    }

    private fun feed(){
        for (cage in cagesToFeed){
            if (cage.foodState() <= 10 && cage.foodState() == this.bag) {
                cage.fill(this.bag)

                val newType = this.cagesToFeed.filter {
                    it.askForTypeOfFood() != this.bag.type
                }.map {
                    it.askForTypeOfFood()
                }.firstOrNull()
                this.bag = Food(newType ?: TypesOfFood.entries.random())
                    return
            }
        }
    }

    fun addCage(cage: ICage){
        this.cagesToFeed.add(cage)
    }

    fun removeCage(cage: ICage){
        if (cagesToFeed.contains(cage)){
            cagesToFeed.remove(cage)
        }
    }
}

//TODO: food and cage (you need to check where to add food and how)