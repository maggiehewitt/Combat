package combat;

/**
 * @author DevC
 * @version $Id: CommandInterpreter.java,v 1.8 2012/04/08 03:50:18 DevA Exp $
 * 
 *          CommandInterpreter class. Listens on the frame to grab keyboard
 *          events and
 *          pass them along to the game or the players
 * 
 *          Revision History:
 *          $Log: CommandInterpreter.java,v $
 *          Revision 1.8 2012/04/08 03:50:18 DevA
 *          Cleaned up the code to run with Java 1.6: removed unused imports,
 *          fixed some UI focus issues (introduced by new focus "features" in
 *          Java since
 *          our original implementation), and made the CommandInterpreter not a
 *          Singleton
 * 
 *          Revision 1.7 2003/05/30 18:25:25 DevB
 *          Reformatted, and added an exception to register for
 *          completeness.
 * 
 *          Revision 1.6 2000/05/11 22:19:24 DevC
 *          cleaned up code & corrected comments
 * 
 *          Revision 1.5 2000/05/09 06:08:04 DevC
 *          error checking in the keyPressed;
 *          reset the command
 * 
 *          Revision 1.4 2000/05/05 04:47:40 DevC
 *          implemented the remainder of the methods.
 *          Compiles with PM and a stub Player
 * 
 *          Revision 1.3 2000/05/04 01:21:10 DevC
 *          set up as listener; set up singleton construct
 * 
 *          Revision 1.1 2000/05/04 01:08:11 DevC
 *          Initial revision
 * 
 *          Revision 1.2 2000/05/04 01:13:57 DevC
 *          changed from rose file to one that I created; cleaner
 * 
 * 
 * 
 */

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

public class CommandInterpreter extends JFrame implements KeyListener {
    private static final long serialVersionUID = -1;

    private PlayerManager player1;
    private PlayerManager player2;

    private Set<MoveCommand> p1Commands;
    private Set<MoveCommand> p2Commands;

    // private int p1Command = 0; // player 1's current command
    // private int p2Command = 0; // player 2's current command

    private int p1cmds[] = new int[5]; // array of commands for player 1
    private int p2cmds[] = new int[5]; // array of commands for player 2

    private Map<String, MoveCommand> moveMap; // id -> move
    private Map<Integer, String> keyMap; // key -> id

    /**
     * Private constructor in order to follow the singleton pattern of
     * creation.
     */
    public CommandInterpreter() {
        // creation and setup for the main window for the system.
        super("COMBAT!");

        setupMoveBindings();

        // add listeners to the window for keystroke and window commands
        setSize(750, 700);
        setResizable(false);
        addKeyListener(this);
        setFocusable(true);
        setLayout(new BorderLayout());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.err.println("Creating the first command interpreter.");
    }

    public void useKeyMap(Map<Integer, String> map) {
        this.keyMap = map;
    }

    private void setupMoveBindings() {
        moveMap = new HashMap<String, MoveCommand>();
        keyMap = new HashMap<Integer, String>();
        p1Commands = new HashSet<MoveCommand>();
        p2Commands = new HashSet<MoveCommand>();

        for (int i = 1; i <= 2; i++) {
            moveMap.put("p" + i + "-up", new MoveCommand() {
                @Override
                void move() {
                    System.out.println(getPlayer());
                    getPlayer().moveForward();
                }
            });

            moveMap.put("p" + i + "-down", new MoveCommand() {
                @Override
                void move() {
                    getPlayer().moveBackward();
                }
            });

            moveMap.put("p" + i + "-left", new MoveCommand() {
                @Override
                void move() {
                    getPlayer().turnLeft();
                }
            });

            moveMap.put("p" + i + "-right", new MoveCommand() {
                @Override
                void move() {
                    getPlayer().turnRight();
                }
            });

            moveMap.put("p" + i + "-fire", new MoveCommand() {
                @Override
                void move() {
                    getPlayer().fire();
                }
            });
        }

        for (Map.Entry<String, MoveCommand> entry : moveMap.entrySet()) {
            String key = entry.getKey();
            MoveCommand cmd = entry.getValue();

            if (key.startsWith("p1"))
                p1Commands.add(cmd);

            if (key.startsWith("p2"))
                p2Commands.add(cmd);
        }
    }

    /**
     * A No-op; we do nothing specific on a key type.
     * Here for completion and adherance of the KeyListener interface.
     * 
     * @param e The key event generated by the user
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Handles interpretation of key events; sets the player's command,
     * if the key was a command key.
     * 
     * @param e The key event generated by the user
     */
    public void keyPressed(KeyEvent e) {
        // get the key code for the event
        int code = e.getKeyCode();

        String moveID = keyMap.get(code);
        System.out.println(moveID);
        MoveCommand command = moveMap.get(moveID);
        command.move();
    }

    /**
     * A No-op; we do nothing specific on a key release.
     * Here for completion and adherance of the KeyListener interface.
     * 
     * @param e The key event generated by the user
     */
    public void keyReleased(KeyEvent e) {
    }

    public void register(int id, PlayerManager player) {
        switch (id) {
            case 1:
                this.player1 = player;
                for (MoveCommand cmd : p1Commands) {
                    cmd.setPlayer(this.player1);
                }
                System.out.println("adding for p1: " + player);
                break;

            case 2:
                this.player2 = player;
                for (MoveCommand cmd : p2Commands) {
                    cmd.setPlayer(this.player2);
                }
                System.out.println("adding for p2: " + player);
                break;

            default:
                throw new RuntimeException("id " + id + " is not valid.");
        }
    }
}
