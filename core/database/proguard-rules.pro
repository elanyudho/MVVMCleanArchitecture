# =============================================================================
# Proguard Rules for core:database
# Covers: Room
# =============================================================================

# --- Room ---
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Room generated implementations
-keep class *_Impl { *; }

# Keep Room migration classes
-keep class * extends androidx.room.migration.Migration { *; }

# Keep TypeConverters
-keep class * {
    @androidx.room.TypeConverter *;
}
