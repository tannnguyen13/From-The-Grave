import java.util.Random;
public class Combat {
    private Parser parser = new Parser();
    private Random rand = new Random();
    private Printer printer = new Printer();
    private Player p1;
    private Player p2;
    Combat(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    public void engage() {
        if(p2.name.equals("Guard")) {
            printer.print("You woke up the orc guard!");
        }
        else {
            printer.print("The angry minotaur looks menacingly at you!");
        }
        while(p1.checkAlive() && p2.checkAlive()) {
            p2.enemyStatus();
            p1.status();
            System.out.println("ATTACK \nDEFEND \nMAGIC ");
            Command command = parser.getCommand();
            processCommand(command);
        }
    }
        private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                printer.print("I don't know what you mean...");
                break;
            case ATTACK:
                attack();
                enemyatk();
                break;
            case DEFEND:
                defend();
                enemyatk();
                break;
            case MAGIC:
                magic();
                enemyatk();
                break;
        }
        return wantToQuit;
    }
    public void attack() {
        int hit = rand.nextInt(100)+1;
        if(hit >(100 - p1.getHr())) {
            int dmg = p1.getAtk() - p2.getDf();
            if(dmg < 0) {
                dmg = 0;
            }
            p2.setHp(dmg);
            if (p1.checkEq("sword")) {
            printer.print("SLAASH!");
            printer.print("You slash the " +p2.name + " for " +dmg+ " damage!");
            }
        
            else {
            printer.print("BAAAM!");
            printer.print("You punch the" + p2.name + " for" +dmg+ " damage!");
            }
        }
        else {
            printer.print("WHOOSH!");
            printer.print("You try to hit the " +p2.name + " but you missed!");
        }
        if(p2.getHp() < 0) {
            printer.print("You have defeated the " +p2.name);
        }
    }
    public void defend() {
        double df = p1.getDf() * 1.25;
        int newDf = (int)(df);
    }
    public void enemyatk() {
        if(p2.checkAlive()) {
            int hit2 = rand.nextInt(100)+1;
            if(hit2 > (100 - p2.getHr())) {
                int dmg2 = p2.getAtk() - p1.getDf();
                if(dmg2 < 0) {
                    dmg2 = 0;
                }
                p1.setHp(dmg2);
                printer.print("WHAAM");
                printer.print("The " +p2.name+ " hit you for " +dmg2+ " damage!");
            }
            else {
            if(p1.checkEq("shield")) {
                printer.print("TINK!");
                printer.print("The " +p2.name + " tries to hit you but you block it with your shield!");
            }
            else {
                printer.print("WHOOSH!");
                printer.print("The " +p2.name + " tries to hit you but misses.");
            }
            }
        }
    }
    public void magic() {
        if(p1.checkEq("necklace") && p1.checkEq("ring")) {
            boolean magic = false;
            printer.print("Cast what?");
            printer.print("SLOW\nEVASION\nHEAL");
            while(!magic) {
                Command command = parser.getCommand();
                magic = doMagic(command);
            }
        }
        else if(p1.checkEq("necklace")) {
            boolean magic = false;
            printer.print("Cast what?");
            printer.print("SLOW\nEVASION");
            while(!magic) {
                Command command = parser.getCommand();
                magic = doMagic(command);
            }
        }
        else if(p1.checkEq("ring")) {
            boolean magic = false;
            printer.print("Cast what?");
            printer.print("HEAL");
            while(!magic) {
                Command command = parser.getCommand();
                magic = doMagic(command);
            }
        }
        else {
            printer.print("You cannot use magic.");
        }
    }
    public boolean doMagic(Command command) {
        boolean quitMagic = true;
        CommandWord commandWord = command.getCommandWord();
        switch(commandWord) {
            case UNKNOWN:
                printer.print("I don't know what you mean...");
                break;
            case SLOW:
                p2.defense -= 5;
                printer.print("You have cast SLOW and have slowed the " +p2.name+ " down making him more susceptible to damage.");
                break;
            case EVASION:
                p2.hitrate -= 10;
                printer.print("You have cast EVASION and increased your agility which makes it harder for the " +p2.name+ " to hit you.");
                break;
            case HEAL:
                p1.hp += 20;
                if(p1.hp > p1.orighp) {
                    p1.hp = p1.orighp;
                }
                printer.print("You have cast HEAL on yourself. You gain 20 HP!");
                break;
        }
        return quitMagic;
    }
}