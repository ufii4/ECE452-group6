package com.ece452.watfit.di;

import com.ece452.watfit.data.source.local.AppDatabase;
import com.ece452.watfit.data.DietaryRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DietaryRepositoryModule {
    @Singleton
    @Provides
    static public DietaryRepository provideDietaryRepository(AppDatabase db) {
        return new DietaryRepository(db.dietaryLogDao(), db.dietaryLogEntryDao());
    }
}
