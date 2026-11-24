package com.example.addon;

import me.collective.client.events.EventHandler;
import me.collective.client.events.MinecraftTickEvent;
import me.collective.client.modules.Module;
import me.collective.client.modules.ModuleCategory;
import me.collective.client.settings.BooleanSetting;
import me.collective.client.settings.NumberSetting;
import me.collective.client.utils.chat.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

// Модуль появляется в ClickGUI. События приходят, пока модуль включен.
public class CustomAddonModule extends Module {

    // настройки видны в ClickGUI автоматически
    public final BooleanSetting chatNotify = add(new BooleanSetting("ChatNotify", true));
    // пятый параметр NumberSetting — Predicate видимости, а не шаг;
    // без него достаточно четырех аргументов: имя, значение, минимум, максимум
    public final NumberSetting<Double> tickInterval = add(new NumberSetting<>("Interval", 20.0, 1.0, 100.0));

    private int timer = 0;

    public CustomAddonModule() {
        super("AddonModule", "Пример модуля из аддона", ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        timer = 0;
        message("AddonModule включен");
    }

    @Override
    public void onDisable() {
        message("AddonModule выключен");
    }

    @EventHandler
    private void onTick(MinecraftTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        timer++;
        if (timer >= tickInterval.getValue()) {
            timer = 0;
            if (chatNotify.getValue()) {
                message("здоровье: " + mc.player.getHealth());
            }
        }
    }

    private void message(String text) {
        ChatUtils.printChat(Component.literal(text), ChatUtils.moduleSignature(this));
    }
}
