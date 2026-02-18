# =============================================================================
# Proguard Rules for core:security
# Covers: Google Tink, Protobuf, Proto DataStore
# =============================================================================

# --- Google Tink ---
-keep class com.google.crypto.tink.** { *; }
-dontwarn com.google.crypto.tink.**
-keep class com.google.errorprone.annotations.** { *; }
-dontwarn com.google.errorprone.annotations.**

# --- Protobuf (Lite) ---
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

# Keep all generated proto classes
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# Keep proto enum values (used by reflection in proto parsing)
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    public static ** getDefaultInstance();
    public static ** parseFrom(byte[]);
    public static ** parseFrom(java.io.InputStream);
    public ** toBuilder();
    public ** newBuilderForType();
}

# Keep fields annotated with proto SerializedName
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
}

# --- DataStore ---
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# --- Project-specific: Keep AuthPreferences proto ---
-keep class com.elanyudho.core.security.AuthPreferences { *; }
-keep class com.elanyudho.core.security.AuthPreferences$Builder { *; }
