plugins {
    id("java")
    id("com.github.johnrengelman.shadow")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.4.8-jre")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveBaseName.set("gradleHelloOtus")
    archiveVersion.set("0.1")
    archiveClassifier.set("")
    manifest {
        attributes["Main-Class"] = "org.example.HelloOtus"
    }
}

tasks.build{
    dependsOn(tasks.shadowJar)
}