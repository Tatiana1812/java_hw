dependencies {
    implementation(project(":hw09_jdbc:demo"))

    implementation ("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
}
