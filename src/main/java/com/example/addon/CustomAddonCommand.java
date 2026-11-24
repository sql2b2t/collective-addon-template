package com.example.addon;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.collective.client.commands.Command;
import me.collective.client.utils.chat.ChatUtils;
import net.minecraft.network.chat.Component;

// Команда чата. Корень: .addoncmd, подкоманда: .addoncmd say <текст>, алиас: .acmd
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
            reply("команда аддона выполнена, напиши .addoncmd say <текст>");
            return 1;
        });

        root.then(literal("say")
            .then(argument("text", StringArgumentType.greedyString()).executes(ctx -> {
                String text = StringArgumentType.getString(ctx, "text");
                reply("ты написал: " + text);
                return 1;
            })));
    }

    private void reply(String text) {
        ChatUtils.printChat(Component.literal(text), ChatUtils.createSignature(-1));
    }
}
