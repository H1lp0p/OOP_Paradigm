import kotlinx.coroutines.*

var running = true
var pause = false

const val ticTime = 1000L

suspend fun tickUpdate(zoo : Zoo){
    while (running){
        if (!pause){
            println(".")
            zoo.tickUpdate()
            logPrint(zoo.getInfo())
            delay(ticTime)
        }
        else{
            print("-paused")
            delay(ticTime / 2)
        }
    }
}

val help = """
    ext - will close all coroutines and exit code
    pause - stop time
    add - will add Entity in the zoo
    del - will delete Entity from the zoo
    info - will print zoo info
    full_info - will print info of all Entitys in the zoo
    set - will change Entity
    voice - will make animal to make a sound
    sleep - will hide command line for some time (if time is paused, there won't be any information in cmd)
    help - will show this text again
""".trimIndent()

suspend fun directorLoop(direct: Director){
    while(running){
        print("->")
        val cmd = readln()
        when(cmd){
            "ext" -> running = false
            "pause" -> pause = !pause
            "add" -> {
                print("What to add? [visitor, worker, animal, cage]\n->")
                val type = readln()
                when(type[0]){
                    'v', 'V' -> {
                        print("Type name: ")
                        val name = readln()
                        print("Choose gender \n${Gender.entries}: ")
                        val genderInput = readln()

                        var gender = Gender.HELICOPTER
                            when(genderInput[0]){
                            'm', 'M' -> gender = Gender.MALE
                            'f', 'F' -> gender = Gender.FEMALE
                            'h', 'H' -> gender = Gender.HELICOPTER
                            's', 'S' -> gender = Gender.SEXIST
                        }
                        val newVisitor = Visitor(name, gender, direct.getZoo()!!)
                        direct.add(newVisitor)
                        println("new Visitor ${newVisitor.getInfo()} added")
                    }
                    'w', 'W' -> {
                        print("Type name: ")
                        val name = readln()
                        print("Choose gender \n${Gender.entries}: ")
                        val genderInput = readln()

                        var gender = Gender.HELICOPTER
                        when(genderInput[0]){
                            'm', 'M' -> gender = Gender.MALE
                            'f', 'F' -> gender = Gender.FEMALE
                            'h', 'H' -> gender = Gender.HELICOPTER
                            's', 'S' -> gender = Gender.SEXIST
                        }
                        print("Set the job: ")
                        val job = readln()

                        val newWorker = Worker(name, gender, job)
                        direct.add(newWorker)
                        println("new Visitor ${newWorker.getInfo()} added")
                    }
                    'a', 'A'->{
                        print("Set type of an animal [fox, capybara, bear]: ")
                        val typeInput = readln()
                        when(typeInput[0]){
                            'f', 'F' -> {
                                direct.add(Fox())
                                println("Fox added")
                            }
                            'c', 'C' -> {
                                direct.add(Capybara())
                                println("Capybara added")
                            }
                            'b', 'B' -> {
                                direct.add(Bear())
                                println("Bear added")
                            }
                        }

                    }
                    'c', 'C' ->{
                        print("Set size of cage: ")
                        val sizeInput = readln().toInt()
                        direct.add(Cage(sizeInput, direct.getZoo()!!.cages.size))
                        println("Cage added")
                    }
                }
            }
            "del" ->{
                print("Type name of Entity to delete: ")
                val name = readln()
                direct.delete(name)
            }
            "info" -> {
                logPrint(direct.getInfo())
            }
            "full_info" ->{
                logPrint(direct._getFullInfo())
            }
            "set"->{
                print("Type name: ")
                val name = readln()
                val Entity = direct.find(name)

                when(Entity){
                    is Animal -> {
                        print("Type new max hunger, new hungerBorder, hunger and state from ${AnimalStates.entries}\n->")
                        val input = readln().split(regex = Regex("(\\s|,\\s?)"))

                        try{
                            val mxHunger = if(input[0] != "") input[0].toInt() else 100
                            val hungerBorder = if(input[1] != "") input[0].toInt() else 50
                            val hunger = if(input[2] != "") input[0].toInt() else 100
                            val state = if (input[3] != "") AnimalStates.entries.filter { it.toString() == input[3] }[0]
                            else AnimalStates.WELLFED

                            Entity.change(mxHunger, hungerBorder, hunger, state)
                        }
                        catch(e: Exception){
                            println("Mistake in some of options. Please, try again")
                        }
                    }
                    is Worker -> {
                        print("Type new name, gender from ${Gender.entries} and a job\n->")
                        val input = readln().split(regex = Regex("(\\s|,\\s?)"))
                        try{
                            val newName = input[0]
                            val gender = if (input[1] != "") Gender.entries.filter { it.toString() == input[3] }[0]
                            else Gender.HELICOPTER
                            val job = input[2]
                            Entity.change(newName, gender, job)
                        }
                        catch(e: Exception){
                            println("Mistake in some of options. Please, try again")
                        }
                    }
                    is Visitor -> {
                        print("Type new name and gender from ${Gender.entries}\n->")
                        val input = readln().split(regex = Regex("(\\s|,\\s?)"))
                        try{
                            val newName = input[0]
                            val gender = if (input[1] != "") Gender.entries.filter { it.toString() == input[3] }[0]
                            else Gender.HELICOPTER
                            Entity.change(newName, gender)
                        }
                        catch(e: Exception){
                            println("Mistake in some of options. Please, try again")
                        }
                    }
                    else ->{
                        println("There is no Entity with name $name")
                    }
                }
            }
            "voice" ->{
                print("Type name: ")
                val name = readln()
                val animal = direct.find(name)
                if (animal is Animal){
                    animal.voice()
                }
                else{
                    println("There is no animal with name $name")
                }
            }
            "sleep" ->{
                print("Set amount of ticks to sleep: ")
                val time = readln().toInt()
                delay((time * ticTime))
            }
            "help" -> println(help)
            else ->{
                println("No such command")
            }
        }
    }
}

fun logPrint(info : Map<*, *>, pref: String = ""){
    for (key in info.keys){
        println("$pref$key-----------------")
        if (info[key] is List<*>){
            for (el in info[key] as List<*>){
                if (el is Map<*, *>){
                    println()
                    logPrint(el, pref + "\t")
                }
                else{
                    println("$pref\t$el")
                }
            }
        }
        else{
            println("$pref${info[key]}")
        }
    }
}

fun main() = runBlocking{
    println("Set your zoo")
    print("number of workers: ")
    val personalCount = readln().toInt()
    print("number of visitors: ")
    val visitorsCount = readln().toInt()


    val zoo = Zoo(personalCount, visitorsCount, 15)
    val director = Director(zoo)

    println("\n-----------------\n")
    println(help)

    val playing = async {
        tickUpdate(zoo)
    }
    val loop = async {
        directorLoop(director)
    }

    loop.await()
    playing.await()
}

//TODO: add diffrent types of food and check them for diffrent animals (at least two types for one animal type)
//TODO: add LINQ and DB to Zoo class
//TODO: add Guid to all elemnts and retype acess to them by this Guid
//TODO: refactor code for jeneric use
//TODO: add comparators somewhere (DB???)