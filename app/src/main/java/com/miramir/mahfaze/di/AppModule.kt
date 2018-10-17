package com.miramir.mahfaze.di

import androidx.room.Room
import com.miramir.mahfaze.MahfazeApp
import com.miramir.mahfaze.data.local.MahfazeDatabase
import com.miramir.mahfaze.data.remote.NoteService
import com.miramir.mahfaze.data.remote.UserService
import com.miramir.mahfaze.util.AuthenticationInterceptor
import com.miramir.mahfaze.util.PreferencesManager
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClientBuilder() = OkHttpClient.Builder()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://mahfaze.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideNoteService(
            mahfazeApp: MahfazeApp,
            okHttpClientBuilder: OkHttpClient.Builder,
            retrofitBuilder: Retrofit.Builder
    ): NoteService = retrofitBuilder
            .client(okHttpClientBuilder
                    .addInterceptor(AuthenticationInterceptor(Credentials.basic(
                            PreferencesManager.getUserEmail(mahfazeApp)!!,
                            PreferencesManager.getUserPassword(mahfazeApp)!!
                    )))
                    .build())
            .build()
            .create(NoteService::class.java)

    @Provides
    @Singleton
    fun provideUserService(
            okHttpClientBuilder: OkHttpClient.Builder,
            retrofitBuilder: Retrofit.Builder
    ): UserService = retrofitBuilder
            .client(okHttpClientBuilder.build())
            .build()
            .create(UserService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(mahfazeApp: MahfazeApp) = Room.databaseBuilder(mahfazeApp,
            MahfazeDatabase::class.java, "mahfaze.db").build()

    @Provides
    @Singleton
    fun provideNoteDao(database: MahfazeDatabase) = database.noteDao()
}