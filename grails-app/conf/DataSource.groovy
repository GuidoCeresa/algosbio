def dataBase = 'wiki'

dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    //dialect = "org.hibernate.dialect.MySQLMyISAMDialect"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
    username = "root"
    password = ""
} // end of dataSource

hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
} // end of hibernate

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost/${dataBase}?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true"
        } // end of dataSource
    } // end of development

    test {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost/${dataBase}?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true"
//            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        } // end of dataSource
    } // end of production

    production {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost/${dataBase}?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true"
        } // end of dataSource
    } // end of test
} // end of environments
