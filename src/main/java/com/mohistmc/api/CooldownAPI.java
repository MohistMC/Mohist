package com.mohistmc.api;

import java.time.Duration;
import java.time.LocalDateTime;

public class CooldownAPI {

    private final LocalDateTime old;
    private final LocalDateTime Now;

    public CooldownAPI(LocalDateTime old, LocalDateTime Now) {
        this.old = old;
        this.Now = Now;
    }

    public boolean isAfter(){
        return Now.isAfter(old);
    }

    public long timeLeft() {
        Duration duration = Duration.between(LocalDateTime.now(), old);
        return duration.getSeconds();
    }
}
