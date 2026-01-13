import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES


plugins {
    idea
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(21)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects {
    group = "org.example"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val guava: String by project
    val testcontainersBom: String by project
    val protobufBom: String by project
    val grpc: String by project

    val sockjs: String by project
    val stomp: String by project
    val bootstrap: String by project
    val jsr305: String by project
    val wiremock: String by project
    val r2dbcPostgresql: String by project


    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("io.grpc:grpc-netty:$grpc")
            dependency("io.grpc:grpc-protobuf:$grpc")
            dependency("io.grpc:grpc-stub:$grpc")
            dependency("org.webjars:sockjs-client:$sockjs")
            dependency("org.webjars:stomp-websocket:$stomp")
            dependency("org.webjars:bootstrap:$bootstrap")
            dependency("com.github.tomakehurst:wiremock-standalone:$wiremock")
            dependency("io.r2dbc:r2dbc-postgresql:$r2dbcPostgresql")
            dependency("com.google.code.findbugs:jsr305:$jsr305")
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-parameters", "-Xlint:all,-serial,-processing"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}