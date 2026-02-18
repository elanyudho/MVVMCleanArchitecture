import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for library modules that use Jetpack Compose.
 * 
 * Apply AFTER mvvm.android.library:
 *   plugins {
 *       id("mvvm.android.library")
 *       id("mvvm.android.library.compose")
 *   }
 * 
 * This adds:
 *   - Kotlin Compose compiler plugin
 *   - buildFeatures.compose = true
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}
