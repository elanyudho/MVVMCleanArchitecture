plugins {
    id("mvvm.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.elanyudho.core.base"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}
