package ru.qa.interceptor.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.qa.interceptor.interceptor.RequestResponseInterceptor
import ru.qa.interceptor.db.RequestResponseDAO
import ru.qa.interceptor.db.RequestResponseDB
import ru.qa.interceptor.examplenetworkservice.NetworkService
import ru.qa.interceptor.examplenetworkservice.PlaceHolderApi
import ru.qa.interceptor.fragments.RequestListFragment
import ru.qa.interceptor.viewmodel.RequestViewModel
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Qualifier
annotation class BaseUrl
@Qualifier
annotation class DBName


@Component(modules = [RestApiModule::class, StorageModule::class])
@Singleton
interface AppComponent{
    fun injectToFragment(requestListFragment: RequestListFragment)
    fun injectToViewModel(requestViewModel: RequestViewModel)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(context: Context): Builder
        @BindsInstance
        fun coroutineContext(coroutineContext: CoroutineContext): Builder
        @BindsInstance
        fun baseUrl(@BaseUrl baseUrl: String): Builder
        @BindsInstance
        fun dbName(@DBName dbName: String): Builder

        fun build(): AppComponent
    }
}

@Module
class RestApiModule {
    @Provides
    @Singleton
    internal fun provideRetrofit(interceptor: RequestResponseInterceptor, @BaseUrl baseUrl: String): PlaceHolderApi {
        return NetworkService.getInstance(interceptor, baseUrl)
    }
}

@Module
class StorageModule {
    @Provides
    @Singleton
    internal fun provideRequestResponseRoomDatabase(context: Context, @DBName dbName: String): RequestResponseDAO {
        return  RequestResponseDB.getDataBase(context, dbName).getDao()
    }
}