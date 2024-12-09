package vn.trunglt.demo_compose_navigation

import android.app.Application

class MainApplication : Application() {
    companion object {
        private var _instance: MainApplication? = null
        val instance: MainApplication
            get() = _instance!!
    }

    override fun onCreate() {
        super.onCreate()
        _instance = this
    }
}