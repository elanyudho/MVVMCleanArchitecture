plugins {
    id("mvvm.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.elanyudho.core.network"
    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
    }
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.okhttp)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.napier)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(projects.core.base)
    implementation(projects.core.security)
}
