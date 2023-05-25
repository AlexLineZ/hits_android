package com.example.hits_android.expressionParser


class LexicalComponents(private val expression: String) {
    private var index = 0
    private var allTokens = mutableListOf<Token>()

    fun getTokensFromCode(): List<Token> {
        getTokens()
        return allTokens
    }

    private fun getTokens(): Unit? {
        if (index >= expression.length) {
            return null
        }

        for (type in tokensList.values) {
            val str = Regex(type.regex.pattern).find(expression.substring(index))

            if ((allTokens.size == 0 || allTokens[allTokens.size - 1].type.identifier == Identifiers.OPERATORS) &&
                type.name == Name.MINUS) {
                continue
            }

            if (str != null) {
                return if (str.value == Name.SPACE.value) {
                    index += str.value.length
                    getTokens()
                } else {
                    val token = Token(type, str.value)
                    index += str.value.length
                    allTokens.add(token)
                    getTokens()
                }
            }
        }
        return null
    }


}