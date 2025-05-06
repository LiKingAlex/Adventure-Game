/**
 * Description: Subclass Character is meant for the attributes of the main character. It includes their stats and information.
 * 
 * 
 */


import java.util.Random;

public class Character {
  //all the variables necessary for a character
  private short shrCurrentHealth;
  private short shrMaxHealth;
  private byte bytArmor;
  private byte bytAccuracy;
  private byte bytRandomDamage;
  private byte bytDamageBonus;
  private byte bytDamageDie;
  private String strName;

  //constructor
  Character(String name, byte armor, byte accuracy, byte DamageDie, byte DamageBonus, short health) {
    this.bytArmor = armor;
    this.bytAccuracy = accuracy;
    this.bytDamageDie = DamageDie;
    this.bytDamageBonus = DamageBonus;
    this.strName = name;
    this.shrCurrentHealth = health;
    this.shrMaxHealth = health;

  }

  //default constructor (unused)
  Character() {
    bytArmor = 0;
    bytAccuracy = 0;
    bytDamageDie = 0;
    bytDamageBonus = 0;
  }

  //method which changes the current health value of the character
  public void changeHealth(short change) {
    shrCurrentHealth = (short) (shrCurrentHealth + change);

    if (shrCurrentHealth > shrMaxHealth) {
      shrCurrentHealth = shrMaxHealth;
    }

  }

  //method which makes the character attack, returning the damage dealt (0 in the case of a miss)
  public byte attack(byte targetArmor) {
    Random dice = new Random();

    if (targetArmor > (bytAccuracy + (byte) (dice.nextInt(20) + 1))) {
      return 0;
    }
    bytRandomDamage = (byte) ((1 + dice.nextInt(bytDamageDie)) + bytDamageBonus);
    return bytRandomDamage;
  }

  public byte getArmor() // getter method
  {
    return bytArmor;
  }

  public String getName() // getter method
  {
    return strName;
  }

  public short getHealth() // getter method
  {
    return shrCurrentHealth;
  }

  //toString method displaying name and health
  public String toString() {
    return this.strName + ": " + this.shrCurrentHealth + "/" + this.shrMaxHealth + " HP";
  }

}
