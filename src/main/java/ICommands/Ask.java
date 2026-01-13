package ICommands;

import Events.ICommand;
import ICommandsHelpers.ChatGPT;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Ask implements ICommand {
    private final String name = "ask";
    private final String description = "Ask MythicMate a question! Powered by ChatGPT 3.5, this feature is in beta.";


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.STRING, "question",
                "The question you would like to ask", true));
        return optionData;
    }

    @Override
    public Runnable execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        ChatGPT.askChat(event);
        return null;
    }
}
