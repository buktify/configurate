# Configurate

Данная библиотека позволяет легко создавать и управлять конфигурациями Spigot плагина с помощью аннотаций.

Чтобы иметь возможность использовать эту библиотеку, нужно просто подключить её как implementation.

```groovy
repositories {
    maven {
        name = 'buktify-repo'
        url = 'https://repo.crazylegend.space/public'
    }
}

dependencies {
    implementation 'org.buktify:configurate:1.2.4'
}
```

### Создание класса конфигурации.

```java

@Getter
@Configuration(
        fileName = "test-config.yml",
        filePath = "%plugin_root%/someDirectory/%filename%"
)
@NoArgsConstructor
@SuppressWarnings("FieldMayBeFinal")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyConfiguration {

    @Comment({"Some comment", "Some other comment"})
    @Variable("some.string")
    String someString = "hello";

}
```

`@Configuration` маркирует класс как конфигурацию, `fileName` указывает на имя файла, а `filePath` на путь к нему,
относительно корневой директории.

В классе конфигурации обязательно должен присутствовать конструктор без аргументов.

`@Variable` помечает поле, как необходимое к обработке и привязывает его к переменной в `.yml` файле.

`@Comment` добавляет комментарии при первичном создании файла конфигурации.
Поддерживается с 1.19

Каждое поле, помеченое аннотацией `@Variable` должно иметь значение по умолчанию.

### Сервис-обработчик конфигураций.

```java
ConfigurationService configurationService=new ConfigurationService(plugin.getDataFolder())
        .registerConfigurations(MyConfiguration.class)
        .apply();
```

При инициализации задайте корневую папку, а так-же сами классы, которые хотите обработать.
Метод `apply()` начинает обработку всех зарегистрированных классов.

Вам не нужно добавлять `.yml` файлы в ресурсы плагина, они будут созданы исходя из того, как вы их сконфигурируете.

### Получение объектов конфигураций

Для получения объекта конфигурации нужно обратиться к пулу конфигураций.

```java
MyConfiguration configuration=configurationService.getConfigurationPool().getConfiguration(MyConfiguration.class);
```

### Сериализация кастомных классов

Основной функционал библиотеки строится на сериализаторах, в ней уже есть готовый набор реализаций,
который покроет базовые потребности.

Допустим у вас есть какой-то класс `MyClass`, для которого нужен свой сериализатор.

```java

@RequiredArgsConstructor
@Getter
public class MyClass {

    private final String someProperty;
}
```

Для реализации сериализатора, нужно создать класс, с именем `MyClassSerializer`, который будет реализовывать интерфейс
`Serializer<T>`, в дженерике указать тип, который нужно сериализировать.

К примеру

```java
public class MyClassSerializer implements Serializer<MyClass> {

    @Override

    public @NotNull MyClass deserialize(@NotNull String path, @NotNull FileConfiguration configuration) {
        String someProperty = configuration.getSting(path + ".property");
        return new MyClass(someProperty);
    }

    @Override
    public void serialize(@NotNull MyClass myClass, @NotNull String path, @NotNull FileConfiguration configuration) {
        configuration.set(path + ".property", myClass.getSomeProperty());
    }
}
```

Далее, при инициализации `ConfigurationService` просто регистрируете сериализатор.

```java
new ConfigurationService(plugin.getDataFolder())
        .registerConfigurations(MyConfiguration.class)
        .registerSerializers(MyClassSerializer.class)
        .apply();
```



