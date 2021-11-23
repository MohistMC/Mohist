package com.mohistmc.tasks

import groovy.json.JsonSlurper

import java.nio.file.Files

class Env {
    private static final data
    private static final defaultData
    static {
        def envFile = new File("env.json");
        def defaultFile = new File("env.default.json");
        if (!envFile.exists()) {
            def generateFile = new File("buildSrc/src/main/resources/env.generate.json")
            Files.copy(
                    generateFile.toPath(),
                    envFile.toPath()
            )
            // Don't use Path.of(), It works incorrectly in Github actions
        }
        def jsonSluper = new JsonSlurper()
        data = jsonSluper.parse(new FileReader(envFile))
        defaultData = jsonSluper.parse(new FileReader(defaultFile))
    }

    static get(String key){
        def result = data.get(key)
        if(result == null){
            result = System.getenv(key)
        }
        if(result == null){
            result = defaultData[key]
        }
        return result
    }
}