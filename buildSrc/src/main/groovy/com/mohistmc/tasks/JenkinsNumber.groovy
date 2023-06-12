package com.mohistmc.tasks

import groovy.json.JsonSlurper

class JenkinsNumber {

    static String info() {
        try {
            def conn = new URL("https://ci.codemc.io/job/MohistMC/job/Mohist-1.20.1/api/json").openConnection()
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
            conn.connect()
            def root = conn.content.text
            def jsonSluper = new JsonSlurper()

            String data = jsonSluper.parseText(root)
            def number = data.substring(data.indexOf("number")).split(",")
            return Integer.valueOf(number[0].replace("number=", "")).intValue()
        } catch (Exception ignored) {
            return '1.20.1'
        }
    }
}
