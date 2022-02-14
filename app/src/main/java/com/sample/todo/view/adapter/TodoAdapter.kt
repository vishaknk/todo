package com.sample.todo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.todo.database.entity.Todo
import com.sample.todo.delegate.OnDeleteClicked
import com.sample.todo.viewModel.ToDoViewModel
import todo.databinding.TodoItemBinding

class TodoAdapter(private val viewModel: ToDoViewModel, list: MutableList<Todo>) :
    RecyclerView.Adapter<TodoAdapter.MyViewHolder>() {

    lateinit var deleteListener: OnDeleteClicked
    var mList: MutableList<Todo> = arrayListOf()

    /* private val differCallback = object : DiffUtil.ItemCallback<Todo>(){
         override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
             return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
             return oldItem == newItem
         }
     }

     var differ = AsyncListDiffer(this,differCallback)*/

    inner class MyViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            TodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateList(list: MutableList<Todo>) {
        this.mList = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTodoItem = mList[position]

        holder.binding.apply {
            todoItemTitle.text = currentTodoItem.title
            todoItemDesc.text = currentTodoItem.description
            //itemCheckbox.isChecked = currentTodoItem.isChecked
            todoItemDateTime.text = currentTodoItem.date + "," + currentTodoItem.time

            if (currentTodoItem.type == 1) {
                todoItemType.text = "Daily"
            } else {
                todoItemType.text = "Weekly"
            }

            itemDelete.setOnClickListener {
                currentTodoItem.setSelected(true)
                deleteListener.onDelete(position, currentTodoItem)
            }
            todoItemLayout.setOnClickListener {
                // pass current Data to Update Fragment
                /*val action = HomeTodoListFragment.actionHomeFragmentToUpdateTodoFragment(currentTodoItem)
                holder.itemView.findNavController().navigate(action)*/
                deleteListener.onItemClicked(position, currentTodoItem)
            }

        }
    }

    override fun getItemCount() = mList.size
}