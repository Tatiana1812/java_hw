plugins {
    id ("com.github.johnrengelman.shadow")
}

dependencies {
    implementation ("com.google.guava:guava")
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