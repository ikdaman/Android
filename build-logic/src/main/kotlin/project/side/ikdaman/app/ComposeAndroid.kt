package project.side.ikdaman.app

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

internal fun Project.configureComposeAndroid() {
    with(plugins) {
        apply("org.jetbrains.kotlin.plugin.compose")
    }

    val libs = extensions.libs
    androidExtension.apply {
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))

            add("implementation", libs.findLibrary("androidx.compose.material3").get())
            add("implementation", libs.findLibrary("androidx.compose.ui").get())
            add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())

//            implementation(libs.findLibrary("androidx.navigation.runtime.ktx").get())
            add("implementation", libs.findLibrary("androidx.navigation.runtime.ktx").get())

            add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
            add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test").get())
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())
            add("debugImplementation", libs.findLibrary("androidx.compose.ui.test.manifest").get())
        }
    }

    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        /*
            includeSourceInformation은 Compose 함수의 소스 위치 정보를 포함할지 여부를 결정합니다.
         */
        includeSourceInformation.set(true)

        /*
            Strong Skipping은 특정 Compose 함수의 상태가 변하지 않았을 때,
            그 함수의 재구성을 완전히 건너뛰도록 최적화하는 기능입니다.
            이를 통해 성능이 개선될 수 있습니다.
         */
        featureFlags.addAll(ComposeFeatureFlag.StrongSkipping)
    }
}
