rootProject.name = "java_hw"
include(":hw01_gradle")
include(":hw02_generics")
include(":hw03_annotations")
include("hw05_byteCodes")

pluginManagement {
    val dependencyManagement: String by settings
    val johnrengelmanShadow: String by settings
    val springframeworkBoot: String by settings

    plugins {
        id("org.springframework.boot") version springframeworkBoot
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}