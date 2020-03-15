package com.lconsulting.sudoku.memento

import android.util.Log

class Caretaker {
    private val listMemento = mutableListOf<Memento>()
    private var position = 0

    fun addMemento(memento: Memento) {
        listMemento.add(position, memento)
        Log.d("tom971", "before ${listMemento.toString()}")
        position++
        if (position < listMemento.size) {
            for (index in listMemento.size-1 downTo position) {
                listMemento.removeAt(index)
            }
            Log.d("tom971", " after ${listMemento.toString()}")
        }
    }

    fun getMemento(p: Int): Memento {
        val memento = if (p == -1) {
            listMemento[position - 1]
        } else {
            listMemento[position]
        }
        position += p
        return memento
    }

    fun clearMemento() {
        listMemento.clear()
    }

    fun isFirstItem() = position == 0

    fun isLastItem() = position == listMemento.size

}