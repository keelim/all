package com.keelim.cnubus

fun main() {
    val person = Person(0, "black", "developer")

    PersonDatabase.getInstance(context)!!.getPersonDao().insert(person)

    val items = PersonDatabase
            .getInstance(context)!!.getPersonDao().getAllPerson()
}