import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    /*iosX64()
    iosArm64()
    iosSimulatorArm64()*/

    jvm()

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.ktor.serverAuth)
            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.clientCio)
            implementation(libs.ktor.clientContentNegotiation)
            implementation(libs.ktor.serializationKotlinxJson)
            dependencies { implementation("androidx.browser:browser:1.7.0") }
            implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

            implementation(libs.koin.core)


            //implementation(libs.androidx.credentials)
            //implementation(libs.androidx.credentials.play.services.auth)
            //implementation(libs.googleid)
            //implementation(libs.play.services.auth)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "de.frinshhd.streamlineai.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
