package ICommands;

import ICommandsHelpers.QueryCommand;
import Events.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Spell implements ICommand {
    private final String name = "spell";
    private final String description = "Look up a spell by name.";



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
        optionData.add(new OptionData(OptionType.STRING, "lookup",
                "The spell you would like to lookup", true, true));
        return optionData;
    }

    @Override
    public Runnable execute(SlashCommandInteractionEvent event) {
        String lookup = Objects.requireNonNull(event.getOption("lookup")).getAsString();
        QueryCommand.query(event, lookup);
        return null;
    }
}
