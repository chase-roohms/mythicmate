package Functions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Scanner;

public class SpellEmbedSender {
    public static EmbedBuilder getSpellEmbed (Scanner fileDetails){
        EmbedBuilder spellEmbed = new EmbedBuilder();
        String description = "";
        boolean needTitle = true;
        boolean needHeader = true;
        boolean needSpellType = true;
        int fields = 0;


        //Pull line by line while the source is not empty
        while(fileDetails.hasNextLine()) {
            String newLine = fileDetails.nextLine();
            if (!newLine.isEmpty()) {
                if (needTitle) {//First line is title
                    spellEmbed.setTitle(newLine.replace("# ", ""));
                    needTitle = false;
                } else if (needHeader) {//Next line is header (author)
                    spellEmbed.setAuthor(newLine, "http://dnd5e.wikidot.com/spells", "https://i.imgur.com/dZvCR8L.png");
                    needHeader = false;
                } else if(needSpellType){//Next line is type of spell
                    description = description.concat(newLine + "\n" + "\n");
                    needSpellType = false;
                } else if (fields < 4) {//Next four lines are spell fields
                    String fieldName = newLine.substring(0, newLine.indexOf(":**") + 3).replace(":", "");
                    newLine = newLine.substring(newLine.indexOf(":**") + 3);
                    spellEmbed.addField(fieldName, newLine, true);
                    fields++;
                    if (fields == 2 || fields == 4) {
                        spellEmbed.addBlankField(true);
                    }
                } else if (newLine.startsWith("***Spell Lists.***")) {//Spell list is footer
                    spellEmbed.setFooter(newLine.replace("***", ""));
                } else {//Rest is body
                    description = description.concat(newLine + "\n" + "\n");
                }
            }
        }
        spellEmbed.setDescription(description);
        spellEmbed.setColor(new Color(205, 204, 198));
        return spellEmbed;
    }
}