package com.miramir.mahfaze.di

import com.miramir.mahfaze.MahfazeApp
import com.miramir.mahfaze.data.worker.SyncWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface WorkComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MahfazeApp): Builder

        fun build(): WorkComponent
    }

    fun inject(syncWorker: SyncWorker)
}