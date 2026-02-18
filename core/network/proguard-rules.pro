# =============================================================================
# Proguard Rules for core:network
# Covers: Ktor, OkHttp, kotlinx.serialization
# =============================================================================

# --- Ktor ---
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Ktor uses reflection for engine selection
-keep class io.ktor.client.engine.** { *; }
-keep class io.ktor.client.plugins.** { *; }

# --- OkHttp ---
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# OkHttp platform adapters
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# --- kotlinx.serialization ---
# Keep @Serializable annotated classes
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
}

-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep generated serializers
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class ** {
    static ** Companion;
}
-keepclassmembers class <2>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

# --- Napier Logging ---
-dontwarn io.github.aakira.napier.**
