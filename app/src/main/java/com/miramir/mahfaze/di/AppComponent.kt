package com.miramir.mahfaze.di

import com.miramir.mahfaze.MahfazeApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            BuildersModule::class,
            ViewModelModule::class
        ]
)
interface AppComponent : AndroidInjector<MahfazeApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MahfazeApp>()
}