package ru.qa.interceptor

import android.app.Application
import kotlinx.coroutines.*
import ru.qa.interceptor.di.*
import kotlin.coroutines.CoroutineContext

class TestApplication: Application(), CoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = job +
                Dispatchers.Default +
                CoroutineName(baseContext.getString(R.string.app_name)) +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    println("Exception $throwable in context:$coroutineContext")
                }

    companion object {
        lateinit var component: AppComponent
    }

    private fun buildComponent(): AppComponent {
        return DaggerAppComponent
            .builder()
            .context(this)
            .coroutineContext(coroutineContext)
            .baseUrl(baseContext.getString(R.string.base_url))
            .dbName(baseContext.getString(R.string.db))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component = buildComponent()
    }

    override fun onTerminate() {
        super.onTerminate()
        job.cancel()
    }
}