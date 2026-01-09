plugins {
    id("org.springframework.boot")
}


dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")
    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.flywaydb:flyway-core")

    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    implementation("org.freemarker:freemarker")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")

    implementation("org.postgresql:postgresql")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    implementation ("org.flywaydb:flyway-database-postgresql:10.4.1")

}
