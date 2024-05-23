// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

// build.gradle.kts (no nível do projeto)
buildscript {
    dependencies {
        // Certifique-se de que esta é a versão mais recente do Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21") // Certifique-se de que a versão esteja correta
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}