package com.mohistmc.tasks

import groovy.json.JsonSlurper

import java.nio.file.Files
import java.nio.file.Path

class Env {
    private static final data
    private static final defaultData
    static {
        def envFile = new File("env.json");
        def defaultFile = new File("env.default.json");
        if (!envFile.exists()) {
            Files.copy(
                    Path.of("buildSrc/src/main/resources/env.generate.json"),
                    Path.of("env.json")
            )
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
