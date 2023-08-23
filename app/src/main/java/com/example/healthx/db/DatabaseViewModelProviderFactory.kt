package com.example.healthx.db

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthx.repository.UserRepository

class DatabaseViewModelProviderFactory(
    val app: Application,
    private val repository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DatabaseViewModel(app, repository) as T
    }

}