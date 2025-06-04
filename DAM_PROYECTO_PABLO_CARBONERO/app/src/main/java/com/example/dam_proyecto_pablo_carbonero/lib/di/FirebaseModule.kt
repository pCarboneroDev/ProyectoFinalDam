package com.example.dam_proyecto_pablo_carbonero.lib.di

import com.example.dam_proyecto_pablo_carbonero.lib.data.firebase.FirebaseDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseDatasource(): FirebaseDatasource {
        return FirebaseDatasource()
    }
}