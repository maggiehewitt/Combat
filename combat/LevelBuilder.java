package combat;

/**
 * @author DevA
 * @version $Id: LevelBuilder.java,v 1.16 2012/04/08 03:50:18 DevA Exp $
 *          Revisions:
 *          $Log: LevelBuilder.java,v $
 *          Revision 1.16 2012/04/08 03:50:18 DevA
 *          Cleaned up the code to run with Java 1.6: removed unused imports,
 *          fixed some UI focus issues (introduced by new focus "features" in
 *          Java since
 *          our original implementation), and made the CommandInterpreter not a
 *          Singleton
 * 
 *          Revision 1.15 2003/05/30 18:54:18 DevB
 *          Reformatted. Fixed a method that had multiple returns
 *          from it.
 * 
 *          Revision 1.14 2000/05/12 04:10:22 DevA
 *          Barriers get cleaned up after a round now. (Using end methods.)
 * 
 *          Revision 1.13 2000/05/11 23:19:16 DevA
 *          Commenting and deleted the setBoard method calls b/c this is
 *          completely
 *          redundant.
 * 
 *          Revision 1.12 2000/05/11 07:12:04 DevA
 *          Removed a debug.
 * 
 *          Revision 1.11 2000/05/11 06:29:12 DevA
 *          Removed most debugs from the system and now everything works
 *          great on a New Game or New Round...at least as far as the user
 *          can tell. I think that whichever Player won a round is left
 *          hanging a bit under the hood. It never redraws, but it
 *          never really goes away. Seems like a waste of processor time
 *          so I'm stil trying to figure this out.
 * 
 *          Revision 1.10 2000/05/10 22:05:03 DevA
 *          Removed debugs.
 * 
 *          Revision 1.9 2000/05/09 18:56:25 DevA
 *          Changed the key commands for player1 :)
 * 
 *          Revision 1.8 2000/05/09 17:20:22 DevA
 *          Threading support for scoring in place in Game and
 *          PlayerManager.
 * 
 *          Revision 1.7 2000/05/09 16:35:27 DevA
 *          Starts the PlayerManager threads
 * 
 *          Revision 1.6 2000/05/09 15:58:47 DevC
 *          fixed the key mappings so two players can play at once
 * 
 *          Revision 1.5 2000/05/09 14:59:41 DevA
 *          Takes a JPanel instead of a Graphics object
 * 
 *          Revision 1.4 2000/05/09 14:05:46 DevC
 *          tracking bugs
 * 
 *          Revision 1.3 2000/05/09 04:58:23 DevA
 *          Don't think I actually changed anything.
 * 
 *          Revision 1.2 2000/05/08 22:22:29 DevA
 *          General debugging and added some debug statements so
 *          we know things have parsed corectly.
 * 
 *          Revision 1.1 2000/05/08 17:01:28 DevA
 *          Initial revision
 * 
 */

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * The class that puts together the barriers and players for starting.
 */
public class LevelBuilder {
    /**
     * The board object
     */
    Board board;

    /**
     * The player objects
     */
    PlayerManager player1;
    PlayerManager player2;

    /**
     * A LinkedList of all barriers in the level
     */
    Set<Barrier> barriers;

    /**
     * The JPanel this round will be show on.
     */
    JPanel canvas;

    /**
     * The command interpreter
     */
    CommandInterpreter ci;

    /**
     * The command bindings
     */
    Properties keyBindings;

    public static final String BINDINGS_FILE = "bindings.properties";

    public LevelBuilder(JPanel canvas, CommandInterpreter ci) {
        barriers = new HashSet<Barrier>();
        this.ci = ci;
        this.canvas = canvas;
        this.keyBindings = new Properties();
        loadKeyBindings();
    }

    public void rebuild(String file) {
        barriers.clear();
        this.player1 = null;
        this.player2 = null;

        loadKeyBindings();

        Scanner input = null;

        try {
            input = new Scanner(new File(file));
        } catch (Exception e) {
            throw new RuntimeException("Could not make file " + file + ": " + e.getMessage());
        }

        while (input.hasNext()) {
            String line = input.nextLine();
            StringTokenizer tokens = new StringTokenizer(line);

            String type = tokens.nextToken();

            if (type.equals("Background")) {
                ImageIcon tmp = new ImageIcon(tokens.nextToken());
                board = new Board(tmp.getImage(), canvas);
            }

            if (type.equals("Player1"))
                addPlayer(1, tokens);
            if (type.equals("Player2"))
                addPlayer(2, tokens);
            if (type.equals("Barrier"))
                addBarrier(tokens);
        }
    }

    private void loadKeyBindings() {
        try {
            Reader bindingsReader = new BufferedReader(new FileReader(BINDINGS_FILE));
            this.keyBindings.load(bindingsReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds another barrier from the given tokens
     * 
     * @param tokens The input data
     */
    private void addBarrier(StringTokenizer tokens) {
        Integer x = new Integer(tokens.nextToken());
        Integer y = new Integer(tokens.nextToken());
        Point location = new Point(x.intValue(), y.intValue());
        ImageIcon tmp = new ImageIcon(tokens.nextToken());
        DirectionalImage image = new DirectionalImage(tmp.getImage());
        Barrier barrier = new Barrier(image, board);
        barrier.place(location);
        barriers.add(barrier);
    }

    /**
     * Builds the requested PlayerManager from the given tokens
     * 
     * @param playerID The player being parsed
     * @param tokens The input data
     */
    private void addPlayer(int playerID, StringTokenizer tokens) {
        Integer x = new Integer(tokens.nextToken());
        Integer y = new Integer(tokens.nextToken());
        Point location = new Point(x.intValue(), y.intValue());
        ImageIcon tmp = new ImageIcon(tokens.nextToken());
        DirectionalImage image = new DirectionalImage(tmp.getImage());

        for (int i = 1; i < 9; i++) {
            tmp = new ImageIcon(tokens.nextToken());
            image.setImage(tmp.getImage(), i);
        }

        Player player = new Player(image, board);
        player.place(location);
        tmp = new ImageIcon(tokens.nextToken());
        DirectionalImage bulletImg = new DirectionalImage(tmp.getImage());
        Bullet bullet = new Bullet(bulletImg, board);

        switch (playerID) {
            case 1:
                int[] cmds = { 0, 0, 0, 0, 0 };
                cmds[0] = Integer.parseInt(this.keyBindings.getProperty("p1-up"));
                cmds[1] = Integer.parseInt(this.keyBindings.getProperty("p1-down"));
                cmds[2] = Integer.parseInt(this.keyBindings.getProperty("p1-right"));
                cmds[3] = Integer.parseInt(this.keyBindings.getProperty("p1-left"));
                cmds[4] = Integer.parseInt(this.keyBindings.getProperty("p1-fire"));
                player1 = new PlayerManager(1, cmds, player, bullet, ci);
                break;
            case 2:
                int[] cmds2 = { 0, 0, 0, 0, 0 };
                cmds2[0] = Integer.parseInt(this.keyBindings.getProperty("p2-up"));
                cmds2[1] = Integer.parseInt(this.keyBindings.getProperty("p2-down"));
                cmds2[2] = Integer.parseInt(this.keyBindings.getProperty("p2-right"));
                cmds2[3] = Integer.parseInt(this.keyBindings.getProperty("p2-left"));
                cmds2[4] = Integer.parseInt(this.keyBindings.getProperty("p2-fire"));
                player2 = new PlayerManager(2, cmds2, player, bullet, ci);

                break;
            default:
        }
    }

    /**
     * Provides a list of all Timed objects for this board
     * 
     * @return A LinkedList containing all objects to be registered with
     *         the TimeManager
     */
    public LinkedList getTimed() {
        LinkedList result = new LinkedList(barriers);
        result.add(board);
        result.add(player1);
        result.add(player2);
        return result;
    }

    /**
     * Returns the current board object.
     * 
     * @return Board The current Board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Determine whether or not the PlayerManager is still running.
     * 
     * @param player Which player is in question.
     * @return True if the player is still alive.
     */
    public boolean playerAlive(int player) {
        return (player == 1 ? player1.isAlive() : player2.isAlive());
    }

    /**
     * End the given player
     * 
     * @param player The PlayerManager to end
     */
    public void endPlayer(int player) {
        if (player == 1 && player1 != null)
            player1.end();
        else if (player == 2 && player2 != null)
            player2.end();
    }

    /**
     * Calls end on every barrier
     */
    public void cleanUp() {
        for (Barrier b : barriers)
            b.end();
    }

}// class LevelBuilder
