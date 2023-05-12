package com.example.hits_android.expressionParser

// Переменная
class Variable(var name: String, var type: String, var value: String) {
    // Сложение переменных
    operator fun plus(otherVariable: Variable) : Variable {
        // Сложение двух Int переменных
        if (type == "Int" && otherVariable.type == "Int") {
            return Variable("newVaribale", "Int",
                (value.toInt() + otherVariable.value.toInt()).toString())
        }

        throw Error("Сложение переменных типов ${type} и ${otherVariable.type}")
    }

    // Вычитание переменных
    operator fun minus(otherVariable: Variable) : Variable {
        // Вычитание двух Int переменных
        if (type == "Int" && otherVariable.type == "Int") {
            return Variable("newVariable", "Int",
                (value.toInt() - otherVariable.value.toInt()).toString())
        }

        throw Error("Вычитание переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator fun compareTo(otherVariable: Variable) : Int {
        // Сравнение Int и Int
        if (type == "Int" && otherVariable.type == "Int") {
            if (value.toInt() > otherVariable.value.toInt()) {
                return 1
            }
            else if (value.toInt() == otherVariable.value.toInt()){
                return 0
            }
            else {
                return -1
            }
        }

        throw Error("Сравнение переменных типов ${type} и ${otherVariable.type}")
    }

    // Умножение переменных
    operator fun times(otherVariable: Variable) : Variable {
        // Умножение Int на Int
        if (type == "Int" && otherVariable.type == "Int") {
            return Variable("newVariable", "Int",
                (value.toInt() * otherVariable.value.toInt()).toString())
        }

        throw Error("Умножение переменных типов ${type} и ${otherVariable.type}")
    }

    // Деление переменных
    operator fun div(otherVariable: Variable) : Variable {
        // Деление Int на Int
        if (type == "Int" && otherVariable.type == "Int") {
            return Variable("newVariable", "Int",
                (value.toInt() / otherVariable.value.toInt()).toString())
        }

        throw Error("Деление переменных типов ${type} и ${otherVariable.type}")
    }

    // Взятие остатко от деления
    operator fun rem(otherVariable: Variable) : Variable {
        // Деление Int и Int
        if (type == "Int" && otherVariable.type == "Int") {
            return Variable("newVariable", "Int",
                (value.toInt() % otherVariable.value.toInt()).toString())
        }

        throw Error("Взятие остатка от деления переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator override fun equals(otherVariable: Any?) : Boolean {

        // Сравнение переменных типа Int
        if (type == "Int" && (otherVariable as Variable).type == "Int") {
            if (value.toInt() == (otherVariable as Variable).value.toInt()) {
                return true
            }
            else {
                return false
            }
        }

        throw Error("Сравнение переменных типов ${type} и ${(otherVariable as Variable).type}")
    }
}