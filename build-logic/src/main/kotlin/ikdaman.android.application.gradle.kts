import project.side.ikdaman.app.configureHiltAndroid
import project.side.ikdaman.app.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()