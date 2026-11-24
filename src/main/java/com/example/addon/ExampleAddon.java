package com.example.addon;

import me.collective.addon.AddonInfo;
import me.collective.addon.CollectiveAddon;

// Имя, версия, автор и описание берутся из этой аннотации.
@AddonInfo(
    name = "Example Addon",
    version = "1.0.0",
    author = "YourName",
    description = "Пример аддона для Collective Client",
    website = "https://github.com/sql2b2t/collective-addon-template"
)
public class ExampleAddon extends CollectiveAddon {

    @Override
    public void onInitialize() {
        registerModule(new CustomAddonModule());
        registerCommand(new CustomAddonCommand());
        System.out.println("[ExampleAddon] загружен");
    }

    @Override
    public void onShutdown() {
        System.out.println("[ExampleAddon] выгружен");
        // снимает модули, команды и подписки, сделанные через registerModule/registerCommand/subscribeEvents
        super.onShutdown();
    }
}
