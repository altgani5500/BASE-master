package com.parttime.enterprise.uicomman.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parttime.enterprise.R
import com.parttime.enterprise.uicomman.BaseFragment

class DummyFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragemenyt_dummy, container, false)

        return view
    }

}