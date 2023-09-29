package com.bgsoftware.superiorskyblock.commands.admin;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.commands.arguments.CommandArgument;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.commands.InternalIslandsCommand;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArguments;
import com.bgsoftware.superiorskyblock.commands.arguments.CommandArgumentsBuilder;
import com.bgsoftware.superiorskyblock.commands.arguments.types.IntArgumentType;
import com.bgsoftware.superiorskyblock.commands.arguments.types.MultipleIslandsArgumentType;
import com.bgsoftware.superiorskyblock.commands.arguments.types.StringArgumentType;
import com.bgsoftware.superiorskyblock.commands.context.IslandsCommandContext;
import com.bgsoftware.superiorskyblock.core.formatting.Formatters;
import com.bgsoftware.superiorskyblock.core.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CmdAdminTitleAll implements InternalIslandsCommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("titleall");
    }

    @Override
    public String getPermission() {
        return "superior.admin.titleall";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return Message.COMMAND_DESCRIPTION_ADMIN_TITLE_ALL.getMessage(locale);
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return new CommandArgumentsBuilder()
                .add(CommandArgument.required("islands", MultipleIslandsArgumentType.INCLUDE_PLAYERS, Message.COMMAND_ARGUMENT_PLAYER_NAME, Message.COMMAND_ARGUMENT_ISLAND_NAME, Message.COMMAND_ARGUMENT_ALL_ISLANDS))
                .add(CommandArgument.required("fade-in", IntArgumentType.INTERVAL, Message.COMMAND_ARGUMENT_TITLE_FADE_IN))
                .add(CommandArgument.required("duration", IntArgumentType.INTERVAL, Message.COMMAND_ARGUMENT_TITLE_DURATION))
                .add(CommandArgument.required("fade-out", IntArgumentType.INTERVAL, Message.COMMAND_ARGUMENT_TITLE_FADE_OUT))
                .add(CommandArgument.required("message", StringArgumentType.MULTIPLE_COLORIZE, "-title"))
                .add(CommandArgument.required("unused", StringArgumentType.MULTIPLE_COLORIZE, "-subtitle"))
                .build();
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, IslandsCommandContext context) {
        CommandSender dispatcher = context.getDispatcher();

        int fadeIn = context.getRequiredArgument("fade-in", Integer.class);
        int duration = context.getRequiredArgument("duration", Integer.class);
        int fadeOut = context.getRequiredArgument("fade-out", Integer.class);

        String message = context.getRequiredArgument("message", String.class);
        Map<String, String> parsedArguments = CommandArguments.parseArguments(message.split(" "));

        String title = parsedArguments.get("title");
        String subtitle = parsedArguments.get("subtitle");

        if (title == null && subtitle == null) {
            Message.INVALID_TITLE.send(dispatcher);
            return;
        }

        String formattedTitle = title == null ? null : Formatters.COLOR_FORMATTER.format(title);
        String formattedSubtitle = subtitle == null ? null : Formatters.COLOR_FORMATTER.format(subtitle);

        List<Island> islands = context.getIslands();
        islands.forEach(island -> island.sendTitle(formattedTitle, formattedSubtitle, fadeIn, duration, fadeOut));

        SuperiorPlayer targetPlayer = context.getTargetPlayer();

        if (targetPlayer == null)
            Message.GLOBAL_TITLE_SENT_NAME.send(dispatcher, islands.size() == 1 ? islands.get(0).getName() : "all");
        else
            Message.GLOBAL_TITLE_SENT.send(dispatcher, targetPlayer.getName());
    }

}
