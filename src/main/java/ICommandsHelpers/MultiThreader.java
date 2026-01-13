package ICommandsHelpers;

import Events.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class MultiThreader extends Thread{
    private static ICommand command;
    private static SlashCommandInteractionEvent event;

    public MultiThreader(ICommand uCommand, SlashCommandInteractionEvent uEvent){
        command = uCommand;
        event = uEvent;
    }

    @Override
    public void run() {
        command.execute(event);
    }
}
