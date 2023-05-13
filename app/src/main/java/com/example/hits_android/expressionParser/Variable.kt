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

        // Сложение двух Double переменных или Int и Double
        else if ((type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
            (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
            (type == Type.DOUBLE && otherVariable.type == Type.INT)) {
            return Variable("newVariable", Type.DOUBLE,
                (value.toString().toDouble() + otherVariable.value.toString().toDouble()).toString())
        }

        // Сложение строк
        else if (type == Type.STRING && otherVariable.type == Type.STRING) {
            return Variable("newVariable", Type.STRING, value.toString() + otherVariable.value.toString())
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

        // Вычитание двух Double переменных или Int и Double
        else if ((type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
            (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
            (type == Type.DOUBLE && otherVariable.type == Type.INT)) {
            return Variable("newVariable", Type.DOUBLE,
                (value.toString().toDouble() - otherVariable.value.toString().toDouble()).toString())
        }

        throw Error("Вычитание переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator fun compareTo(otherVariable: Variable) : Int {
        // Сравнение чисел
        if ((type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
            (type == Type.DOUBLE && otherVariable.type == Type.INT) ||
            (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
            (type == Type.INT && otherVariable.type == Type.INT)) {
            if (value.toString().toDouble() > otherVariable.value.toString().toDouble()) {
                return 1
            }
            else if (value.toString().toDouble() == otherVariable.value.toString().toDouble()){
                return 0
            }
            else {
                return -1
            }
        }

        // Сравнение строк
        else if (type == Type.STRING && otherVariable.type == Type.STRING) {
            if (value.toString() > otherVariable.value.toString()) {
                return 1
            }
            else if (value.toString() == otherVariable.value.toString()) {
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

        // Умножение Double и Double или Double и Int
        else if ((type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
            (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
            (type == Type.DOUBLE && otherVariable.type == Type.INT)) {
            return Variable("newVariable", Type.DOUBLE,
                (value.toString().toDouble() * otherVariable.value.toString().toDouble()).toString())
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

        // Деление Double и Double или Double и Int
        else if ((type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
            (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
            (type == Type.DOUBLE && otherVariable.type == Type.INT)) {
            return Variable("newVariable", Type.DOUBLE,
                (value.toString().toDouble() / otherVariable.value.toString().toDouble()).toString())
        }

        throw Error("Деление переменных типов ${type} и ${otherVariable.type}")
    }

    // Взятие остатка от деления
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
        if ((type == Type.INT && (otherVariable as Variable).type == Type.INT) ||
            (type == Type.INT && (otherVariable as Variable).type == Type.DOUBLE) ||
            (type == Type.DOUBLE && (otherVariable as Variable).type == Type.INT) ||
            (type == Type.DOUBLE && (otherVariable as Variable).type == Type.DOUBLE)) {
            return value.toString().toDouble() == (otherVariable as Variable).value.toString().toDouble()
        }

        // Сравнение строк
        if (type == Type.STRING && (otherVariable as Variable).type == Type.STRING) {
            return value.toString() == (otherVariable as Variable).value.toString()
        }

        throw Error("Сравнение переменных типов ${type} и ${(otherVariable as Variable).type}")
    }
}