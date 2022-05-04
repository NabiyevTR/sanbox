package ru.click.luaj;

import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.*;

import javax.script.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;


@Slf4j
public class LuaScriptEngine {

    public void process() {
        try {
            String resource = getClass().getResource("/lua/script.lua").getPath();
            Reader reader = new FileReader(resource);

            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine engine = scriptEngineManager.getEngineByExtension(".lua");

            CompiledScript script = ((Compilable) engine).compile(reader);
            Bindings bindings = new SimpleBindings();
            script.eval(bindings);

            Document doc = new Document();
            LuaLogger logger = new LuaLogger();

            LuaFunction handle = (LuaFunction) bindings.get("handle");
            LuaValue luaDoc = CoerceJavaToLua.coerce(doc);
            LuaValue luaLog = CoerceJavaToLua.coerce(logger);

            LuaValue result = handle.call(luaDoc, luaLog);

            log.info("Function result: {}", result);
            log.info("Doc: {}", doc);

        } catch (ScriptException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
