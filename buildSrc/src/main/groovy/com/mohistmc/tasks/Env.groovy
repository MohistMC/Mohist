package com.mohistmc.tasks

import groovy.json.JsonSlurper

import java.util.function.Supplier

class Env {
    private static final data
    private static final defaultData
    static {
        def envFile = new File("env.json");
        def defaultFile = new File("env.default.json");
        if (!envFile.exists()) {
            new FileOutputStream("env.json").write(
                    getClass().getResourceAsStream("env.generate.json").readAllBytes()
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
