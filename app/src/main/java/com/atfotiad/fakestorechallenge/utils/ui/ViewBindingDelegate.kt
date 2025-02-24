package com.atfotiad.fakestorechallenge.utils.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> AppCompatActivity.viewDataBinding() =
    ActivityViewBindingDelegate(T::class.java, this)

inline fun <reified T : ViewBinding> Fragment.viewDataBinding() =
    FragmentViewBindingDelegate(T::class.java, this)

class ActivityViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val activity: AppCompatActivity,
) : ReadOnlyProperty<AppCompatActivity, T> {

    private var binding: T? = null

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        binding?.let { return it }

        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        val invokedBinding = inflateMethod.invoke(null, thisRef.layoutInflater) as T
        binding = invokedBinding
        activity.setContentView(binding!!.root)
        return binding!!
    }
}

class FragmentViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    val fragment: Fragment,
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(object :
                androidx.lifecycle.DefaultLifecycleObserver {
                override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
                    binding = null
                }
            })
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }

        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val invokedBinding = inflateMethod.invoke(null, thisRef.layoutInflater, null, false) as T
        binding = invokedBinding
        return binding!!
    }
}