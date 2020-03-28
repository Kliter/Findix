package com.kl.findix.util.delegate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kl.findix.util.FindixError
import com.kl.findix.util.UiState

interface UiStateViewModelDelegate {
    val uiState: MutableLiveData<UiState>
    val showErrorAlertCommand: LiveData<String>
    fun handleError(exception: Exception)
}

class UiStateViewModelDelegateImpl: UiStateViewModelDelegate {

    override val uiState: MutableLiveData<UiState> = MutableLiveData()
    override val showErrorAlertCommand: MutableLiveData<String> = MutableLiveData()

    override fun handleError(exception: Exception) {
        when (exception) {
            is FindixError.NetworkError -> {
                uiState.postValue(UiState.Retry)
            }
            is FindixError.UndefinedError -> {
                uiState.postValue(UiState.Error)
                showErrorAlertCommand.postValue(exception.alertMessage)
            }
            else -> {
                uiState.postValue(UiState.Error)
                exception.message?.let { message ->
                    showErrorAlertCommand.postValue(message)
                }
            }
        }
    }
}