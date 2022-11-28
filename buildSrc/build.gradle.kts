plugins { `kotlin-dsl` }

group = "com.keelim.buildSrc"

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation("com.android.tools.build:gradle:7.3.1")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
}
