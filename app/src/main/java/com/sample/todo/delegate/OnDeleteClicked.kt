package com.sample.todo.delegate

import com.sample.todo.database.entity.Todo


interface OnDeleteClicked {
    fun onDelete(position: Int, todo: Todo)
    fun onItemClicked(position: Int, todo: Todo)
}