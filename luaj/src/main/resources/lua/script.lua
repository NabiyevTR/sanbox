function handle(document)
    print(document:getId())
    print(document:getAttributes())

    attr = document:getAttributes()

    document:setId("doc_id_lua")

    document:addValue("attr_3", "lua_lua")
    print(document:getValues("attr_3"))
    print(document:getValues("attr_99"))
    print(document:getValue("attr_3"))
    print(document:getValue("attr_99"))

    print(document:save("C:\\click\\lua\\1.txt"))
    print(document:save("C:\\click\\lua\\1.txt"))
    print(document:save("H:\\g.txt"))

end
