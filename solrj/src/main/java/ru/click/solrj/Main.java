package ru.click.solrj;

public class Main {

    public static void main(String[] args) {

        SolrJTestService service = new SolrJTestService();
        // service.addDoc("person_2");
        // service.removeDoc("person_1");
        // service.addNested("person_1","person_200");
        // service.atomicUpdate("add", "person_1", "person_200");
        // service.atomicUpdate("add", "person_1", "person_201");
        // service.atomicUpdate("add", "person_1", "person_203");

        // service.atomicUpdate("add", "person_1", "person_203", "person_204");

        // service.atomicUpdate("add", "person_1", "person_205");
        // service.atomicUpdate("add", "person_1", "person_206");

        // service.atomicUpdate("remove", "person_1", "person_203");

        //  service.atomicUpdate("add", "person_1", "person_205", "person_221");
        //  service.atomicUpdate("add", "person_1", "person_205", "person_222");
        //  service.atomicUpdate("add", "person_1", "person_205", "person_223");

        // service.atomicRemove("person_1", "person_703");

        // service.atomicRemove("person_1", "person_1", "person_902");

        // service.atomicUpdate("remove", "person_1", "person_203");

        // service.atomicUpdate("add", "person_1", "person_705");

        service.atomicUpdate("add-distinct", "person_1", "person_902");
        service.atomicUpdate("add-distinct", "person_1", "person_902");
        service.atomicUpdate("add-distinct", "person_1", "person_902");
        service.atomicUpdate("add", "person_1", "person_902", "person_1100");
        service.atomicUpdate("add", "person_1", "person_902", "person_1100");
        service.atomicUpdate("add", "person_1", "person_902", "person_1100");

        /**
         * Выводы по использованию библиотеки SolrJ:
         * 1) При обновлении родительского документа, дочерние удаляются
         * 2) При удалении родительского документа, дочерние удаляются
         * 3) Дочерний документ нельзя удалить по Id
         * 4) Дочерний документ можно удалить только через remove при редактировании основного документа.
         * 5) Дочерний документ добавляется в ту же коллекцию
         * 6) Если уровлень вложенности > 1, то помимо родительского id нужно указывать идентификатор
         * родительского документа _root_, при уровне вложенности 1 это не обязательно. Это относится ко всем операциям
         * 7) Если дочерний документ содержит поле, которого нет в схеме, то вознкнет ошибка, даже в режиме
         * schemaless
         * 8) Для того чтобы удалить дочерний элемент через remove, нужно создать документ с ОДНИМ параметром (id).
         * Если параметров будет больше, то удаление не произойдет.
         * 9) Если использовать add для добавления вложенных документов с одинаковым id, будут создано несколько документов
         * с одинаковыми id. Конфликта не будет, что странно. Если добавить вложенный документ к документам с одинаковым id,
         * то документ будет добавлен только к одному из них (к первому добавленному). Если вызвать команду remove,
         * то при каждом вызове будет удален только один документ.
         *
         * */
    }

}



