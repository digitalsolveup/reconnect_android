package com.mobile.reconnect.ui.my.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.reconnect.data.model.MyReportList
import com.mobile.reconnect.data.model.enumeration.Status

class ReportViewModel : ViewModel() {

    private val _reports = MutableLiveData<List<MyReportList>>()
    val reports: LiveData<List<MyReportList>> get() = _reports

    fun fetchReports() {
    }
}
