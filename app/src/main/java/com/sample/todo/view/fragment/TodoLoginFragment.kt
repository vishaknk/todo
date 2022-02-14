package com.sample.todo.view.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.sample.todo.delegate.AppOrientation
import com.sample.todo.utils.CustomProgressBar
import com.sample.todo.utils.SessionManager
import com.sample.todo.view.base.BaseFragment
import com.sample.todo.viewModel.ToDoViewModel
import com.sof.retail.viewModel.common.kodeinViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import todo.R
import todo.databinding.FragmentLoginTodoBinding

class TodoLoginFragment : BaseFragment(), KodeinAware {

    override val kodein by kodein()
    private lateinit var mBinding: FragmentLoginTodoBinding
    private val todoViewModel: ToDoViewModel by kodeinViewModel()
    val progressBar = CustomProgressBar()
    private lateinit var sessionManager: SessionManager

    override fun getAppOrientation(): AppOrientation {
        return AppOrientation.PORTRAIT
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.home)
    }

    override fun hideToolbar(): Boolean {
        return true
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
            R.layout.fragment_login_todo,
            null,
            false
        )
        sessionManager = SessionManager(requireContext())
        return mBinding.root
    }

    override fun getViewCreated(view: View, savedInstanceState: Bundle?) {

        if (::sessionManager.isInitialized) {
            if (sessionManager.isLoggedIn) {
                findNavController().navigate(R.id.action_goto_list_todo)
            }
        }

        //For Testing
        //mBinding.evTodoEmail.setText("eve.holt@reqres.in")
        //mBinding.evTodoPassword.setText("cityslicka")

        mBinding.btnLogin.setOnClickListener {

            if (mBinding.evTodoEmail.text.isNullOrEmpty()) {
                showMessage(getString(R.string.please_enter_email))
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mBinding.evTodoEmail.text.toString()).matches()) {
                showMessage(getString(R.string.invalid_email))
                return@setOnClickListener
            }

            if (mBinding.evTodoPassword.text.isNullOrEmpty()) {
                showMessage(getString(R.string.please_enter_password))
                return@setOnClickListener
            }
            progressBar.show(requireContext(), getString(R.string.loading))
            authenticateUser(
                mBinding.evTodoEmail.text.toString(),
                mBinding.evTodoPassword.text.toString()
            )

        }

    }

    private fun authenticateUser(email: String, password: String) {
        todoViewModel.authenticateUser(email, password).observe(viewLifecycleOwner, {
            Log.e("Response : ", Gson().toJson(it))
            progressBar.dialog.dismiss()
            if (it != null) {
                showMessage(getString(R.string.login_success))
                sessionManager.setLogin(true)
                sessionManager.setToken(it.token ?: "null")
                findNavController().navigate(R.id.action_goto_list_todo)
            } else {
                showMessage(getString(R.string.login_failed))
            }
        })
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}