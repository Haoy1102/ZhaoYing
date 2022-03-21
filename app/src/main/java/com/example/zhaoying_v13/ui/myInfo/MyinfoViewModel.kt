package com.example.zhaoying_v13.ui.myInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zhaoying_v13.network.Report
import com.example.zhaoying_v13.network.ReportApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyinfoViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _report = MutableLiveData<Report>()

    val report: LiveData<Report>
        get() = _report

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
//        getReports()
    }

    private fun getReports() {
        coroutineScope.launch {
            var getReportDefferd = ReportApi.retrofitService.getProperties()
            var listResult = getReportDefferd.await()
            try {
                if (listResult.size > 0) {
                    _report.value = listResult[0]
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}