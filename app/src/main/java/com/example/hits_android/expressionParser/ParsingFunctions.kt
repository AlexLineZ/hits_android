package com.example.hits_android.expressionParser

import com.example.hits_android.blocks.Scope
import com.example.hits_android.blocks.blockIndex
import java.util.*

val variables = mutableMapOf<String, Variable>()
var scopes = Scope()

class ParsingFunctions(private var tokens: List<Token>) {
    private var index = 0

    // Массив ожидаемых токенов в выражении
    private val nextToken = arrayOf(
        Name.MOD.value,
        Name.BOOL.value,
        Name.STRING.value,
        Name.DOUBLE.value,
        Name.RAND.value,
        Name.NUMBER.value,
        Name.SPACE.value,
        Name.VARIABLE.value,
        Name.L_SQUARE_BRACKET.value,
        Name.R_SQUARE_BRACKET.value,
        Name.L_BRACKET.value,
        Name.R_BRACKET.value,
        Name.SEMICOLON.value,
        Name.PLUS.value,
        Name.MINUS.value,
        Name.MULTIPLY.value,
        Name.DIVIDE.value,
        Name.EQUALS.value,
        Name.NOT_EQUALS.value,
        Name.GREATER_OR_EQUALS.value,
        Name.GREATER.value,
        Name.LESS_OR_EQUALS.value,
        Name.LESS.value,
        Name.OR.value,
        Name.AND.value,
        Name.L_FIG_BRACKET.value
    )

    fun parseExpression(): Variable? {
        val operatorStack = Stack<String>()  // Создаем стек операторов
        val resultStack = Stack<Variable>()  // Стек для подсчета чисел

        // Ищем ожидаемый токен начала выражения
        var nowToken = getTokenOrError(
            Name.STRING.value,
            Name.RAND.value,
            Name.DOUBLE.value,
            Name.NUMBER.value,
            Name.BOOL.value,
            Name.VARIABLE.value,
            Name.L_BRACKET.value
        )

        // До выполнять до тех пор, пока нет окончания условия выражение(то есть начало выполнения тела ({)
        // или окончания выражения в массиве (]) или окончания выражения (;)
        while (nowToken.type.name != Name.L_FIG_BRACKET
            && nowToken.type.name != Name.R_SQUARE_BRACKET
            && nowToken.type.name != Name.SEMICOLON
        ) {

            // Если токен является левой скобочкой (обычной)
            if (nowToken.type.name == Name.L_BRACKET) {
                operatorStack.push(Name.L_BRACKET.value)
            }

            // Если токен является правой скобочкой (обычной)
            else if (nowToken.type.name == Name.R_BRACKET) {
                var nowOperator = operatorStack.pop()
                while (nowOperator != Name.L_BRACKET.value) {
                    val result = applyOperator(resultStack.pop(), resultStack.pop(), nowOperator)
                    resultStack.push(result)
                    nowOperator = operatorStack.pop()
                }
            }

//            // Если текущий токен - это случайное число
//            else if (nowToken.type.name == Name.RAND) {
//                resultStack.push((-1000..1000).random())
//            }

            // Если текущий токен - это число типа Int
            else if (nowToken.type.name == Name.NUMBER) {
                resultStack.push(Variable("", Type.INT, nowToken.text))
            }

            // Если текущий токен - это число типа Double
            else if (nowToken.type.name == Name.DOUBLE) {
                resultStack.push(Variable("", Type.DOUBLE, nowToken.text))
            }

            // Если текущий токен - это String
            else if (nowToken.type.name == Name.STRING) {
                resultStack.push(
                    Variable(
                        "",
                        Type.STRING,
                        nowToken.text.slice(1..nowToken.text.length - 2)
                    )
                )
            }

            // Если текущий токен - это Bool
            else if (nowToken.type.name == Name.BOOL) {
                if (nowToken.text != "0" && nowToken.text != "false") {
                    resultStack.push(Variable("", Type.BOOL, "1"))
                } else {
                    resultStack.push(Variable("", Type.BOOL, "0"))
                }
            }

            // Если текущий токен является переменной
            else if (nowToken.type.name == Name.VARIABLE) {
                // Если переменной нет, то выдать ошибку
                if (variables[nowToken.text] == null) {
                    throw Exception("Переменная ${nowToken.text} не была задана")
                }

                // Если следующий токен - квадратная скобка, то закинуть элемент массива
                else if (findToken(Name.L_SQUARE_BRACKET.value) != null) {
                    try {
                        // Элемент Int массива
                        if ((variables[nowToken.text]?.type == Type.INT/* as Array<*>)[0] is Int*/)) {
                            resultStack.push(
                                Variable(
                                    "", Type.INT,
                                    (variables[nowToken.text]?.value as Array<Int>)[parseExpression()!!.value.toString()
                                        .toInt()]
                                )
                            )

                            // Элемент Double массива
                        } else if ((variables[nowToken.text]?.value as Array<*>)[0] is Double) {
                            resultStack.push(
                                Variable(
                                    "", Type.DOUBLE,
                                    (variables[nowToken.text]?.value as Array<Double>)[parseExpression()!!.value.toString()
                                        .toInt()]
                                )
                            )

                            // Элемент String массива
                        } else if (variables[nowToken.text]?.type == Type.STRING) {
                            resultStack.push(
                                Variable(
                                    "", Type.STRING,
                                    (variables[nowToken.text]?.value as Array<String>)[parseExpression()!!.value.toString()
                                        .toInt()]
                                )
                            )
                        }

                        // Элемент Bool массива
                        else {
                            resultStack.push(
                                Variable(
                                    "", Type.BOOL,
                                    (variables[nowToken.text]?.value as Array<String>)[parseExpression()!!.value.toString()
                                        .toInt()]
                                )
                            )
                        }
                    }
                    // Выход за пределы массива
                    catch (e: ArrayIndexOutOfBoundsException) {
                        throw Exception("Произошел выход за пределы массива")
                    }
                }

                // В иных случаях закинуть значение переменной
                else {
                    resultStack.push(variables[nowToken.text])
                }
            }

            // Если текущий токен - это оператор или оператор сравнения
            else if (nowToken.type.identifier == Identifiers.OPERATORS || nowToken.type.identifier == Identifiers.BOOLEAN) {
                // Если стек пустой или выражение начинается со скобки
                if (operatorStack.isEmpty() || operatorStack.firstElement() == Name.L_BRACKET.value) {
                    operatorStack.push(nowToken.text)
                }

                // Начать выполнять операции в ином случае
                else {
                    val currentPriority = nowToken.type.priority
                    val firstPriority = tokensList[operatorStack.firstElement()]?.priority

                    // Если приоритет текущего больше, то просто добавляем оператор
                    if (currentPriority > firstPriority!!) {
                        operatorStack.push(nowToken.text)
                    }

                    // Иначе сосчитать операцию
                    else {
                        if (resultStack.size < 2) {
                            throw Exception("Для применения операций в блоке ${blockIndex + 1} не хватает операндов.")
                        }

                        val result =
                            applyOperator(resultStack.pop(), resultStack.pop(), operatorStack.pop())
                        resultStack.push(result)
                        operatorStack.push(nowToken.text)
                    }
                }
            }

            nowToken = getTokenOrError(*nextToken)
        }

        // Добиваем выражения до тех пор, пока стек не пустой
        while (!operatorStack.isEmpty()) {
            if (resultStack.size < 2) {
                throw Exception("Для применения операций в блоке ${blockIndex + 1} не хватает операндов.")
            }

            val result = applyOperator(resultStack.pop(), resultStack.pop(), operatorStack.pop())
            resultStack.push(result)
        }

        if (resultStack.size > 1) {
            throw Exception("Некорректное выражение, указаны несколько значений подряд " +
                    "в блоке номер ${blockIndex + 1}")
        }
        return resultStack.pop()
    }

    // Применение операторов к переменным
    private fun applyOperator(a: Variable, b: Variable, operator: String): Variable {
        when (operator) {
            "+" -> return b + a
            "-" -> return b - a
            "*" -> return b * a
            "/" -> return try {
                b / a
            } catch (e: Exception) {
                if (e.message == "divide by zero") {
                    throw Exception("Деление на 0")
                }
                throw Exception(e.message)
            }
            "%" -> return b % a
            "!=" -> return if (b != a) Variable(" ", Type.INT, "1") else Variable("", Type.INT, "0")
            "==" -> return if (b == a) Variable("", Type.INT, "1") else Variable("", Type.INT, "0")
            ">=" -> return if (b >= a) Variable("", Type.INT, "1") else Variable("", Type.INT, "0")
            "<=" -> return if (b <= a) Variable("", Type.INT, "1") else Variable("", Type.INT, "0")
            ">" -> return if (b > a) Variable("", Type.INT, "1") else Variable("", Type.INT, "0")
            "<" -> return if (b < a) Variable("", Type.INT, "1") else Variable("", Type.INT, "0")
            "||" -> return if ((b.value != "0") || (a.value != "0")) Variable(
                "",
                Type.INT,
                "1"
            ) else Variable("", Type.INT, "0")

            "&&" -> return if ((b.value != "0") && (a.value != "0")) Variable(
                "",
                Type.INT,
                "1"
            ) else Variable("", Type.INT, "0")

            else -> throw Exception("OMG")
        }
    }

    private fun getTokenOrError(vararg target: String): Token {
        val token = findToken(*target)
        if (token == null) {
            throw Exception("Синтаксическая ошибка")
        } else {
            return token
        }
    }

    private fun findToken(vararg target: String): Token? {
        return if ((index >= tokens.size) || target.find { i -> tokensList[i]?.name == tokens[index].type.name } == null) {
            null
        } else {
            return tokens[index++]
        }
    }

}