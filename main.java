
/**
 * Description: This program runs a game, in which the player has to fight against endless waves of monsters 
 * 
 * 
 */

//import java.util Classes
import java.util.Scanner;
import java.util.ArrayList;

public class main {
 
    public static void main(String[] args) {
        // clean the screen every time code runs
        System.out.println("\u000C");

        // declare variables which will be used later on
        String strUserName;

        // declare and initialize the option variable to choose for menu
        int intOption = 0;

        // starts the game with the user prompt
        // also gets user's name
        // STARTS THE WHOLE GAME.
        strUserName = startGame();

        while (true) {
            // gives the user a menu of options to choose from
            System.out.println("1. Play \n2. Exit\n3. Help");

            // verify the user input
            intOption = optionCheck(intOption);

            if (intOption == 1) {
                // loop so the user can select to play again and won't have to re-enter name
                do {
                    // call the game method
                    game(strUserName);
                } while (continueGame());

            } else if (intOption == 2) {
                System.out.println("Thanks for playing the game! Hope to see you next time!");
                break;
            } else if (intOption == 3) {
                // calls the info method
                info();
            } else {
                System.out.println("Invalid input!");
            }
        }
    }

    // Method that runs the entire game starting from the character selection
    
    public static void game(String strUserName) {
        // variable for the wave count
        int intWaveCount = 1;
        // variable for character choice
        String strCharacter;
        // player character
        Character player;

        // ask the user what class they want to play
        System.out.println("Which character would you like to choose?");
        System.out.println("(Warrior) or (Mage)");
        System.out.println("Warrior class allows you to have 16 armor and 60 health. Your special ability heals you.");
        System.out.println(
                "Mage allows you to have 14 armor and 45 health. Mages do more damage. \nYour special ability deals less damage than your normal attack, but cannot miss.");
        System.out.println();

        // verify user input
        strCharacter = characterOption();

        // create the actual player character
        if (strCharacter.equalsIgnoreCase("warrior")) {
            player = new Warrior(strUserName);
        } else {
            player = new Mage(strUserName);
        }

        // loop the game with a method
        while (wave(player, intWaveCount)) {
            intWaveCount++;
        }
        endGame(intWaveCount);
    }

    
    public static boolean wave(Character player, int intWaveCount) {
        // arraylist for all the enemies in the wave
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        // variable to loop through combat rounds
        int intContinue = 0;

        // generate enemies depending on the wave count
        for (byte i = 0; i < (Math.floor(intWaveCount / 5) + 2); i++) {
            enemies.add(new Enemy((byte) intWaveCount, "Enemy " + (i + 1)));
        }

        // output the wave number
        System.out.println("\nWave " + intWaveCount + "!\n" + enemies.size() + " enemies approaching!");

        // loop through each combat round
        // use a method that returns a 0 to continue, a 1 if the player dies, and a 2 if
        // all the enemies die
        // while enemy is alive and character is alive
        while (intContinue == 0) {
            intContinue = combatRound(player, enemies);
            if (intContinue == 1) {
                return false;
            }
        }
        // check if game over
        return true;
    }

    // method that runs each round of combat. returns 0 if both sides are alive, 1
    // if player dies, and 2 if enemies die 
    public static int combatRound(Character player, ArrayList<Enemy> enemies) {
        // prints the battlefield
        System.out.println("\nBattlefield:");
        printBattlefield(player, enemies);
        System.out.println();

        // player attacks
        playerAttack(player, enemies);

        // check if all enemies dead
        if (enemies.size() == 0) {
            return 2;
        }
        // else is unnecessary because of return

        // use a method to go through the enemy attacks
        if (enemyAttack(player, enemies)) {
            return 1;
        }

        // if neither party dies
        return 0;
    }

    // Method that manages the player's attack 
    public static void playerAttack(Character player, ArrayList<Enemy> enemies) {
        int intAttackChoice, intTargetChoice;
        byte bytTargetArmor, bytDamage;

        // player picks attack
        System.out.println("Which attack would you like to choose?");
        System.out.println("1. Normal Attack\n2. Special Ability");

        intAttackChoice = attackChoice();

        // if it's a warrior and they're using special, don't pick a target
        if (intAttackChoice == 2 && player instanceof Warrior) {
            ((Warrior) player).heal();
        }
        // otherwise, pick a target
        else {
            intTargetChoice = getTarget(enemies);
            // check if it was a mage special or a regular attack
            if (intAttackChoice == 1) {
                bytTargetArmor = enemies.get(intTargetChoice).getArmor();
                bytDamage = player.attack(bytTargetArmor);
            } else {
                bytDamage = ((Mage) player).special();
            }
            // player does damage to target
            enemies.get(intTargetChoice).changeHealth((short) -bytDamage);

            // output the message to the user that they hit/miss
            printAttack(player.getName(), enemies.get(intTargetChoice).getName(), bytDamage);

            // check if the enemy attacked died
            if (enemies.get(intTargetChoice).getHealth() <= 0) {
                System.out.println(enemies.get(intTargetChoice).getName() + " was defeated!");
                enemies.remove(intTargetChoice);
            }
        }
    }

    // Method for the enemies' attacks that returns true if the player dies 
    public static boolean enemyAttack(Character player, ArrayList<Enemy> enemies) {
        // variable to store player armor (to not have to make the calculations
        // repeatedly)
        byte bytArmor = player.getArmor();
        // variable to store the damage of each attack
        byte bytDamage;

        // loop through the arraylist and make attacks
        for (Enemy x : enemies) {
            // make the attack
            bytDamage = x.attack(bytArmor);
            // output damage
            printAttack(x.getName(), player.getName(), bytDamage);

            // damage the player
            player.changeHealth((short) -bytDamage);
            // check if the player is ded
            if (player.getHealth() <= 0) {
                return true;
            }
        }

        return false;
    }

    // Method that asks the user which target they want to attack 
    public static int getTarget(ArrayList<Enemy> enemies) {
        boolean bolTryCatch = false;
        int targetChoice = 0;

        System.out.println("Enter the number beside the name of the enemy you want to attack.");
        // loop through the array list and output
        for (int i = 0; i < enemies.size(); i++) {
            System.out.println((i + 1) + ": " + enemies.get(i).toString());
        }
        do {
            try {

                targetChoice = new Scanner(System.in).nextInt();
                // checks to see if user input is correct
                if (targetChoice <= 0 || targetChoice > enemies.size()) {
                    System.out.println("Please enter a number from 1 to " + enemies.size());
                    bolTryCatch = true;
                } else {
                    bolTryCatch = false;
                }
            } catch (Exception e) {
                System.out.println("Please enter a number from 1 to " + enemies.size());
                bolTryCatch = true;
            }
        } while (bolTryCatch);

        return targetChoice - 1;
    }

    // Method to output the information about the game
    public static void info() {
        System.out.println(
                "How the game works: As your character, you will enter the 1st round where you will encounter a group of monsters.");
        System.out.println("You will have the option to choose which attacks to use.");
        System.out.println("Your attacks will do damage, same with the monsters'.");
        System.out.println("Once you have 0 health, you lose. Once the monsters have 0 health, they die.");
        System.out.println(
                "If all the monsters die, you move on to the next wave. Each wave will get harder. Try to survive as many waves as possible!");
    }

    // Method that figures out which attack the user wants to use
    public static int attackChoice() {
        boolean bolTryCatch = false;
        int attack = 0;

        do {
            try {
                attack = new Scanner(System.in).nextInt();

                // checks to see if user input is correct
                if (attack != 1 && attack != 2) {
                    System.out.println("Please enter either '1 (Normal attack), 2(Special ability)");
                    bolTryCatch = true;
                } else {
                    bolTryCatch = false;
                }
            } catch (Exception e) {
                System.out.println("Please enter either '1 (Normal attack), 2(Special ability)");
                bolTryCatch = true;
            }
        } while (bolTryCatch);

        return attack;
    }

    // Method to check for users input when prompted the menu choice 
    public static int optionCheck(int menuCheck) {
        boolean bolTryCatch = false;

        do {
            try {
                menuCheck = new Scanner(System.in).nextInt();

                // checks to see if user input is correct
                if (menuCheck != 1 && menuCheck != 2 && menuCheck != 3) {
                    System.out.println("Please enter either '1 (Play)', '2(Exit)', or '3(Help)'");
                    bolTryCatch = true;
                } else {
                    bolTryCatch = false;
                }
            } catch (Exception e) {
                System.out.println("Please enter either '1 (Play)', '2(Exit)', or '3(Help)'");
                bolTryCatch = true;
            }
        } while (bolTryCatch);

        // returns the variable back to the main program
        return menuCheck;
    }

    // Method to check which character user inputs 
    public static String characterOption() {
        boolean bolTryCatch = false;
        String check;

        do {
            check = new Scanner(System.in).nextLine();

            // Checks to see if user inputted the correct characters they can choose from
            if (!check.equalsIgnoreCase("warrior") && !check.equalsIgnoreCase("mage")) {
                System.out.println("Please enter either 'warrior' or 'mage'");
                bolTryCatch = true;
            } else {
                bolTryCatch = false;
            }
        } while (bolTryCatch);

        return check;
    }

    // Method that ends the game 
    public static void endGame(int intWaveCounter) {
        String strPlayAgain;
        System.out.println("YOU LOST!!!");
        System.out.println("You lasted: " + intWaveCounter + "waves!");

        System.out.println("Would you like to play again?");
        strPlayAgain = new Scanner(System.in).nextLine();
        if (strPlayAgain.equalsIgnoreCase("yes")) {
            startGame();
        }
    }

    // Method that starts the game 
    public static String startGame() {
        String strUserName;

        System.out.println("WELCOME TO SLAYING THE BEAST ADVENTURES!");

        System.out.println("Please choose your name");

        strUserName = new Scanner(System.in).nextLine();

        return strUserName;

    }

    // Method to output attacks
    public static void printAttack(String strAttacker, String strTarget, byte bytDamage) {
        if (bytDamage <= 0) {
            System.out.println(strAttacker + " missed " + strTarget + "!");
        } else {
            System.out.println(strAttacker + " hit " + strTarget + " for " + bytDamage + " damage!");
        }
    }

    // Method to continue the game 
    public static boolean continueGame() {
        String strContinueGame;

        System.out.println("Would you like to continue the game? If so, please type 'yes'. ");
        strContinueGame = new Scanner(System.in).nextLine();
        if (strContinueGame.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }

    }

    // Method to print the description of the battlefield, which includes the player
    // and the enemies
    public static void printBattlefield(Character player, ArrayList<Enemy> enemies) {
        System.out.println(player);

        System.out.println();
        System.out.println("ENEMIES");
        for (Enemy y : enemies) {

            System.out.println(y);
        }
    }
}
