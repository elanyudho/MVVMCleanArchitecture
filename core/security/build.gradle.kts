plugins {
    id("mvvm.android.library")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.elanyudho.core.security"
    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // DataStore
    implementation(libs.datastore.core)

    // Protobuf
    implementation(libs.protobuf.javalite)

    // Tink (Encryption)
    implementation(libs.tink.android)

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
