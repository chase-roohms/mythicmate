package ICommands.Roll;

import ICommandsHelpers.RollCommand;
import Events.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class RollGWF implements ICommand {
    private final String name = "rollgwf";
    private final String description = "Roll some dice, reroll 1's and 2's!";


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
        optionData.add(new OptionData(OptionType.STRING, "dice",
                "The dice you want to roll", true));
        return optionData;
    }

    @Override
    public Runnable execute(SlashCommandInteractionEvent event) {
        RollCommand.roll(event, RollCommand.RollType.GWF);
        return null;
    }
}
