package com.ece452.watfit.di;

import androidx.room.Room;

import com.ece452.watfit.data.source.local.AppDatabase;
import com.ece452.watfit.data.source.remote.SpoonacularDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Singleton
    @Provides
    static public AppDatabase provideAppDatabase() {
        return Room.databaseBuilder(com.ece452.watfit.MainApplication.getInstance().getApplicationContext(),
                AppDatabase.class, "watfit-db").build();
    }

    @Singleton
    @Provides
    static public SpoonacularDataSource provideSpoonacularDataSource() {
        return new SpoonacularDataSource();
    }
}
