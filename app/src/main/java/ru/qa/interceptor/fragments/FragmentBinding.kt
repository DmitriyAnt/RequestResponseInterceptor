package ru.qa.interceptor.fragments

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class FragmentBinding : Fragment() {

    protected var binding: ViewBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}