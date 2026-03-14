plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("com.google.code.gson:gson")
    implementation("org.hibernate.orm:hibernate-core")
    implementation("org.flywaydb:flyway-core")
    implementation ("jakarta.validation:jakarta.validation-api:3.0.2")

    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.postgresql:postgresql")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    implementation ("org.flywaydb:flyway-database-postgresql:10.4.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.14")
}