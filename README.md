# skytecgame Тестовое задание.

Старт осуществляется из класса Main.

Там же можно указать к какой базе необходимо подключаться для сохранения данных.

В приложении использовалось асинхронное обновление данных в БД.

При старте данные о клане обновляются по данным из БД.

Во время работы главные потоки используют кеш память для быстродействия. 

Все действия записываются в очередь, после чего из этой очереди пачками записываются в БД.

Для использования PostgreSql базу данных в классе PostgresJDBCConnection необходимо указать данные для подключения и в Main методе указать JDBCConnection jdbcConnection = new PostgresJDBCConnection();
