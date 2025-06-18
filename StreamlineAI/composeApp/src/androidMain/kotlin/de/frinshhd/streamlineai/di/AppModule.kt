package de.frinshhd.streamlineai.di

import androidx.credentials.CredentialManager
import de.frinshhd.streamlineai.BuildConfig
import de.frinshhd.streamlineai.auth.GoogleAuthProvider
import de.frinshhd.streamlineai.auth.GoogleAuthProviderImpl
import de.frinshhd.streamlineai.auth.models.GoogleAuthCredentials
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val platformModule: Module
    get() = module {
        single { CredentialManager.create(androidContext()) }
        single<GoogleAuthProvider> {
            GoogleAuthProviderImpl(
                GoogleAuthCredentials(BuildConfig.GOOGLE_CLIENT_ID),
                get()
            )
        }
    }