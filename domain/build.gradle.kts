plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("kotlin-kapt")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.javax.inject)
    // kotlin flow
    implementation(libs.coroutines.core)

    implementation(libs.hilt.core)
    testImplementation(libs.junit)
    kapt(libs.hilt.compiler)
}