import kotlin.reflect.KProperty

class Delegate {
    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) {
        println("$thisRef pass the $value to ${prop.name}")
    }
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {
       return "$thisRef get ${prop.name}"
    }
}

fun main(args: Array<String>) {
    val e = Example()
    println(e.q)
    println(e.q)
    e.q = "New"
    println(e.q)
}

class Example {
    var q: String by Delegate()
}