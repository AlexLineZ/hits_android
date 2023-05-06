package com.example.hits_android.blocks

class Scope() {
    private var scopesList = mutableListOf<MutableList<String>>()

    fun addScope(scope: MutableList<String>) {
        scopesList.add(scope)
    }

    fun destoryScope() {
        scopesList.removeAt(scopesList.size - 1)
    }

    fun getScope(): MutableList<String> {
        return scopesList[scopesList.size - 1]
    }
}