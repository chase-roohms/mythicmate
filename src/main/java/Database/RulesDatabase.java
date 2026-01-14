package Database;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;

public class RulesDatabase {
    /*Cover Rules                                                                               */
    /*==========================================================================================*/
    public final static String HALF_COVER ="""
            A target has half cover if an obstacle blocks at least half of its body. The obstacle might be a low wall, a large piece of furniture, a narrow tree trunk, or a creature, whether that creature is an enemy or a friend.
            - +2 bonus to AC
            - +2 bonus to Dexterity saving throws
            """;

    public final static String THREE_QUARTERS_COVER ="""
            A target has three-quarters cover if about three-quarters of it is covered by an obstacle. The obstacle might be a portcullis, an arrow slit, or a thick tree trunk.
            - +5 bonus to AC
            - +5 bonus to Dexterity saving throws
            """;

    public final static String FULL_COVER ="""
            A target has total cover if it is completely concealed by an obstacle.
            - Can't be targeted directly by an attack or a spell
            - Some spells can reach such a target by including it in an area of effect
            """;

    /*Condition Rules                                                                           */
    /*==========================================================================================*/
    public final static String BLINDED ="""
            - A blinded creature can't see and automatically fails any ability check that requires sight.
            - Attack rolls against the creature have advantage, and the creature's attack rolls have disadvantage.
            """;

    public final static String  CHARMED ="""
            - A charmed creature can't attack the charmer or target the charmer with harmful abilities or magical effects.
            - The charmer has advantage on any ability check to interact socially with the creature.
            """;

    public final static String  DEAFENED ="""
            - A deafened creature can't hear and automatically fails any ability check that requires hearing.
            """;

    public final static String  FRIGHTENED ="""
            - A frightened creature has disadvantage on ability checks and attack rolls while the source of its fear is within line of sight.
            - The creature can't willingly move closer to the source of its fear.
            """;

    public final static String  GRAPPLED ="""
            - A grappled creature's speed becomes 0, and it can't benefit from any bonus to its speed.
            - The condition ends if the grappler is incapacitated.
            - The condition also ends if an effect removes the grappled creature from the reach of the grappler or grappling effect, such as when a creature is hurled away by the thunderwave spell.
            """;

    public final static String  INCAPACITATED ="""
            - An incapacitated creature can't take actions or reactions.
            """;

    public final static String  INVISIBLE ="""
            - An invisible creature is impossible to see without the aid of magic or a special sense. For the purpose of hiding, the creature is heavily obscured. The creature's location can be detected by any noise it makes or any tracks it leaves.
            - Attack rolls against the creature have disadvantage, and the creature's attack rolls have advantage.
            """;

    public final static String  PARALYZED ="""
            - A paralyzed creature is incapacitated and can't move or speak.
            - The creature automatically fails Strength and Dexterity saving throws.
            - Attack rolls against the creature have advantage.
            - Any attack that hits the creature is a critical hit if the attacker is within 5 feet of the creature.
            """;

    public final static String  PETRIFIED ="""
            - A petrified creature is transformed, along with any nonmagical object it is wearing or carrying, into a solid inanimate substance (usually stone). Its weight increases by a factor of ten, and it ceases aging.
            - The creature is incapacitated, can't move or speak, and is unaware of its surroundings.
            - Attack rolls against the creature have advantage.
            - The creature automatically fails Strength and Dexterity saving throws.
            - The creature has Resistant to all damage.
            - The creature is immune to poison and disease, although a poison or disease already in its system is suspended, not neutralized.
            """;

    public final static String  POISONED ="""
            - A poisoned creature has disadvantage on attack rolls and ability checks.
            """;

    public final static String  PRONE ="""
            - A prone creature's only movement option is to crawl, unless it stands up and thereby ends the condition.
            - The creature has disadvantage on attack rolls.
            - An attack roll against the creature has advantage if the attacker is within 5 feet of the creature. Otherwise, the attack roll has disadvantage.
            """;

    public final static String  RESTRAINED ="""
            - A restrained creature's speed becomes 0, and it can't benefit from any bonus to its speed.
            - Attack rolls against the creature have advantage, and the creature's attack rolls have disadvantage.
            - The creature has disadvantage on Dexterity saving throws.
            """;

    public final static String  STUNNED ="""
            - A stunned creature is incapacitated (see the condition), can't move, and can speak only falteringly.
            - The creature automatically fails Strength and Dexterity saving throws.
            - Attack rolls against the creature have advantage.
            """;

    public final static String  UNCONSCIOUS ="""
            - An unconscious creature is incapacitated (see the condition), can't move or speak, and is unaware of its surroundings
            - The creature drops whatever it's holding and falls prone.
            - The creature automatically fails Strength and Dexterity saving throws.
            - Attack rolls against the creature have advantage.
            - Any attack that hits the creature is a critical hit if the attacker is within 5 feet of the creature.
            """;

    public final static String  EXHAUSTION ="""
            - **1:** Disadvantage on ability checks
            - **2:** Speed halved
            - **3:** Disadvantage on attack rolls and saving throws
            - **4:** Hit point maximum halved
            - **5:** Speed reduced to 0
            - **6:** Death
            """;



    private final static String FORCE = """
                A raw form of magical energy that hits like a brick wall.
                - Immune: 1
                - Resistant: 0
                - Vulnerable: 0
                """;

    private final static String RADIANT = """
                A divine smiting from the highest heavens.
                - Immune: 2
                - Resistant: 6
                - Vulnerable: 2
                """;

    private final static String THUNDER = """
                Percussive noise that is loud enough to cause damage.
                - Immune: 4
                - Resistant: 25
                - Vulnerable: 2
                """;

    private final static String PSYCHIC = """
                Telepathic abilities inflicting massive damage to the mind.
                - Immune: 22
                - Resistant: 4
                - Vulnerable: 0
                """;

    private final static String ACID = """
                Intense burning and stinging that can corrode if strong enough.
                - Immune: 26
                - Resistant: 32
                - Vulnerable: 0
                """;

    private final static String NECROTIC = """
                The destruction of 'life force'. Death, decay, rotting, and corruption.
                - Immune: 27
                - Resistant: 30
                - Vulnerable: 0
                """;

    private final static String LIGHTNING = """
                Lightning damage is caused by a high voltage of electricity.
                - Immune: 26
                - Resistant: 87
                - Vulnerable: 0
                """;

    private final static String COLD = """
                Frigid pain leading to potential numbness caused by temperatures approaching zero Kelvin.
                - Immune: 30
                - Resistant: 115
                - Vulnerable: 4
                """;

    private final static String FIRE = """
                Fire is hot, and can burn your skin if it comes in contact.
                - Immune: 68
                - Resistant: 87
                - Vulnerable: 13
                """;

    private final static String  POISON= """
                A toxic substance that is “ingested” and causes harm to the body.
                - Immune: 207
                - Resistant: 16
                - Vulnerable: 0
                """;

    private final static String  NON_MAGIC_MELEE= """
                Physical harm caused by a non magical weapon.
                - Immune: 46
                - Resistant: 189
                - Vulnerable: 5
                """;

    private final static String  MAGIC_MELEE= """
                Physical harm caused by a magical weapon.
                - Immune: 0
                - Resistant: 0
                - Vulnerable: 0
                """;

    //Format and return embed with damage info
    @NotNull
    public static EmbedBuilder getDamageEmbed(){
        EmbedBuilder damageEmbed = new EmbedBuilder();
        damageEmbed.setTitle("DND 5e Damage Types");

        damageEmbed.addField("**Magic Weapons**", MAGIC_MELEE, true);
        damageEmbed.addField("**Force**", FORCE, true);
        damageEmbed.addField("**Radiant**", RADIANT, true);
        damageEmbed.addField("**Thunder**", THUNDER, true);
        damageEmbed.addField("**Psychic**", PSYCHIC, true);
        damageEmbed.addField("**Acid**", ACID, true);
        damageEmbed.addField("**Necrotic**", NECROTIC, true);
        damageEmbed.addField("**Lightning**", LIGHTNING, true);
        damageEmbed.addField("**Cold**", COLD, true);
        damageEmbed.addField("**Fire**", FIRE, true);
        damageEmbed.addField("**Poison**", POISON, true);
        damageEmbed.addField("**Non-Magic Melee**", NON_MAGIC_MELEE, true);
        damageEmbed.setFooter("Stats come from the following books: XGE, VGM, TCE, PHB, MTF, MM and DMG",
                "https://i.imgur.com/dZvCR8L.png");
        return damageEmbed;
    }

    //Format and return embed with cover rules
    @NotNull
    public static EmbedBuilder getCoverEmbed(){
        EmbedBuilder coverEmbed = new EmbedBuilder();
        coverEmbed.setTitle("DND 5e Cover Rules");
        coverEmbed.setDescription("Walls, trees, creatures, and other obstacles can provide cover during combat, making a target more difficult to harm. A target can benefit from cover only when an attack or other effect originates on the opposite side of the cover.");

        coverEmbed.addField("**Half Cover**", HALF_COVER, false);
        coverEmbed.addField("**Three Quarters Cover**", THREE_QUARTERS_COVER, false);
        coverEmbed.addField("**Full Cover**", FULL_COVER, false);
        coverEmbed.setFooter("Info comes from the Basic Rules",
                "https://i.imgur.com/dZvCR8L.png");
        return coverEmbed;
    }

    //Format and return embed with conditions info
    @NotNull
    public static EmbedBuilder getConditionEmbed(){
        EmbedBuilder conditionEmbed = new EmbedBuilder();
        conditionEmbed.setTitle("DND 5e Conditions");
        conditionEmbed.addField("**Blinded**", BLINDED, false);
        conditionEmbed.addField("**Charmed**", CHARMED, false);
        conditionEmbed.addField("**Deafened**", DEAFENED, false);
        conditionEmbed.addField("**Frightened**", FRIGHTENED, false);
        conditionEmbed.addField("**Grappled**", GRAPPLED, false);
        conditionEmbed.addField("**Incapacitated**", INCAPACITATED, false);
        conditionEmbed.addField("**Invisible**", INVISIBLE, false);
        conditionEmbed.addField("**Paralyzed**", PARALYZED, false);
        conditionEmbed.addField("**Petrified**", PETRIFIED, false);
        conditionEmbed.addField("**Poisoned**", POISONED, false);
        conditionEmbed.addField("**Prone**", PRONE, false);
        conditionEmbed.addField("**Restrained**", RESTRAINED, false);
        conditionEmbed.addField("**Stunned**", STUNNED, false);
        conditionEmbed.addField("**Unconscious**", UNCONSCIOUS, false);
        conditionEmbed.addField("**Exhausted**", EXHAUSTION, false);
        conditionEmbed.setFooter("Info comes from the Basic Rules",
                "https://i.imgur.com/dZvCR8L.png");

        return conditionEmbed;
    }

}
