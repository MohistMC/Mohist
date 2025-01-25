package com.mohistmc.tasks

class BuildNumber {

    static String string() {
        try {
            return "git rev-list --count HEAD".execute().text.trim()
        } catch (Exception ignored) {
            return 'dev'
        }
    }
}
