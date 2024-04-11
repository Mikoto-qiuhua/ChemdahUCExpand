plugins {
    id("java")
}

group = "org.qiuhua.chemdahucexpand"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()  //加载本地仓库
    mavenCentral()  //加载中央仓库
    maven("https://jitpack.io")
    maven {
        name = "spigotmc-repo"
        url = uri ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }  //SpigotMC仓库
    //mm的仓库
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly ("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly(fileTree("src/libs"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar>().configureEach {
    archiveFileName.set("ChemdahUCExpand-测试插件.jar")
    destinationDirectory.set(File ("D:\\Server-1.20.2\\plugins"))
}

tasks.withType<JavaCompile>{
    options.encoding = "UTF-8"
}