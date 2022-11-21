import org.jetbrains.kotlin.gradle.plugin.statistics.ReportStatisticsToElasticSearch.url
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool.generate
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Property
import org.jooq.meta.jaxb.Target

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("org.liquibase.gradle") version "2.1.1"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.example.cockroach"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath("org.jooq:jooq-codegen:3.17.5")
		classpath("org.postgresql:postgresql:42.5.0")
		classpath("org.jooq:jooq-meta-extensions:3.17.5")
	}
}

repositories {
	google()
	mavenCentral()
}
extra["testcontainersVersion"] = "1.17.4"

dependencies {
	implementation("org.jooq:jooq:3.17.5")
	implementation("org.jooq:jooq-codegen:3.17.5")
	implementation("org.jooq:jooq-meta-extensions:3.17.5")

	implementation("org.jooq:jooq-meta:3.17.5")
	implementation("org.jooq:jooq-kotlin-coroutines:3.17.5")
	implementation("org.jooq:jooq-kotlin:3.17.5")

	// r2dbc
	runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
	runtimeOnly("io.r2dbc:r2dbc-spi:1.0.0.RELEASE")
	runtimeOnly("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")
	runtimeOnly("org.postgresql:postgresql:42.5.0")

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.liquibase:liquibase-core")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	testImplementation("org.testcontainers:testcontainers:1.17.5")
	testImplementation("org.testcontainers:cockroachdb:1.17.5")
	testImplementation("org.testcontainers:junit-jupiter")

	// Liquibase
	add("liquibaseRuntime", "org.liquibase:liquibase-core:4.11.0")
	add("liquibaseRuntime", "org.liquibase:liquibase-gradle-plugin:2.1.1")
	add("liquibaseRuntime", "info.picocli:picocli:4.6.3")
	add("liquibaseRuntime", "org.postgresql:postgresql:42.5.0")
	add("liquibaseRuntime", "org.yaml:snakeyaml:1.30")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	withType<Test> {
		useJUnitPlatform()
	}

	val generateJooq by registering {
		doLast {
			generateJooq()
		}
	}
	named("build") {
		dependsOn(":generateJooq")
	}
	named("compileKotlin") {
		dependsOn(":generateJooq")
	}
}

liquibase {
	data class ActivityOptions(
		val name: String,
		val arguments: Map<String, String>
	)
	val activities = listOf(
		ActivityOptions(
			name = "main",
			arguments = mapOf(
				"logLevel" to "info",
				"changeLogFile" to "src/main/resources/db/changelog.sql",
				"url" to "jdbc:postgresql://localhost:26257/test",
				"username" to "root"
			)
		)
	)
	activities {
		activities.forEach {
			register(it.name) { this.arguments = it.arguments }
		}
	}
	runList = project.ext.properties["runList"]
}

val projectDir = rootProject.projectDir.toString().trimEnd { it == '/' }
val jooqGenerationTargetDir = "$projectDir/build/generated/sources/jooq"
sourceSets["main"].java.srcDir(jooqGenerationTargetDir)

fun generateJooq() {
	Configuration().apply {
		generator = Generator().apply {
			database = Database().apply {
				name = "org.jooq.meta.extensions.ddl.DDLDatabase"
				properties = listOf(
					Property()
						.withKey("scripts")
						.withValue("$projectDir/src/main/resources/db/changelog.sql"),
					Property()
						.withKey("sort")
						.withValue("alphanumeric"),
					Property()
						.withKey("defaultNameCase")
						.withValue("lower")
				)
			}
			generate = Generate().apply {
				isDeprecationOnUnknownTypes = false
				isJavaTimeTypes = true
			}
			target = Target().apply {
				packageName = "database.schema"
				directory = jooqGenerationTargetDir
			}
		}
	}.also {
		generate(it)
	}
}
