package com.example.hits_android.expressionParser

import com.example.hits_android.blocks.blockIndex
import com.example.hits_android.scope.Scope
import java.util.Stack

var variables = mutableMapOf<String, Variable>()
var scopes = Scope()

class ParsingFunctions(private var tokens: List<Token>) {
    private var index = 0

    // Массив ожидаемых токенов в выражении
    private val nextToken = arrayOf(
        Name.MOD.value,
        Name.BOOL.value,
        Name.STRING.value,
        Name.DOUBLE.value,
        Name.CHAR.value,
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
            Name.SPACE.value,
            Name.CHAR.value,
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

            // Если текущий токен - это случайное число
            else if (nowToken.type.name == Name.RAND) {
                resultStack.push(
                    Variable(
                        "",
                        Type.DOUBLE,
                        (-100000..100000).random().toDouble() / 100
                    )
                )
            }

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

            // Если текущий токен - это Char
            else if (nowToken.type.name == Name.CHAR) {
                resultStack.push(
                    Variable(
                        "",
                        Type.CHAR,
                        nowToken.text.slice(1..nowToken.text.length - 2)
                    )
                )
            }

            // Если текущий токен - это Bool
            else if (nowToken.type.name == Name.BOOL) {
                if (nowToken.text != "0" && nowToken.text != "false") {
                    resultStack.push(Variable("", Type.BOOL, "true"))
                } else {
                    resultStack.push(Variable("", Type.BOOL, "false"))
                }
            }

            // Если текущий токен является переменной
            else if (nowToken.type.name == Name.VARIABLE) {
                // Если переменной нет, то выдать ошибку
                if (variables[nowToken.text] == null && !nowToken.text.contains('.')) {
                    throw Exception("Variable ${nowToken.text} was not set")
                }

                // Если следующий токен - поле структуры
                if (variables[nowToken.text.split('.')[0]]?.type == Type.STRUCT && nowToken.text.contains(
                        '.'
                    )
                ) {
                    if (nowToken.text.split('.').count() != 2) {
                        throw Exception("Incorrect access to structure field")
                    }

                    val structName = nowToken.text.split('.')[0]
                    val fieldName = nowToken.text.split('.')[1]

                    if ((variables[structName]?.value as MutableMap<String, Variable>)[fieldName] == null) {
                        throw Exception("Referencing a non-existent field in the ${structName} structure")
                    }
                    resultStack.push((variables[structName]?.value as MutableMap<String, Variable>)[fieldName])
                }
                // Поле элемента массива структур
                else if (nowToken.text[0] == '.') {
                    val curVar = resultStack.pop()

                    val fieldName = nowToken.text.slice(1..nowToken.text.length - 1)

                    if (fieldName.contains('.')) {
                        throw Exception("Incorrect access to structure field")
                    }

                    if ((curVar.value as MutableMap<String, Variable>)[fieldName] == null) {
                        throw Exception("Referencing a non-existent field in a struct")
                    }

                    resultStack.push((curVar.value as MutableMap<String, Variable>)[fieldName])
                } else {
                    if (variables[nowToken.text] == null) {
                        throw Exception("Variable ${nowToken.text} was not set")
                    }
                    resultStack.push(variables[nowToken.text])
                }

                // Если следующий токен - квадратная скобка, то закинуть элемент массива
                if (findToken(Name.L_SQUARE_BRACKET.value) != null) {
                    val currentVar = resultStack.pop()

                    val index = parseExpression()!!

                    if (index.type != Type.INT) {
                        throw Exception("Incorrect array element index")
                    }

                    try {
                        // Элемент Int массива
                        if (currentVar?.type == Type.INT + "Array") {
                            resultStack.push(
                                Variable(
                                    "", Type.INT,
                                    (currentVar.value as Array<Int>)[index.value.toString()
                                        .toInt()]
                                )
                            )

                            // Символ строки
                        } else if (currentVar?.type == Type.STRING) {
                            resultStack.push(
                                Variable(
                                    "",
                                    Type.CHAR,
                                    (currentVar.value as String)[index.value.toString().toInt()]
                                )
                            )

                            // Элемент Double массива
                        } else if (currentVar.type == Type.DOUBLE + "Array") {
                            resultStack.push(
                                Variable(
                                    "", Type.DOUBLE,
                                    (currentVar.value as Array<Double>)[index.value.toString()
                                        .toInt()]
                                )
                            )

                            // Элемент String массива
                        } else if (currentVar.type == Type.STRING + "Array") {
                            resultStack.push(
                                Variable(
                                    "", Type.STRING,
                                    (currentVar.value as Array<String>)[index.value.toString()
                                        .toInt()]
                                )
                            )
                        }

                        // Элемент Bool массива
                        else if (currentVar.type == Type.BOOL + "Array") {
                            resultStack.push(
                                Variable(
                                    "", Type.BOOL,
                                    (currentVar.value as Array<String>)[index.value.toString()
                                        .toInt()]
                                )
                            )
                        }

                        // Элемент Char массива
                        else if (currentVar.type == Type.CHAR + "Array") {
                            resultStack.push(
                                Variable(
                                    "",
                                    Type.CHAR,
                                    (currentVar.value as Array<String>)[index.value.toString()
                                        .toInt()]
                                )
                            )
                        }

                        // Элемент Struct массива
                        else if (currentVar.type == Type.STRUCT + "Array") {
                            resultStack.push(
                                Variable(
                                    "",
                                    Type.STRUCT,
                                    (currentVar.value as Array<MutableMap<String, Variable>>)[index.value.toString()
                                        .toInt()]
                                )
                            )
                        } else {
                            throw Exception("${nowToken.text} is not an array")
                        }
                    }
                    // Выход за пределы массива
                    catch (e: java.lang.Exception) {
                        if (e.message == "${nowToken.text} is not an array") {
                            throw Exception(e.message)
                        }
                        throw Exception("Out of range of array")
                    }

                    // Нахождение символа в массиве строк
                    if (findToken(Name.L_SQUARE_BRACKET.value) != null) {
                        val secondCurrentVar = resultStack.pop()

                        if (secondCurrentVar.type != Type.STRING) {
                            throw Exception("${nowToken.text} is not a multidimensional array")
                        }

                        val secondIndex = parseExpression()!!

                        if (secondIndex.type != Type.INT) {
                            throw Exception("Incorrect array element index")
                        }

                        try {
                            resultStack.push(
                                Variable(
                                    "",
                                    Type.CHAR,
                                    (secondCurrentVar.value as String)[secondIndex.value.toString()
                                        .toInt()]
                                )
                            )
                        } catch (e: Exception) {
                            throw Exception("Out of range of array")
                        }
                    }
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
                            throw Exception("There are not enough operands to apply operations in the block ${blockIndex}")
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
                throw Exception("There are not enough operands to apply operations in the ${blockIndex} block")
            }

            val result = applyOperator(resultStack.pop(), resultStack.pop(), operatorStack.pop())
            resultStack.push(result)
        }

        if (resultStack.size > 1) {
            throw Exception("Incorrect expression in block number ${blockIndex}")
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
            "!=" -> return if (b != a) Variable(" ", Type.BOOL, "true") else Variable(
                "",
                Type.BOOL,
                "false"
            )

            "==" -> return if (b == a) Variable("", Type.BOOL, "true") else Variable(
                "",
                Type.BOOL,
                "false"
            )

            ">=" -> return if (b >= a) Variable("", Type.BOOL, "true") else Variable(
                "",
                Type.BOOL,
                "false"
            )

            "<=" -> return if (b <= a) Variable("", Type.BOOL, "true") else Variable(
                "",
                Type.BOOL,
                "false"
            )

            ">" -> return if (b > a) Variable("", Type.BOOL, "true") else Variable(
                "",
                Type.BOOL,
                "false"
            )

            "<" -> return if (b < a) Variable("", Type.BOOL, "true") else Variable(
                "",
                Type.BOOL,
                "false"
            )

            "||" -> return if ((b.value != "false") || (a.value != "false")) Variable(
                "",
                Type.BOOL,
                "true"
            ) else Variable("", Type.BOOL, "false")

            "&&" -> return if ((b.value != "false") && (a.value != "false")) Variable(
                "",
                Type.BOOL,
                "true"
            ) else Variable("", Type.BOOL, "false")

            else -> throw Exception("Congratulations. You were able to trigger a secret bug!!!")
        }
    }

    private fun getTokenOrError(vararg target: String): Token {
        val token = findToken(*target)
        if (token == null) {
            throw Exception("Syntax error")
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