package Events;

import ICommandsHelpers.MultiThreader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandManager extends ListenerAdapter {
    private final List<ICommand> commands = new ArrayList<>();
    private static ExecutorService pool;

    public CommandManager() {
        super();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        //For every guild bot is in, add commands from bot to the guild
        for(Guild guild : event.getJDA().getGuilds()){
            for(ICommand command : commands){
                guild.upsertCommand(command.getName(), command.getDescription())
                        .addOptions(command.getOptions()).queue();
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for(ICommand command : commands){
            if(command.getName().equalsIgnoreCase(event.getName())){
                MultiThreader thread = new MultiThreader(command, event);
                thread.start();
            }
        }
    }




    public void add(ICommand command){
        commands.add(command);
    }
}
