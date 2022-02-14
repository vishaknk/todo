package com.sample.todo.view.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.sample.todo.database.entity.Todo
import com.sample.todo.delegate.AppOrientation
import com.sample.todo.utils.Constants
import com.sample.todo.view.activity.BaseHomeActivity
import com.sample.todo.view.base.BaseFragment
import com.sample.todo.viewModel.ToDoViewModel
import com.sof.retail.viewModel.common.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import timber.log.Timber
import todo.R
import todo.databinding.FragmentUpdateTodoBinding
import java.text.SimpleDateFormat
import java.util.*

class UpdateTodoFragment : BaseFragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var mBinding: FragmentUpdateTodoBinding
    private val todoViewModel: ToDoViewModel by kodeinViewModel()
    lateinit var cal: Calendar
    var mType = 1
    var mReceivedData: Todo? = null

    override fun getAppOrientation(): AppOrientation {
        return AppOrientation.PORTRAIT
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.update_task)
    }

    override fun hideToolbar(): Boolean {
        return false
    }

    override fun showBackIcon(): Boolean {
        return false
    }

    override fun showLogoutIcon(): Boolean {
        return false
    }

    override fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.fragment_update_todo,
            null,
            false
        )

        return mBinding.root
    }

    override fun getViewCreated(view: View, savedInstanceState: Bundle?) {
        cal = Calendar.getInstance()

        /*mBinding.evUpdateTodoTitle.setText(args.currentTodo.title)
        mBinding.evUpdateTodoDescription.setText(args.currentTodo.description)
        mBinding.updateTodoTime.setText(args.currentTodo.time)
        mBinding.updateTodoDate.setText(args.currentTodo.date)*/

        if (arguments != null) {
            Timber.e("Recv " + Gson().toJson(requireArguments()))
            if (requireArguments().containsKey("currentTodo")) {
                val data = requireArguments().getString("currentTodo")
                val conv = Gson().fromJson(data, Todo::class.java)
                if (conv != null) {
                    mReceivedData = conv
                    initViewWithData(mReceivedData)
                }
            }
        }


        mBinding.btnUpdateTodo.setOnClickListener {
            updateTodoItem()
        }

        mBinding.updateTodoTime.setOnClickListener {
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        mBinding.updateTodoDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->
                val sdf = SimpleDateFormat(Constants.DATE_FORMAT_1)
                val targetSDF = SimpleDateFormat(Constants.DATE_FORMAT_2)
                val selectedDate = "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year
                val parsed = sdf.parse(selectedDate)
                val formatted = targetSDF.format(parsed)
                mBinding.updateTodoDate.text = formatted

            }, year, month, day)

            dpd.show()
        }

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioDaily) {
                mType = 1
            } else if (checkedId == R.id.radioWeekly) {
                mType = 2
            }
        }
    }

    private fun initViewWithData(data: Todo?) {
        data?.apply {
            mBinding.evUpdateTodoTitle.setText(data.title)
            mBinding.evUpdateTodoDescription.setText(data.description)
            mBinding.updateTodoTime.text = data.time
            mBinding.updateTodoDate.text = data.date

            if (data.type == 1) {
                mType = 1
                mBinding.radioDaily.isChecked = true
                mBinding.radioWeekly.isChecked = false
            } else {
                mType = 2
                mBinding.radioDaily.isChecked = false
                mBinding.radioWeekly.isChecked = true
            }
        }
    }

    private fun updateTodoItem() {
        val updateTitle = mBinding.evUpdateTodoTitle.text.toString()
        val updateDesc = mBinding.evUpdateTodoDescription.text.toString()
        val time = mBinding.updateTodoTime.text.toString()
        val date = mBinding.updateTodoDate.text.toString()

        if (updateTitle.isNotEmpty() && updateDesc.isNotEmpty()) {
            val updatedTodoItem = Todo(mReceivedData!!.id, updateTitle, updateDesc, time, date, mType)
            todoViewModel.updateTodo(updatedTodoItem).observe(viewLifecycleOwner, {
                Log.e("Observer : ", it.toString())
                if (it) {
                    showToast(getString(R.string.item_updated))
                    NavHostFragment.findNavController(this).navigate(R.id.action_goto_list_todo)
                } else {
                    showToast(getString(R.string.update_failed))
                }
            })
        } else {
            showToast(getString(R.string.fill_fields))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        mBinding.updateTodoTime.text = SimpleDateFormat(Constants.TIME_FORMAT).format(cal.time)
    }
}