package Bot;

import Authenticate.Authenticate;
import Events.AutoCompleteManager;
import Events.CommandManager;
import ICommands.Ask;
import ICommands.CommandList;
import ICommands.Conditions;
import ICommands.Cover;
import ICommands.Damages;
import ICommands.Feat;
import ICommands.Lineage;
import ICommands.PlayerClass;
import ICommands.PlayerSubclass;
import ICommands.Roll.Roll;
import ICommands.Roll.RollAdv;
import ICommands.Roll.RollDisadv;
import ICommands.Roll.RollEmpower;
import ICommands.Roll.RollGWF;
import ICommands.Spell;
import ICommands.Update;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class DiscordBot {
    public static String ROOTDIR;

    /*Main                                                                                      */
    /*==========================================================================================*/
    public static void main(String[] args){
        // Set ROOTDIR - use environment variable if available, otherwise use current directory
        ROOTDIR = System.getenv("MYTHICMATE_ROOTDIR");
        if (ROOTDIR == null || ROOTDIR.isEmpty()) {
            ROOTDIR = System.getProperty("user.dir");
        }

        Authenticate auth = new Authenticate();                 //Authenticator Object
        JDA bot = JDABuilder.createDefault(auth.getToken())     //Build bot (token hidden for safety)
                .setActivity(Activity.customStatus("Dice Sage & Rules Lawyer"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)   //Give permission to view messages
                .build();

        //Create command manager and add all commands
        CommandManager commandManager = new CommandManager();
        commandManager.add(new Roll());
        commandManager.add(new RollAdv());
        commandManager.add(new RollDisadv());
        commandManager.add(new RollEmpower());
        commandManager.add(new RollGWF());
        commandManager.add(new CommandList());
        commandManager.add(new Spell());
        commandManager.add(new PlayerClass());
        commandManager.add(new PlayerSubclass());
        commandManager.add(new Feat());
        commandManager.add(new Lineage());
        commandManager.add(new Update());
        commandManager.add(new Cover());
        commandManager.add(new Conditions());
        commandManager.add(new Damages());
        commandManager.add(new Ask());

        //Create autocomplete manager
        AutoCompleteManager autoCompleteManager;
        try {
            //Try to open database lists and add them as autocomplete options
            autoCompleteManager = new AutoCompleteManager();
            bot.addEventListener(commandManager, autoCompleteManager);
        } catch (java.io.FileNotFoundException e) {
            //If it fails, still intialize bot without autocomplete
            System.out.println(e.getMessage());
            bot.addEventListener(commandManager);
        }
    }
}