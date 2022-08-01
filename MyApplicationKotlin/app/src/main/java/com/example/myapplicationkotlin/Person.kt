package com.example.myapplicationkotlin

import java.io.Serializable

class Person(var name: String, var age: String) : Serializable {
    override fun toString(): String {
        return "Person(name='$name', age=$age)"
    }
}