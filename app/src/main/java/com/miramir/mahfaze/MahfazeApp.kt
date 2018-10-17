package com.miramir.mahfaze

import com.jakewharton.threetenabp.AndroidThreeTen
import com.miramir.mahfaze.di.DaggerAppComponent
import com.miramir.mahfaze.di.DaggerWorkComponent
import com.miramir.mahfaze.di.WorkComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MahfazeApp : DaggerApplication() {
    lateinit var workComponent: WorkComponent
        private set

    override fun onCreate() {
        super.onCreate()

        workComponent = DaggerWorkComponent
                .builder()
                .application(this)
                .build()

        AndroidThreeTen.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder().create(this)
}