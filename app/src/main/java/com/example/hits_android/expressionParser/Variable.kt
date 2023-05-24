package com.example.hits_android.expressionParser

// Переменная
class Variable(var name: String, var type: String, var value: Any) {
    // Проверка возможности привести тип к Double
    private fun isCastingToDouble(otherVariable: Variable): Boolean {
        return (type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
                (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
                (type == Type.DOUBLE && otherVariable.type == Type.INT)
    }

    // Проверка возможности привести тип к String
    private fun isCastingToString(otherVariable: Variable): Boolean {
        return type == Type.STRING && (otherVariable.type == Type.STRING || otherVariable.type == Type.CHAR)
    }

    // Проверка на число
    private fun isNumber(otherVariable: Variable): Boolean {
        return (type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
                (type == Type.DOUBLE && otherVariable.type == Type.INT) ||
                (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
                (type == Type.INT && otherVariable.type == Type.INT)
    }

    // Сложение переменных
    operator fun plus(otherVariable: Variable): Variable {
        // Сложение двух Int переменных
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable(
                "newVaribale", Type.INT,
                (value.toString().toInt() + otherVariable.value.toString().toInt()).toString()
            )
        }

        // Сложение двух Double переменных или Int и Double
        else if (isCastingToDouble(otherVariable)) {
            return Variable(
                "newVariable", Type.DOUBLE,
                (value.toString().toDouble() + otherVariable.value.toString().toDouble()).toString()
            )
        }

        // Сложение строк
        else if (isCastingToString(otherVariable)) {
            return Variable(
                "newVariable",
                Type.STRING,
                value.toString() + otherVariable.value.toString()
            )
        }

        // Сложение Char и Char
        else if (type == Type.CHAR && otherVariable.type == Type.CHAR) {
            return Variable(
                "newVariable",
                Type.CHAR,
                (value.toString()[0].code + otherVariable.value.toString()[0].code).toChar()
            )
        }

        // Сложение Char и Int
        else if (type == Type.CHAR && otherVariable.type == Type.INT) {
            return Variable(
                "newVariable",
                Type.CHAR,
                (value.toString()[0].code + otherVariable.value.toString().toInt()).toChar()
            )
        }

        // Сложение Int и Char
        else if (type == Type.INT && otherVariable.type == Type.CHAR) {
            return Variable(
                "newVariable",
                Type.CHAR,
                (value.toString().toInt() + otherVariable.value.toString()[0].code).toChar()
            )
        }

        throw Exception("Сложение переменных типов ${type} и ${otherVariable.type}")
    }

    // Вычитание переменных
    operator fun minus(otherVariable: Variable): Variable {
        // Вычитание двух Int переменных
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable(
                "newVariable", Type.INT,
                (value.toString().toInt() - otherVariable.value.toString().toInt()).toString()
            )
        }

        // Вычитание двух Double переменных или Int и Double
        else if ((type == Type.DOUBLE && otherVariable.type == Type.DOUBLE) ||
            (type == Type.INT && otherVariable.type == Type.DOUBLE) ||
            (type == Type.DOUBLE && otherVariable.type == Type.INT)
        ) {
            return Variable(
                "newVariable", Type.DOUBLE,
                (value.toString().toDouble() - otherVariable.value.toString().toDouble()).toString()
            )
        }

        // Вычитание Char
        else if (type == Type.CHAR && otherVariable.type == Type.CHAR) {
            return Variable(
                "newVariable",
                Type.CHAR,
                (value.toString()[0].code - otherVariable.value.toString()[0].code).toChar()
            )
        }

        // Вычитание Char и Int
        else if (type == Type.CHAR && otherVariable.type == Type.INT) {
            return Variable(
                "newVariable",
                Type.CHAR,
                (value.toString()[0].code - otherVariable.value.toString().toInt()).toChar()
            )
        }

        // Вычитание Int и Char
        else if (type == Type.INT && otherVariable.type == Type.CHAR) {
            return Variable(
                "newVariable",
                Type.CHAR,
                (value.toString().toInt() - otherVariable.value.toString()[0].code).toChar()
            )
        }

        throw Exception("Вычитание переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator fun compareTo(otherVariable: Variable): Int {
        // Сравнение чисел
        if (isNumber(otherVariable)) {
            if (value.toString().toDouble() > otherVariable.value.toString().toDouble()) {
                return 1
            } else if (value.toString().toDouble() == otherVariable.value.toString().toDouble()) {
                return 0
            } else {
                return -1
            }
        }

        // Сравнение строк и булевых переменных
        else if (type == Type.STRING && otherVariable.type == Type.STRING ||
            type == Type.BOOL && otherVariable.type == Type.BOOL
        ) {
            if (value.toString() > otherVariable.value.toString()) {
                return 1
            } else if (value.toString() == otherVariable.value.toString()) {
                return 0
            } else {
                return -1
            }
        }

        // Сравнение Char и Char
        else if (type == Type.CHAR && otherVariable.type == Type.CHAR) {
            if (value.toString()[0].code > otherVariable.value.toString()[0].code) {
                return 1
            }
            else if (value.toString()[0].code == otherVariable.value.toString()[0].code) {
                return 0
            }
            else {
                return -1
            }
        }

        // Сравнение Char и Int
        else if (type == Type.CHAR && otherVariable.type == Type.INT) {
            if (value.toString()[0].code > otherVariable.value.toString().toInt()) {
                return 1
            }
            else if (value.toString()[0].code == otherVariable.value.toString().toInt()) {
                return 0
            }
            else {
                return -1
            }
        }

        // Сравнение Int и Char
        else if (type == Type.INT && otherVariable.type == Type.INT) {
            if (value.toString().toInt() > otherVariable.value.toString().toInt()) {
                return 1
            }
            else if (value.toString().toInt() == otherVariable.value.toString().toInt()) {
                return 0
            }
            else {
                return -1
            }
        }

        throw Exception("Сравнение переменных типов ${type} и ${otherVariable.type}")
    }

    // Умножение переменных
    operator fun times(otherVariable: Variable): Variable {
        // Умножение Int на Int
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable(
                "newVariable", Type.INT,
                (value.toString().toInt() * otherVariable.value.toString().toInt()).toString()
            )
        }

        // Умножение Double и Double или Double и Int
        else if (isCastingToDouble(otherVariable)) {
            return Variable(
                "newVariable", Type.DOUBLE,
                (value.toString().toDouble() * otherVariable.value.toString().toDouble()).toString()
            )
        }

        throw Exception("Умножение переменных типов ${type} и ${otherVariable.type}")
    }

    // Деление переменных
    operator fun div(otherVariable: Variable): Variable {
        // Деление Int на Int
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable(
                "newVariable", Type.INT,
                (value.toString().toInt() / otherVariable.value.toString().toInt()).toString()
            )
        }

        // Деление Double и Double или Double и Int
        else if (isCastingToDouble(otherVariable)) {
            return Variable(
                "newVariable", Type.DOUBLE,
                (value.toString().toDouble() / otherVariable.value.toString().toDouble()).toString()
            )
        }

        throw Exception("Деление переменных типов ${type} и ${otherVariable.type}")
    }

    // Взятие остатка от деления
    operator fun rem(otherVariable: Variable): Variable {
        // Деление Int и Int
        if (type == Type.INT && otherVariable.type == Type.INT) {
            return Variable(
                "newVariable", Type.INT,
                (value.toString().toInt() % otherVariable.value.toString().toInt()).toString()
            )
        }

        throw Exception("Взятие остатка от деления переменных типов ${type} и ${otherVariable.type}")
    }

    // Сравнение переменных
    operator override fun equals(otherVariable: Any?): Boolean {

        // Сравнение переменных типа Int
        if (isNumber(otherVariable as Variable)) {
            return value.toString().toDouble() == otherVariable.value.toString()
                .toDouble()
        }

        // Сравнение строк и булевых переменных
        if (type == Type.STRING && otherVariable.type == Type.STRING ||
            type == Type.BOOL && otherVariable.type == Type.BOOL
        ) {
            return value.toString() == otherVariable.value.toString()
        }

        // Сравнение Char и Char
        else if (type == Type.CHAR && otherVariable.type == Type.CHAR) {
            return value.toString()[0].code == otherVariable.value.toString()[0].code
        }

        // Сравнение Char и Int
        else if (type == Type.CHAR && otherVariable.type == Type.INT) {
            return value.toString()[0].code == otherVariable.value.toString().toInt()
        }

        // Сравнение Int и Char
        else if (type == Type.INT && otherVariable.type == Type.CHAR) {
            return value.toString().toInt() == otherVariable.value.toString()[0].code
        }

        throw Exception("Сравнение переменных типов ${type} и ${otherVariable.type}")
    }
}