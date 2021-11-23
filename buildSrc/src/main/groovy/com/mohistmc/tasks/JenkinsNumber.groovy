package com.mohistmc.tasks

import groovy.json.JsonSlurper

class JenkinsNumber {

    static String info() {
        if(Env.BUILD_NAME != ""){
            return Env.BUILD_NAME
        }
        try {
            def conn = new URL(Env.JENKINS_API_URL).openConnection()
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0")
            conn.connect()
            def root = conn.content.text
            def jsonSluper = new JsonSlurper()

            String data = jsonSluper.parseText(root)
            def number = data.substring(data.indexOf("number")).split(",")
            return Integer.valueOf(number[0].replace("number=", "")).intValue()
        } catch (Exception e1) {
            return 'dev'
        }
    }
}
