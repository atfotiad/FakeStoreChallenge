package com.atfotiad.fakestorechallenge.utils.ui

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <T : ViewBinding> Fragment.viewDataBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            if (binding == null) {
                binding = bindingInflater.invoke(layoutInflater)
                thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
            }
            return binding!!
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }

inline fun <T : ViewBinding> AppCompatActivity.viewDataBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
): ReadOnlyProperty<AppCompatActivity, T> =
    object : ReadOnlyProperty<AppCompatActivity, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        init {
            lifecycle.addObserver(this)
        }

        override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
            if (binding == null) {
                binding = bindingInflater.invoke(layoutInflater)
                setContentView(binding!!.root)
            }
            return binding!!
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }