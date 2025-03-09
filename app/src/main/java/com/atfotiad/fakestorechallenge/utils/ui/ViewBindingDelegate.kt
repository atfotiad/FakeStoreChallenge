package com.atfotiad.fakestorechallenge.utils.ui

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 *  ViewBinding delegate classes for fragments and activities.
 *  @param T is the type of the ViewBinding class.
 *  @return ViewBinding delegate class.
 * */
inline fun <reified T : ViewBinding> FragmentActivity.viewDataBinding() =
    ActivityViewBindingDelegate(T::class.java, this)

/**
 *  ViewBinding delegate extension fun for fragments.
 *  @param T is the type of the ViewBinding class.
 *  @return ViewBinding delegate class.
 * */
inline fun <reified T : ViewDataBinding> Fragment.viewDataBinding(
    noinline viewDataBindingFactory: (View) -> T
) =
    FragmentViewDataBindingDelegate(this, viewDataBindingFactory)

/**
 *  ViewBinding delegate class for activities.
 *  @property [getValue] is a function that returns the binding.
 * */
@Suppress("UNCHECKED_CAST")
class ActivityViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val activity: FragmentActivity,
) : ReadOnlyProperty<FragmentActivity, T> {

    private var binding: T? = null

    /**
     *  [getValue] is a function that returns the binding.
     *  Uses reflection to work with any type of ViewBinding.
     * */
    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        binding?.let { return it }

        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        val invokedBinding = inflateMethod.invoke(null, thisRef.layoutInflater) as T
        binding = invokedBinding
        activity.setContentView(binding!!.root)
        return binding!!
    }
}

/**
 *  ViewBinding delegate class for fragments.
 *  @param T is the type of the ViewBinding class.
 *  @param fragment is the fragment that uses the binding.
 *  @param viewDataBindingFactory is a function that returns the binding for a [View]
 *  @property [getValue] is a function that returns the binding.
 * */
class FragmentViewDataBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewDataBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver =
                Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }

    /**
     *  [getValue] is a function that returns the binding.
     * */
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }
        return viewDataBindingFactory(thisRef.requireView()).also {
            this.binding = it
        }

    }
}