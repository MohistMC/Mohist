package com.mohistmc.tasks

import groovy.json.JsonSlurper

class JenkinsNumber {

    static String info() {
        try {
            def data = new JsonSlurper().parseText(new URL("https://ci.codemc.io/job/MohistMC/job/Mohist-1.20.1/api/json").getText("UTF-8"))
            return data.builds["number"][0]
        } catch (Exception ignored) {
            return 'dev'
        }
    }
}
