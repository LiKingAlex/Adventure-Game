/**
 * Description: This subclass represents the mage (one of the character options)
 * which includes all of the stats.
 * 
 * 
 */

public class Mage extends Character {
  // making mage constructor
  Mage(String name) {
    super(name, (byte) 14, (byte) 7, (byte) 6, (byte) 3, (byte) 45);
  }

  // mage special
  public byte special() {
    int min = 4;
    int max = 9;

    // Generate random int value from 50 to 100
    int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
    return (byte) random_int;
  }

}
