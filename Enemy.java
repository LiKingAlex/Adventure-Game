/**
 * Description: This subclass represents the enemy and all their stats
 * 
 * 
 */

public class Enemy extends Character {
  // Constructor
  Enemy(byte round, String name) {
    // calling super constructor and scaling enemy
    super(name,
        (byte) (10 + (Math.floor(round / 3))), // armor (increases by 1 every 3 waves)
        (byte) (10 + (Math.floor(round / 3))), // accuracy (increases by 1 every 3 waves)
        (byte) (4 + 2 * (Math.floor(round / 3))), // damage die (increases by 2 every 3 waves)
        (byte) (1 + (Math.floor(round / 5))), // damage bonus (increases by 1 every 5 waves)
        (short) (20 + (3 * round))); // health (increases by 3 every wave)
  }
}
