function handle(document, log)
    print(document:getId())
    print(document:getAttributes())
    attr = document:getAttributes()
    document:setId("doc_id_lua")
    document:addValue("attr_3", "lua_lua")
    document:addValue("lua", "lua_lua")
    print(document:getValues("attr_3"))
    print(document:getValues("attr_99"))
    print(document:getValue("attr_3"))
    print(document:getValue("attr_99"))
    print(document:save("C:\\click\\lua\\1.txt"))
    print(document:save("C:\\click\\lua\\1.txt"))
    print(document:save("H:\\g.txt"))
    save(document,"C:\\click\\lua\\2.txt")

    log:info("Hello from script")
end

function save(document, path)
    document:save(path)
end