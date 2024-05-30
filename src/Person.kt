import java.util.*

abstract class Person(var name: String, var gender: Gender) : Entity {

    override val id : UUID = UUID.randomUUID()

    override fun getInfo(): Map<Any, Any> {
        return mapOf(PersonKeys.NAME to name, PersonKeys.GENDER to gender)
    }

    fun change(newName: String, newGender: Gender){
        this.gender = newGender
        this.name = newName
    }
}