# Collective Client — шаблон аддона

collective client - клиент для копий 2b2t (Minecraft 1.21.11, Fabric).
Аддон - это обычный Fabric-мод с точкой входа `collective`: он добавляет
в клиент свои модули, команды и обработчики событий.

Загрузка двумя способами:
1. jar в папку `mods/` вместе с клиентом;
2. jar в папку `.minecraft/collective/addons/` и команда `.addon reload`
   в игре (без перезапуска).

## Структура

```text
collective-addon-template/
├── build.gradle                    сборка (loom, Mojang-маппинги)
├── settings.gradle
├── gradle.properties               здесь указан путь к JDK 25 для Gradle
├── libs/
│   └── collective-dev.jar          API клиента (dev-сборка)
├── src/main/java/com/example/addon/
│   ├── ExampleAddon.java           точка входа
│   ├── CustomAddonModule.java      пример модуля
│   └── CustomAddonCommand.java     пример команды
└── src/main/resources/
    └── fabric.mod.json             манифест, entrypoint "collective"
```

## Сборка

Нужен JDK 25 для запуска Gradle (требование fabric-loom 1.18),
компиляция идёт под Java 21. Путь к JDK 25 задается в `gradle.properties`
(строка `org.gradle.java.home`), поправь его под свою систему.

```bash
git clone https://github.com/sql2b2t/collective-addon-template.git my-collective-addon
cd my-collective-addon
./gradlew build
```

Готовый jar появится в `build/libs/collective-example-addon-1.0.0.jar`.

### Обновление API клиента

`libs/collective-dev.jar` - это dev-сборка клиента. После обновления клиента
пересобери его и скопируй свежий jar:

```bash
cd collective-client && ./gradlew build
cp build/devlibs/collective-*-dev.jar ../collective-addon-template/libs/collective-dev.jar
```

## Примеры кода

### Точка входа

```java
package com.example.addon;

import me.collective.addon.AddonInfo;
import me.collective.addon.CollectiveAddon;

@AddonInfo(
    name = "My Addon",
    version = "1.0.0",
    author = "YourName",
    description = "мой аддон",
    website = "https://github.com/YourName/my-addon"
)
public class MyAddon extends CollectiveAddon {

    @Override
    public void onInitialize() {
        registerModule(new CustomAddonModule());
        registerCommand(new CustomAddonCommand());
    }
}
```

### Модуль

Модули наследуют `me.collective.client.modules.Module`, события приходят,
пока модуль включен. В чат пишут через `ChatUtils.printChat`.

```java
public class CustomAddonModule extends Module {
    public final BooleanSetting chatNotify = add(new BooleanSetting("ChatNotify", true));
    public final NumberSetting<Double> tickInterval =
            add(new NumberSetting<>("Interval", 20.0, 1.0, 100.0));

    public CustomAddonModule() {
        super("AddonModule", "описание модуля", ModuleCategory.MISC);
    }

    @EventHandler
    private void onTick(MinecraftTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (chatNotify.getValue()) {
            ChatUtils.printChat(Component.literal("здоровье: " + mc.player.getHealth()),
                    ChatUtils.moduleSignature(this));
        }
    }
}
```

`NumberSetting` принимает имя, значение, минимум и максимум. Пятый параметр
конструктора - `Predicate` видимости, а не шаг.

### Команда

Команды наследуют `me.collective.client.commands.Command`, тело - в методе
`exec`. Алиасы задаются через `setAliases`.

```java
public class CustomAddonCommand extends Command {
    public CustomAddonCommand() {
        super("addoncmd");
        setAliases("acmd");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void exec(LiteralArgumentBuilder<?> builder) {
        LiteralArgumentBuilder<Object> root = (LiteralArgumentBuilder<Object>) builder;
        root.executes(ctx -> {
            ChatUtils.printChat(Component.literal("команда выполнена"),
                    ChatUtils.createSignature(-1));
            return 1;
        });
    }
}
```

### fabric.mod.json

Точка входа аддона - блок `entrypoints` с ключом `collective`:

```json
"entrypoints": {
  "collective": [ "com.example.addon.MyAddon" ]
}
```

## Команды в игре

| Команда | Что делает |
|---|---|
| `.addon list` | список загруженных аддонов |
| `.addon load <jar>` | загрузить аддон из папки addons |
| `.addon unload <имя>` | выгрузить аддон |
| `.addon info <имя>` | подробности об аддоне |
| `.addon sources` | открыть папку исходников аддонов |
| `.addon build <имя>` | собрать исходник из collective/addons-src |
| `.addon reload` | перезагрузить все аддоны |
| `.addon folder` | открыть папку аддонов |

## Лицензия

MIT.
