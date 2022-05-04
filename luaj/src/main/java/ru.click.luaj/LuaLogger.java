package ru.click.luaj;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LuaLogger {

    public void warn(String s) {
        log.warn(s);
    }

    public void error(String s) {
        log.error(s);
    }

    public void info(String s) {
        log.info(s);
    }

}
