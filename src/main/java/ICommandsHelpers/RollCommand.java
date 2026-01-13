package ICommandsHelpers;

import Functions.Dice;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RollCommand {
    /*Fields                                                                                    */
    /*==========================================================================================*/
    public enum RollType {REG, ADV, DISADV, GWF, EMPOWER}

    /*Function(s))                                                                              */
    /*==========================================================================================*/
    public static void roll(SlashCommandInteractionEvent event, RollType rollType) {

        int empowerParam = 0;

        ArrayList<String> dice = new ArrayList<>(Arrays.asList(Objects.requireNonNull(
                event.getOption("dice")).getAsString().split("\\+")));
        if (rollType == RollType.EMPOWER) {
            empowerParam = Objects.requireNonNull(event.getOption("this-or-lower")).getAsInt();
        }


        int constants = 0;                                  //Track constants
        int sum = 0;                                        //Track rolling sum
        int rerolls = 0;                                    //Track rerolls (for gwf and empower)
        double average = 0;                                 //Track average
        int totalNumDie = 0;                                //Track number of dice rolled
        StringBuilder diceRolled = new StringBuilder();     //To output each dice's roll

        while (!dice.isEmpty()) {                          //While there is input to process
            dice.set(0, dice.get(0).replace(" ", ""));     //Remove all whitespace
            if (dice.get(0).matches("\\d*")) {                         //Current item is a number
                constants += Integer.parseInt(dice.get(0));                   //Add it to the contants
                average += constants;                                         //Add it to the average
            } else if (dice.get(0).matches("\\d*d\\d*")                //##d## e.g. 1d6, 5d8, etc
                    || dice.get(0).matches("d\\d*")) {                 //d##   e.g. d6, d8, etc
                int numDie; //To loop rolls
                int d = dice.get(0).indexOf('d');              //Save index of character 'd'
                if (dice.get(0).matches("d\\d*")) {      //d## numDice is 1
                    numDie = 1;
                } else {
                    numDie = Integer.parseInt(dice.get(0).substring(0, d));
                }  //##d## numDice is before d
                int faces = Integer.parseInt(dice.get(0).substring(d + 1));    //faces is after d
                average += numDie*(((double) faces /2) + 0.5);
                totalNumDie += numDie;

                //Format string with the dice being roled in bold letters
                diceRolled.append("**").append(numDie).append("d").append(faces).append("**").append(": (");


                Dice die = new Dice(faces);             //Dice object with number of faces = faces
                for (int i = 0; i < numDie; i++) {      //Loop numDie times
                    int roll = 0;                       //To store roll value

                    if (rollType == RollType.REG) {    //Regular roll
                        roll = die.roll();              //Roll dice
                        diceRolled.append("**").append(roll).append("**");        //Add it to the dice rolled string
                    } else if (rollType == RollType.ADV) {             //Roll with advantage
                        int roll1 = die.roll();                         //Roll twice
                        int roll2 = die.roll();
                        int badRoll = Math.min(roll1, roll2);           //Bad roll is lower
                        roll = Math.max(roll1, roll2);                  //Save higher roll

                        //Add both to string, crossing out lower roll
                        diceRolled.append("(*").append(badRoll).append("* -> **").append(roll).append("**)");
                    } else if (rollType == RollType.DISADV) {          //Roll with disadvantage
                        int roll1 = die.roll();                         //Roll twice
                        int roll2 = die.roll();
                        int goodRoll = Math.max(roll1, roll2);          //Good roll is higher
                        roll = Math.min(roll1, roll2);                  //Save lower roll

                        //Add both to string, crossing out higher roll
                        diceRolled.append("(*").append(goodRoll).append("* -> **").append(roll).append("**)");
                    } else if (rollType == RollType.GWF) {     //Roll great weapon fighting
                        int roll1 = die.roll();                 //Roll dice
                        if (roll1 <= 2) {                       //If the roll is less than or equal to 2
                            roll = die.roll();                  //Reroll, saving new roll

                            //Add both to string, crossing out rerolled roll
                            diceRolled.append("(*").append(roll1).append("* -> **").append(roll).append("**)");
                            rerolls++;                      //Add a reroll
                        } else {                            //Roll was higher than 2
                            roll = roll1;                   //Save the roll
                            diceRolled.append("**").append(roll).append("**");        //Add to string
                        }

                    } else if (rollType == RollType.EMPOWER) {  //Roll and empower
                        int roll1 = die.roll();                  //Roll dice
                        if (roll1 <= empowerParam) {             //If the roll is less than or equal to users num
                            roll = die.roll();                   //Reroll, saving new roll

                            //Add both to string, crossing out rerolled roll
                            diceRolled.append("(*").append(roll1).append("* -> **").append(roll).append("**)");
                            rerolls++;                      //Add a reroll
                        } else {                            //Roll was higher than users num
                            roll = roll1;                   //Save the roll
                            diceRolled.append("**").append(roll).append("**");        //Add to string
                        }
                    }
                    sum += roll;                    //Add roll to the total

                    if (i < numDie - 1) {           //If not the last roll in loop
                        diceRolled.append(", ");    //Add a comma and space to the string
                    }
                }
                diceRolled.append(") + ");          //Loop is done, close portion of string
            } else {                                //Not a recognized dice formate
                diceRolled.append("**Unknown** + ");
            }
            dice.remove(0);                  //Done with that index of input
        }
        sum += constants;                           //Add constants to sum
        if (constants == 0) {                       //If no constants
            diceRolled.delete(diceRolled.lastIndexOf("+") - 1, diceRolled.length() - 1); //Remove last "+"
        } else {                                    //If constants
            diceRolled.append("**").append(constants).append("**");                          //Add to string
        }

        EmbedBuilder toEmbed = diceResponse(diceRolled, sum, (int)average, rerolls, empowerParam, rollType);
        event.replyEmbeds(toEmbed.build()).queue();
    }




    /*Output Embed                                                                              */
    /*==========================================================================================*/
    private static EmbedBuilder diceResponse(StringBuilder diceRolled, int sum, int average,
                                       int rerolls, int empowerParam, RollType rollType){

        EmbedBuilder diceEmbed = new EmbedBuilder();
        diceEmbed.setDescription(diceRolled);
        diceEmbed.addField("**Total**", "➥" + sum, true);
        diceEmbed.addField("**Expected**", "➥" + average, true);
        diceEmbed.setColor(new Color(205, 204, 198));
        diceEmbed.setThumbnail("https://i.imgur.com/AaMCQiQ.png");

        String rollTitle = "";
        if (rollType.equals(RollType.REG)) {
            rollTitle +="Rolling Your Dice";

        } else if(rollType.equals(RollType.ADV)){
            rollTitle += "Rolling with Advantage";
        } else if(rollType.equals(RollType.DISADV)){
            rollTitle += "Rolling with Disdvantage";
        }

        else if (rollType.equals(RollType.GWF) || rollType.equals(RollType.EMPOWER)) {
            if (rollType.equals(RollType.GWF)) {
                rollTitle += "Great Weapon Fighting - Rerolling 2 or Lower";
            } else {
                rollTitle += "Rolling and Empowering - Rerolling " + empowerParam + " or Lower";
            }
            diceEmbed.addField("**Rerolls**", "➥" + rerolls, true);
        }

        diceEmbed.setAuthor(rollTitle, "https://chaseroohms.github.io/", "https://i.imgur.com/CnYaMkM.png");
        //Return output message
        return diceEmbed;
    }
}

