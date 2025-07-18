plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("io.projectreactor:reactor-test:3.6.5")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
	implementation("io.github.springwolf:springwolf-ui:1.12.0")
	implementation("io.github.springwolf:springwolf-stomp:1.12.0")
	implementation("link.honeycombpizza:stomptester"){
		version{
			branch = "main"
		}
	} // Ensure this dependency is available in your repository
}

tasks.withType<Test> {
	useJUnitPlatform()
}
