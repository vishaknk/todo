package com.sample.todo.view.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.sample.todo.delegate.AppOrientation
import com.sample.todo.view.activity.BaseHomeActivity
import kotlinx.android.synthetic.main.activity_base_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    protected abstract fun getAppOrientation(): AppOrientation
    protected abstract fun getToolbarTitle(): String
    protected abstract fun hideToolbar(): Boolean
    protected abstract fun showBackIcon(): Boolean
    protected abstract fun showLogoutIcon(): Boolean

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Set full screen
        setupFullScreen(hideToolbar())
        //Toolbar title
        setToolbarTitle(getToolbarTitle())
        //Set Toolbar back icon
        setToolbarBackIcon(showBackIcon())
        //Set orientation
        setUpScreenOrientation(getAppOrientation())
        //Setup logout button
        setupLogout(showLogoutIcon())
        return (getCreateView(inflater, container, savedInstanceState))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getViewCreated(view, savedInstanceState)
    }

    protected abstract fun getCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    protected abstract fun getViewCreated(
        view: View, savedInstanceState: Bundle?
    )

    private fun setToolbarTitle(title: String) {
        (activity as BaseHomeActivity).toolBarHeader.text = title
    }

    private fun setToolbarBackIcon(show: Boolean = false) {
        if (show) {
            (activity as BaseHomeActivity).imageBack.visibility = View.VISIBLE
        } else {
            (activity as BaseHomeActivity).imageBack.visibility = View.GONE
        }
    }

    private fun setupFullScreen(hideToolbar: Boolean = false) {
        if (hideToolbar) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            (activity as BaseHomeActivity).toolbar.visibility = View.GONE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            (activity as BaseHomeActivity).toolbar.visibility = View.VISIBLE
        }
    }

    private fun setupLogout(showLogoutIcon: Boolean) {
        if (showLogoutIcon) {
            (activity as BaseHomeActivity).imageLogout.visibility = View.VISIBLE
        } else {
            (activity as BaseHomeActivity).imageLogout.visibility = View.GONE
        }
    }

    private fun setUpScreenOrientation(orientation: AppOrientation) {
        when (orientation) {
            AppOrientation.PORTRAIT -> {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            AppOrientation.LANDSCAPE -> {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            AppOrientation.UNSPECIFIED -> {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
}