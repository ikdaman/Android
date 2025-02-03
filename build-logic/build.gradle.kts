plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.verify.detektPlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "ikdaman.android.hilt"
            implementationClass = "project.side.ikdaman.app.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "ikdaman.kotlin.hilt"
            implementationClass = "project.side.ikdaman.app.HiltKotlinPlugin"
        }
    }
}
