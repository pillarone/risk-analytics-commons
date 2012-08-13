//Use a custom plugins dir, because different branches use different plugin versions
grails.project.plugins.dir = "../local-plugins/risk-analytics-commons-master"

grails.project.dependency.resolution = {
    inherits "global" // inherit Grails' default dependencies
    log "warn"

    repositories {
        grailsHome()
        grailsCentral()

        mavenRepo "https://repository.intuitive-collaboration.com/nexus/content/repositories/pillarone-public/"
    }

    plugins {
        runtime ":background-thread:1.3"
        runtime ":hibernate:2.1.0"
        runtime ":joda-time:0.5"
        runtime ":release:2.0.3"
        runtime ":quartz:0.4.2"
        runtime ":spring-security-core:1.2.7.3"
        runtime ":tomcat:2.1.0"

        compile(":excel-import:0.9.6") { excludes 'xmlbeans' }
        test ":code-coverage:1.2.4"

        if (appName == "risk-analytics-commons") {
            runtime "org.pillarone:risk-analytics-core:1.6-ALPHA-4.10-2.0.1"
        }
    }
}

grails.project.dependency.distribution = {
    String password = ""
    String user = ""
    String scpUrl = ""
    try {
        Properties properties = new Properties()
        properties.load(new File("${userHome}/deployInfo.properties").newInputStream())

        user = properties.get("user")
        password = properties.get("password")
        scpUrl = properties.get("url")
    } catch (Throwable t) {
    }
    remoteRepository(id: "pillarone", url: scpUrl) {
        authentication username: user, password: password
    }
}

coverage {
    enabledByDefault = false
    xml = true
    exclusions = [
            'models/**',
            '**/*Test*',
            '**/com/energizedwork/grails/plugins/jodatime/**',
            '**/grails/util/**',
            '**/org/codehaus/**',
            '**/org/grails/**',
            '**GrailsPlugin**',
            '**TagLib**'
    ]

}