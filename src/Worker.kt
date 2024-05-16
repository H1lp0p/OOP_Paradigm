class Worker(name: String, gender: Gender, private var job: String) : Person(name, gender){

    var cagesToFeed : MutableList<ICage> = mutableListOf()
    private val bag : Int = IntRange(1,5).random()

    override fun tickUpdate() {
        this.feed()
    }

    override fun getInfo(): Map<Any, Any> {
        return mapOf(PersonKeys.NAME to this.name, PersonKeys.GENDER to gender, WorkerKeys.JOB to this.job)
    }

    override fun _getAllInfo(): Map<Any, Any> {
        return this.getInfo()
    }

    fun change(newName: String = this.name, newGender: Gender = this.gender, newJob: String = this.job){
        this.job = newJob
        this.name = newName
        this.gender = newGender
    }

    private fun feed(){
        for (cage in cagesToFeed){
            if (cage.foodState() <= 1) {
                cage.fill(bag)
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