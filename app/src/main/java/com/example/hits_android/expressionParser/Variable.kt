package com.example.hits_android.expressionParser

// Переменная
class Variable(var name: String, var type: String, var value: Any) {
    // Сложение переменных
    operator fun plus(otherVariable: Variable) : Variable {
        // Сложение двух Int переменных
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable("newVaribale", Type.INT,
                (value.toString().toInt() + otherVariable.value.toString().toInt()).toString())
        }

        throw Error("Сложение переменных типов ${type} и ${otherVariable.type}")
    }

    // Вычитание переменных
    operator fun minus(otherVariable: Variable) : Variable {
        // Вычитание двух Int переменных
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable("newVariable", Type.INT,
                (value.toString().toInt() - otherVariable.value.toString().toInt()).toString())
        }

        throw Error("Вычитание переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator fun compareTo(otherVariable: Variable) : Int {
        // Сравнение Int и Int
        if (type == Type.INT && otherVariable.type == Type.INT) {
            if (value.toString().toInt() > otherVariable.value.toString().toInt()) {
                return 1
            }
            else if (value.toString().toInt() == otherVariable.value.toString().toInt()){
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
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable("newVariable", Type.INT,
                (value.toString().toInt() * otherVariable.value.toString().toInt()).toString())
        }

        throw Error("Умножение переменных типов ${type} и ${otherVariable.type}")
    }

    // Деление переменных
    operator fun div(otherVariable: Variable) : Variable {
        // Деление Int на Int
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable("newVariable", Type.INT,
                (value.toString().toInt() / otherVariable.value.toString().toInt()).toString())
        }

        throw Error("Деление переменных типов ${type} и ${otherVariable.type}")
    }

    // Взятие остатко от деления
    operator fun rem(otherVariable: Variable) : Variable {
        // Деление Int и Int
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable("newVariable", Type.INT,
                (value.toString().toInt() % otherVariable.value.toString().toInt()).toString())
        }

        throw Error("Взятие остатка от деления переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator override fun equals(otherVariable: Any?) : Boolean {

        // Сравнение переменных типа Int
        if (type == Type.INT && (otherVariable as Variable).type == Type.INT) {
            if (value.toString().toInt() == (otherVariable as Variable).value.toString().toInt()) {
                return true
            }
            else {
                return false
            }
        }

        throw Error("Сравнение переменных типов ${type} и ${(otherVariable as Variable).type}")
    }
}