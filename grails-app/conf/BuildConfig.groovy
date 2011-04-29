//Use a custom plugins dir, because different branches use different plugin versions
grails.project.plugins.dir = "../local-plugins/risk-analytics-commons-master"

grails.project.dependency.resolution = {
    inherits "global" // inherit Grails' default dependencies
    log "warn"

    repositories {
        grailsHome()
        grailsCentral()
    }

    mavenRepo "https://build.intuitive-collaboration.com/maven/plugins/"

    plugins {
        runtime ":hibernate:1.3.7"
        runtime ":tomcat:1.3.7"

        test ":code-coverage:1.2.2"

//        if (appName == "risk-analytics-commons") {
//            runtime "org.pillarone:risk-analytics-core:1.4-ALPHA-1.8"
//        }
    }
}

grails.project.dependency.distribution = {
    String passPhrase = ""
    String scpUrl = ""
    try {
        Properties properties = new Properties()
        properties.load(new File("${userHome}/deployInfo.properties").newInputStream())

        passPhrase = properties.get("passPhrase")
        scpUrl = properties.get("url")
    } catch (Throwable t) {
    }
    remoteRepository(id: "pillarone", url: scpUrl) {
        authentication username: 'root', privateKey: "${userHome.absolutePath}/.ssh/id_rsa", passphrase: passPhrase
    }
}
