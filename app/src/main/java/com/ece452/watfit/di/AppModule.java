package com.ece452.watfit.di;

import androidx.room.Room;

import com.ece452.watfit.data.source.local.AppDatabase;
import com.ece452.watfit.data.source.remote.SpoonacularDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    @Singleton
    @Provides
    static public FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Singleton
    @Provides
    static public FirebaseUser provideFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
