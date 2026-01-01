rootProject.name = "java_hw"
include(":hw01_gradle")
include(":hw02_generics")
include(":hw03_annotations")
include("hw04_gc")
include("hw05_byteCodes")
include("hw06_oop")
include("hw07_patterns")
include("hw08_serialization")
include("hw09_jdbc")
include("hw09_jdbc:homework")
findProject(":hw09_jdbc:homework")?.name = "homework"
include("hw09_jdbc:demo")
findProject(":hw09_jdbc:demo")?.name = "demo"
include("hw10_jpql")
include("hw11_cache")
include("hw12_web")
include("hw13_di")
include("hw15_executors")

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
