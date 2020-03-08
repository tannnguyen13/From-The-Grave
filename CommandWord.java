/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
     GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), ENGAGE("engage"), SNEAK("sneak"), GRAB("grab"), USE("use"), LOOK("look"), INVENTORY("inventory"), EQUIP("equip"), ATTACK("attack"), DEFEND("defend"), MAGIC("magic"), SLOW("slow"), EVASION("evasion"), HEAL("heal");
    
    // The command string.
    private String commandString;
    
    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    /**
     * @return The command word as a string.
     */
    public String toString()
    {
        return commandString;
    }
}
