package com.example.hits_android.expressionParser

import com.example.hits_android.blocks.Scope
import java.util.*

val variables = mutableMapOf<String, Variable>()
var scopes = Scope()

class ParsingFunctions(private var tokens: List<Token>){
    private var index = 0

    private val nextToken = arrayOf( //массив ожидаемых токенов в выражении
        Name.MOD.value,
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
        val operatorStack = Stack<String>() //создаем стек операторов
        val resultStack = Stack<Variable>()  //стек для подсчета чисел

        //ищем ожидаемый токен начала выражения
        var nowToken = getTokenOrError(Name.STRING.value,
            Name.RAND.value, Name.DOUBLE.value, Name.NUMBER.value, Name.VARIABLE.value, Name.L_BRACKET.value)

        // до выполнять до тех пор, пока нет окончания условия выражение(то есть начало выполнения тела ({)
        // или окончания выражения в массиве (]) или окончания выражения (;)
        while(nowToken.type.name != Name.L_FIG_BRACKET
            && nowToken.type.name != Name.R_SQUARE_BRACKET
            && nowToken.type.name != Name.SEMICOLON) {

            //если токен является левой скобочкой (обычной)
            if (nowToken.type.name == Name.L_BRACKET) {
                operatorStack.push(Name.L_BRACKET.value)
            }

            //если токен является правой скобочкой (обычной)
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

            //если текущий токен - это число типа Int
            else if (nowToken.type.name == Name.NUMBER) {
                resultStack.push(Variable("", Type.INT, nowToken.text))
            }

            //если текущий токен - это число типа Double
            else if (nowToken.type.name == Name.DOUBLE) {
                resultStack.push(Variable("", Type.DOUBLE, nowToken.text))
            }

            //если текущий токен - это String
            else if (nowToken.type.name == Name.STRING) {
                resultStack.push(Variable("", Type.STRING, nowToken.text.slice(1..nowToken.text.length - 2)))
            }

            //если текущий токен является переменной
            else if (nowToken.type.name == Name.VARIABLE){

                //если переменной нет, то выдать ошибку
                if (variables[nowToken.text] == null) {
                    throw Exception("Где-то не задал переменную, ищи сам")
                }
                //если следующий токен - квадратная скобка, то закинуть элемент массива
                else if (findToken(Name.L_SQUARE_BRACKET.value) != null) {
                    if ((variables[nowToken.text]?.value as Array<*>)[0] is Int) {
                        resultStack.push(Variable("", Type.INT, (variables[nowToken.text]?.value as Array<Int>)[parseExpression()!!.value.toString().toInt()]))
                    }
                    else if ((variables[nowToken.text]?.value as Array<*>)[0] is Double){
                        resultStack.push(Variable("", Type.DOUBLE, (variables[nowToken.text]?.value as Array<Double>)[parseExpression()!!.value.toString().toInt()]))
                    }
                    else {
                        resultStack.push(Variable("", Type.STRING, (variables[nowToken.text]?.value as Array<String>)[parseExpression()!!.value.toString().toInt()]))
                    }
                }
                //в иных случаях закинуть значение переменной
                else {
                    resultStack.push(variables[nowToken.text])
                }
            }

            //если текущий токен - это оператор или оператор сравнения
            else if (nowToken.type.identifier == Identifiers.OPERATORS || nowToken.type.identifier == Identifiers.BOOLEAN) {
                //если стек пустой или выражение начинается со скобки
                if (operatorStack.isEmpty() || operatorStack.firstElement() == Name.L_BRACKET.value) {
                    operatorStack.push(nowToken.text)
                }
                else { //начать выполнять операции в ином случае
                    val currentPriority = nowToken.type.priority
                    val firstPriority = tokensList[operatorStack.firstElement()]?.priority
                    //если приоритет текущего больше, то просто добавляем оператор
                    if (currentPriority > firstPriority!!){
                        operatorStack.push(nowToken.text)
                    }
                    else { //иначе сосчитать операцию
                        val result = applyOperator(resultStack.pop(), resultStack.pop(), operatorStack.pop())
                        resultStack.push(result)
                        operatorStack.push(nowToken.text)
                    }
                }
            }
            nowToken = getTokenOrError(*nextToken)
        }

        //добиваем выражения до тех пор, пока стек не пустой
        while (!operatorStack.isEmpty()) {
            val result = applyOperator(resultStack.pop(), resultStack.pop(), operatorStack.pop())
            resultStack.push(result)
        }
        return resultStack.pop()
    }

    private fun applyOperator(a: Variable, b: Variable, operator: String): Variable {
        when (operator) {
            "+" -> return b + a
            "-" -> return b - a
            "*" -> return b * a
            "/" -> return b / a
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
            else -> throw Error("OMG")
        }
    }

    private fun getTokenOrError(vararg target: String): Token{
        val token = findToken(*target)
        if (token == null){
            throw Error("Cringe")
        }
        else {
            return token
        }
    }

    private fun findToken(vararg target: String): Token? {
        return if ((index >= tokens.size) || target.find { i -> tokensList[i]!!.name == tokens[index].type.name } == null) {
            null
        } else {
            return tokens[index++]
        }
    }

}





