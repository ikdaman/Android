plugins {
    id("ikdaman.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "project.side.ikdaman"

    defaultConfig {
        applicationId = "project.side.ikdaman"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.main)
    implementation(projects.core)

    implementation(libs.androidx.core.ktx)

}