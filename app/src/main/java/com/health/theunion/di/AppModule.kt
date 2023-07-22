package com.health.theunion.di

import android.content.Context
import androidx.room.Room
import com.health.theunion.common.Constants
import com.health.theunion.data.he_activity.HeActivityHistoryDatabase
import com.health.theunion.data.patient_referral.PatientReferralHistoryDatabase
import com.health.theunion.data.user.AppUserDatabase
import com.health.theunion.repository.he_activity.HeActivityHistoryRepository
import com.health.theunion.repository.he_activity.HeActivityHistoryRepositoryImpl
import com.health.theunion.repository.patient_referral.PatientReferralHistoryRepository
import com.health.theunion.repository.patient_referral.PatientReferralHistoryRepositoryImpl
import com.health.theunion.repository.user.AppUserRepository
import com.health.theunion.repository.user.AppUserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesReferralDb(
        @ApplicationContext context: Context
    ): PatientReferralHistoryDatabase {
        return Room.databaseBuilder(
            context,
            PatientReferralHistoryDatabase::class.java,
            Constants.DB_NAME
        )
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesHeActivityDb(
        @ApplicationContext context: Context
    ): HeActivityHistoryDatabase {
        return Room.databaseBuilder(
            context,
            HeActivityHistoryDatabase::class.java,
            Constants.DB_NAME
        )
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesUserDb(
        @ApplicationContext context: Context
    ): AppUserDatabase {
        return Room.databaseBuilder(
            context,
            AppUserDatabase::class.java,
            Constants.USER_TABLE_NAME
        )
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesPatientReferralRepo(
        db: PatientReferralHistoryDatabase
    ): PatientReferralHistoryRepository {
        return PatientReferralHistoryRepositoryImpl(
            db = db
        )
    }

    @Provides
    @Singleton
    fun providesHeActivityRepo(
        db: HeActivityHistoryDatabase
    ): HeActivityHistoryRepository {
        return HeActivityHistoryRepositoryImpl(
            db = db
        )
    }

    @Provides
    @Singleton
    fun providesUserRepo(
        db: AppUserDatabase
    ): AppUserRepository {
        return AppUserRepositoryImpl(
            db = db
        )
    }


}