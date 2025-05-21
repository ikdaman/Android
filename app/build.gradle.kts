import java.util.Properties

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

        val properties = Properties()
        properties.load(project.rootProject.file("key.properties").inputStream())

        val kakaoAppKey = properties.getProperty("KAKAO_APP_KEY")
        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        manifestPlaceholders["KAKAO_APP_KEY"] = kakaoAppKey
    }

    buildFeatures {
        buildConfig = true
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
    implementation(projects.feature)
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.data)

    implementation(libs.androidx.core.ktx)

    implementation(libs.kakao.login)
}