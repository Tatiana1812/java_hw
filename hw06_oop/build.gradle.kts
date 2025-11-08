dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation ("com.google.guava:guava")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.assertj:assertj-core")
}