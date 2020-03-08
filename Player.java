import java.util.Random;
import java.util.HashMap;
import java.util.Set;

public class Player
{
    HashMap <String, Item> inventory;
    HashMap <String, Item> equipped;
    String name;
    int hp;
    int orighp;
    int strength;
    int stamina;
    int defense;
    int hitrate;
    Random rand;
    boolean tiedUp;
    boolean alive;
    boolean sword;
    boolean shield;
    
    Player(String name, int hp, int strength, int defense, int stamina, int hitrate) {
        inventory = new HashMap<>();
        equipped = new HashMap<>();
        this.name = name;
        this.hp = hp;
        orighp = hp;
        this.strength = strength;
        this.stamina = stamina;
        this.defense = defense;
        this.hitrate = hitrate;
        tiedUp = false;
        alive = true;
        sword = false;
        shield = false;
    }
    
    public void setItem(String name, Item item) 
    {
        inventory.put(name, item);
    }
    
    public Item getItem(String name) {
        return inventory.get(name);
    }
    
    public void removeItem(String name) {
        inventory.remove(name);
    }
    
    public boolean checkItem(String name) {
        return inventory.containsKey(name);
    }
    
    public int getHp () {
        return hp;
    }
    
    public int getOrigHP () {
        return orighp;
    }
    
    public void setHp (int damage) {
        hp -= damage;
    }
    
    public int getHr() {
        return hitrate;
    }
    
    public int getAtk () {
        return strength;
    }
    
    public int getDf () {
        return defense;
    }
    
    public void addStrength(int i) {
        strength += i;
    }
    
    public void addDefense(int i) {
        defense += i;
    }
    
    public void addHR() {
        hitrate = 80;
    }
    
    public boolean checkTied() {
        return tiedUp;
    }
    
    public void changeTied() {
        tiedUp = true;
    }
    
    public boolean checkAlive() {
        if(hp > 0)
        alive = true;
        else {
        alive = false;
        }   
        return alive;
    }
    
    public void status() {
        System.out.println("Your HP: " + hp + " / " +orighp+ "\nYour Stamina: " + stamina);
    }
    
    public void enemyStatus() {
        System.out.println(name + "'s HP: " + hp);
    }
    
    public void updateStatus() {
        if (stamina > 0) {
            stamina -= 5;
        }
        else if(stamina <= 0) {
            hp -= 5;
        }
    }
    
    public void startStamina() {
        hp = 20;
        stamina = 20;
    }
    
    public void addStamina() {
        hp = 100;
        stamina = 100;
    }
    
    public void equip(String name) {
        equipped.put(name, inventory.remove(name));
    }
    
    public boolean checkEq(String name) {
        return equipped.containsKey(name);
    }
    
    public Item getEq(String name) {
        return equipped.get(name);
    }
    
    public String getItemString() {
        String returnString = "Inventory: ";
        Set<String> keys = inventory.keySet();
        for(String exit : keys) {
            returnString += exit + ", ";
        }
        String thing = "Equipped: ";
        Set<String> things = equipped.keySet();
        for(String bleh : things) {
            thing += bleh + ", ";
        }
        return (returnString + "\n" + thing);
    }
}
