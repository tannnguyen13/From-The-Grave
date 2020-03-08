import java.util.Random;
import java.util.ArrayList;
import java.util.Set;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Player p1;
    private Player guard;
    private Player minotaur;
    private Random rand;
    private Item key;
    private Item cellKey;
    private Item sword;
    private Item shield;
    private Item food;
    private Item rope;
    private Item necklace;
    private Item ring;
    private Printer printer;
    
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        guard = new Player("Guard", 150, 37, 5, 50, 75);
        minotaur = new Player("Minotaur", 250, 48, 10, 50, 80);
        rand = new Random();
        key = new Item(0, 0);
        cellKey = new Item(0, 0);
        sword = new Item(15, 0);
        shield = new Item(0, 15);
        food = new Item(0, 0);
        rope = new Item(0, 0);
        necklace = new Item(0, 0);
        ring = new Item(0, 0);
        printer = new Printer();
        createRooms();
    }
    
    public static void main(String [] args) {
        Game game = new Game();
        game.play();
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room tombs, dungeon, guardroom, armory, storageroom, monsterroom, exit;
     
        
        // create the rooms;
        tombs = new Room("are in a fairly large room full of tombs. The one you woke up in seems to be vacant now that you have stepped out of it. There is a glowing ring inside the tomb. There is a door to the north.");
        dungeon = new Room("are in a room full of cells. In these said cells, people are chained up and look hopeless. They call out to you to free them. There is a door to the north");
        guardroom = new Room("are in a room with doors to the north, the east, the south, and the west. Guarding this room is an asleep orc that has several keys dangling from his belt.");
        armory = new Room("are in a room filled to the brim with weapons, armor, and war-ready equipment.");
        storageroom = new Room("are in a room filled with sacks of supplies. You assume that at least some of these sacks are filled with food. You see some rope.");
        monsterroom = new Room("are in a room with a huge minotaur with a fierce axe. He looks particularly peeved and is ready to swing his axe. Past him seems to be the exit.");
        exit = new Room("are now in the exit of this dungeon. You do not know what is going on and who you are but you have made it out and ready to set out on your own adventure.");
        
        guardroom.setNPC("guard", guard);
        // initialise room exits        
        tombs.setExit("north", dungeon);
        tombs.setItems("ring", ring);
        
        dungeon.setExit("north", guardroom);
        dungeon.setExit("south", tombs);
        dungeon.setPrisoners();
        
        guardroom.setExit("north", monsterroom);
        guardroom.setExit("west", armory);
        guardroom.setExit("east", storageroom);
        guardroom.setExit("south", dungeon);
        
        storageroom.setExit("west", guardroom);
        storageroom.setItems("food", food);
        storageroom.setItems("rope", rope);
        
        armory.setExit("east", guardroom);
        armory.setItems("sword", sword);
        armory.setItems("shield", shield);
        
        
        monsterroom.setExit("north", exit);
        monsterroom.setExit("south", guardroom);
        monsterroom.setLock();
        monsterroom.setNPC("minotaur", minotaur);
        
        currentRoom = tombs;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        p1 = new Player("Hero", 100, 25, 20, 100, 50);
        p1.startStamina();
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (!finished && alive()) {
            Command command = parser.getCommand();
            if(processCommand(command) || currentRoom.checkExits()) {
            finished = true;
           }
        }
        if(p1.getHp() < 1) {
            printer.print("You have fallen in battle.");
        }
        printer.print("Thank you for playing.  Good bye.");
    }
    
    private boolean alive() {
        while(p1.getHp() > 0) {
        return true;
       }
       return false;
    }

    /**
     * printer.print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        printer.print("Welcome!");
        printer.print("Type '" + CommandWord.HELP + "' if you need help.");
        printer.printLong(" ");
        printer.printLong(" ");
        printer.printLong("...");
        printer.printLong(" ");
        printer.printLong(" ");
        printer.printLong("...");
        printer.printLong(" ");
        printer.printLong(" ");
        printer.print("You wake up laying down in a dark place.");
        printer.printLong(" ");
        printer.printLong(" ");
        printer.printLong("...");
        printer.printLong(" ");
        printer.printLong(" ");
        printer.print("After you realize you're conscious, you feel around. You seem to be confined in a rectangular shape. You push on the top of this 'box'.");
        printer.printLong(" ");
        printer.print("You are very weak but you manage to push the top off. You are greeted with a dull light. After you push the top off, you sit up.");
        printer.printLong(" ");
        printer.print("Sitting up, you realize that you are in a tomb. You don't know what to make of this.");
        printer.printLong(" ");
        printer.print("You are very hungry right now.");
        printer.printLong(" ");
        printer.printLong(" ");
        printer.print(currentRoom.getLongDescription());
        p1.status();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                printer.print("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case GRAB:
                grab(command);
                break;
                
            case ENGAGE:
                engage(command);
                break;
                
            case LOOK:
                printer.print(currentRoom.getLongDescription());
                break;
                
            case SNEAK:
                sneak(command);
                break;
                
            case USE:
                use(command);
                break;
            
            case INVENTORY:
                printer.print(p1.getItemString());
                break;
                
            case EQUIP:
                equip(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * printer.print out some help information.
     * Here we printer.print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        printer.print("You are lost. You are alone. You wander");
        printer.print("in this seemingly hopeless dungeon.");
        System.out.println();
        printer.print("Your command words are:");
        parser.showCommands();
    }
    
    public void engage(Command command) {
        if(!command.hasSecondWord()) {
            printer.print("Engage who?");
            return;
        }
        String name = command.getSecondWord();
        if(currentRoom.returnNPC(name)) {
            Combat rpg = new Combat(p1, currentRoom.getNPC(name));
            rpg.engage();
            if(name.equals("guard") && !currentRoom.getNPC(name).checkAlive()) {
                currentRoom.setItems("key", key);
                currentRoom.setItems("cellkey", cellKey);
                printer.print("The keys on his belt drop to the floor.");
                printer.print(currentRoom.getItems());
                currentRoom.changeDescription("are in a room with a room to the north, the east, and the west. The orc guarding this room has been defeated.");
            }
            else if(name.equals("minotaur") && !currentRoom.getNPC(name).checkAlive()) {
                printer.print("As the minotaur falls, you and the prisoners around feel a sense of relief. After a moment of realization, everyone");
                printer.print("starts to move towards the exit.");
            }
            currentRoom.removeNPC(name);
        }
        else {
            printer.print("You cannot engage that.");
        }
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise printer.print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            printer.print("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) {
            printer.print("You cannot go that way.");
        }
        else {
            if(!nextRoom.checkLock()) {
                if(currentRoom.emptyNPC()) {
                    currentRoom = nextRoom;
                    printer.print("You walk to the " + direction + " door.");
                    printer.printLong("");
                    if(!currentRoom.checkExits()) {
                        printer.print(currentRoom.getLongDescription());
                        p1.updateStatus();
                    }
                    else {
                        printer.print("You " + currentRoom.getShortDescription());
                    }
                }
                else if(currentRoom.returnNPC("guard")){
                    if(!currentRoom.getNPC("guard").checkTied()) {
                        printer.print("You cannot go that way. The guard might be alerted if you just walk there!");
                        return;
                    }
                    else {
                        currentRoom = nextRoom;
                        printer.print("You walk to the " + direction + " door.");
                        printer.printLong("");
                        if(!currentRoom.checkExits()) {
                            printer.print(currentRoom.getLongDescription());
                            p1.updateStatus();
                        }
                        else {
                            printer.print(currentRoom.getShortDescription());
                        }
                    }
                }
                else if(currentRoom.returnNPC("minotaur")) {
                    if(currentRoom.getNPC("minotaur").checkAlive()) {
                        printer.print("You cannot get past the big minotaur.");
                        return;
                    }
                    else {
                        currentRoom = nextRoom;
                        printer.print("You walk to the " + direction + " door.");
                        printer.printLong("");
                        if(!currentRoom.checkExits()) {
                            printer.print(currentRoom.getLongDescription());
                            p1.updateStatus();
                        }
                        else {
                            printer.print(currentRoom.getShortDescription());
                        }
                    }
                }
            }
            else {
                printer.print("That door is locked.");
            }
        }
        if(!currentRoom.checkExits())
        p1.status();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            printer.print("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    private void grab(Command command) {
        if(!command.hasSecondWord()) {
            printer.print("Grab what?");
            return;
        }
        String item = command.getSecondWord();
        if(currentRoom.getItems(item) != null) {
            p1.setItem(item, currentRoom.getItems(item));
            currentRoom.removeItem(item);
            printer.print("You have grabbed the " +item);
        }
        else {
            printer.print("That isn't in this room.");
            return;
        }
        printer.print(p1.getItemString());
    }
    
    private void sneak(Command command) {
        if(!command.hasSecondWord()) {
            printer.print("Sneak to where?");
            return;
        }
        else {
        
        String direction = command.getSecondWord();
        
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) {
            printer.print("You cannot sneak that way.");
            return;
        }
        else {
            if(!currentRoom.getExit(direction).checkLock()) {
                currentRoom = nextRoom;
                printer.print("You sneaked your way to the " + direction + " door.");
                printer.printLong("");
                printer.print(currentRoom.getLongDescription());
            }
           else {
                printer.print("You cannot sneak that way because that door is locked.");
                return;
            }
        }
        }
        p1.updateStatus();
        p1.status();
    }
    
    private void use(Command command) {
        if(!command.hasSecondWord()) {
            printer.print("Use what?");
            return;
        }
        String item = command.getSecondWord();
        if(p1.checkItem(item)) {
            if(item.equals("key")) {
                currentRoom.getExit("north").unlock();
                p1.removeItem("key");
                printer.print("You have unlocked the door to seemingly the exit to this dungeon.");
            }
            else if (item.equals("rope")) {
                if(currentRoom.returnNPC("guard")) {
                    currentRoom.getNPC("guard").changeTied();
                    p1.removeItem("rope");
                    currentRoom.setItems("key", key);
                    currentRoom.setItems("cellkey", cellKey);
                    printer.print("You have tied up the orc guard posted. You can easily grab the keys dangling from his belt now.");
                    printer.print(currentRoom.getItems());
                    currentRoom.changeDescription("are in a room with a room to the north, the east, and the west. Guarding this room is a tied up orc.");
                }
                else {
                    printer.print("Who are you trying to tie up?");
                }
            }
            else if (item.equals("food")) {
                p1.addStamina();
                p1.removeItem("food");
                printer.print("You have eaten the food from the storage room. You don't really know what it is. It tastes bland but you feel a new vigor in you.");
                p1.status();
            }
            else if (item.equals("cellkey")) {
                if(currentRoom.checkPrisoners()) {
                     currentRoom.freePrisoners();
                     minotaur.setHp(75);
                     p1.removeItem("cellkey");
                     printer.print("You have freed the prisoners. The prisoners all walk to the north door filled with new hope that they will get out of this decrepit place.");
                     printer.print("One of the prisoners comes back to thank you for your valiant efforts. He gives you a necklace and it gives you a faint sense of magic.");
                     p1.setItem("necklace", necklace);
                     currentRoom.changeDescription("are in a room full of cells. These cells are empty since you unlocked them. There is a door to the north");
                     currentRoom.getExit("north").changeDescription("are in a room with a room to the north, the east, the south, and the west. All of the prisoners you have freed are in this room waiting for you to unlock the door for them to exit.");
                     currentRoom.getExit("north").getExit("north").changeDescription("are in a room with a huge minotaur with a fierce axe. He looks particularly peeved and is ready to swing his axe. Past him seems to be the exit. Fortunately, you have the might of the mass of prisoners you have saved and they are helping you to fight the minotaur.");
                }
                else {
                    printer.print("You can't use that here.");
                }
            }
        }
        else {
            printer.print("You cannot use that item here.");
            return;
        }
    }
    
    private void equip(Command command) {
        if(!command.hasSecondWord()) {
            printer.print("Equip what?");
            return;
        }
        String item = command.getSecondWord();
        if(p1.checkItem(item)) {
            if(item.equals("sword")) {
                p1.equip(item);
                p1.addStrength(p1.getEq(item).getStrength());
                p1.addHR();
                p1.sword = true;
                printer.print("You have equipped the sword. You feel stronger with this in your hand!");
            }
            else if(item.equals("shield")) {
                p1.equip(item);
                p1.addDefense(p1.getEq(item).getDefense());
                p1.shield = true;
                printer.print("You have equpped the shield. You feel you can take a hit from anything!");
            }
            else if(item.equals("necklace")) {
                p1.equip(item);
                printer.print("You have equipped the necklacke. You feel a new sense of arcane power in you.");
            }
            else if(item.equals("ring")) {
                p1.equip(item);
                printer.print("You have put the ring on your finger. It feels familiar and you feel a bit of magic energy within you.");
            }
            else {
                printer.print("You cannot equip that.");
            }
        }
        else {
            printer.print("You cannot equip something that you don't have.");
            return;
        }
    }
}

