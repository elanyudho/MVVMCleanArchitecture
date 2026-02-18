// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Note: AGP and Kotlin plugins are already on the classpath via buildSrc,
// so we only declare additional plugins here.
plugins {
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.protobuf) apply false
}