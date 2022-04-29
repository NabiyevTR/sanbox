package ru.click.luaj;

import javax.script.*;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;

public class Main {

    public static void main(String[] args) {

        LuaScriptEngine se = new LuaScriptEngine();
        se.process();

    }

}
