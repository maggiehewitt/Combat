package combat;

/**
 * @author DevB
 * @version $Id: Combat.java,v 1.18 2012/04/08 03:50:18 DevA Exp $
 * 
 *          Revision History:
 *          $Log: Combat.java,v $
 *          Revision 1.18 2012/04/08 03:50:18 DevA
 *          Cleaned up the code to run with Java 1.6: removed unused imports,
 *          fixed some UI focus issues (introduced by new focus "features" in
 *          Java since
 *          our original implementation), and made the CommandInterpreter not a
 *          Singleton
 * 
 *          Revision 1.17 2003/05/30 14:43:47 DevB
 *          Decreased coupling by introducing the scoreboard to
 *          handle scoring instead of acting as an intermediary
 *          between the Game which held the score and the
 *          CombatMenu that is displaying the score.
 * 
 *          Revision 1.16 2003/05/30 14:27:28 DevB
 *          Changed some formatting, also introduced the scoreboard
 *          onto the panel. Now need to phase out the scoring in
 *          the menu.
 * 
 *          Revision 1.15 2000/05/11 23:49:32 DevC
 *          cleanup and comments
 * 
 *          Revision 1.14 2000/05/10 20:44:45 DevC
 *          added method to allow update of UI for scoring
 * 
 *          Revision 1.13 2000/05/09 17:20:22 DevA
 *          Threading support for scoring in place in Game and
 *          PlayerManager.
 * 
 *          Revision 1.12 2000/05/09 15:57:03 DevB
 *          Game doesn't draw over the button panel anymore.
 * 
 *          Revision 1.11 2000/05/09 15:26:35 DevA
 *          Made timeDelay smaller.
 * 
 *          Revision 1.10 2000/05/09 14:57:56 DevA
 *          Just set timeDelay back to 1 second
 * 
 *          Revision 1.9 2000/05/09 07:27:51 DevA
 *          Just trying to figure out why painting isn't working right.
 *          No real progress.
 * 
 *          Revision 1.8 2000/05/09 05:54:52 DevC
 *          added the listener for the kb events
 * 
 *          Revision 1.7 2000/05/09 04:57:25 DevB
 *          Moved the menu building to a panel in the CombatMenu class
 *          because of oddities while drawing.
 * 
 *          Revision 1.6 2000/05/09 03:52:41 DevA
 *          I honestly don't remember what I changed...if anything. Whatever
 *          I did or didn't changed, it kinda works now. DevB needs to make
 *          buttons instead of menus now.
 * 
 *          Revision 1.5 2000/05/08 22:22:29 DevA
 *          Change buildLevel in Game back to setLevel.
 * 
 *          Revision 1.4 2000/05/08 04:21:13 DevB
 *          Fixed to be compatible with changes in game: parseLevel
 *          to buildLevel. Fixed comments.
 * 
 *          Revision 1.3 2000/05/08 03:30:44 DevB
 *          Made window starting size bigger, and added a level
 *          selector window.
 * 
 *          Revision 1.2 2000/05/08 01:33:55 DevB
 *          Got a preliminary window popping up, everything exits
 *          ok now too.
 * 
 *          Revision 1.1 2000/05/06 19:48:53 DevB
 *          Initial revision
 * 
 * 
 */

import java.awt.BorderLayout;

/**
 * This class start up the game. It puts together the GUI and instantiates
 * the main classes and links them together.
 */
public class Combat {

    static Game theGame; // the game object appearing in the combat screen
    static CommandInterpreter ci;

    /**
     * main
     * This is COMBAT! This sets up the main UI, hooks up the main
     * components and starts it running.
     * 
     * @param argv[] The command line arguments.
     */
    public static void main(String argv[]) {
        Scoreboard sb = new Scoreboard();
        theGame = new Game(sb);
        CommandInterpreter ci = new CommandInterpreter();

        // attempt to load the game by creatign a time manager &
        // game obejct, which gets started.
        // if the load fails, end execution
        LevelBuilder levelBuilder = new LevelBuilder(theGame, ci);

        theGame.setLevelBuilder(levelBuilder);

        // Create the menu, then place it, as well as the game and
        // scoreboard,
        // on the content pane.
        CombatMenu cm = new CombatMenu(theGame);
        KeyDialog kDialog = new KeyDialog(ci);
        cm.setKeyConfigurationDialog(kDialog);

        ci.useKeyMap(kDialog.getBindings());

        ci.add(cm, BorderLayout.NORTH);
        ci.add(theGame, BorderLayout.CENTER);
        ci.add(sb, BorderLayout.SOUTH);

        // display the finished frame
        ci.setVisible(true);
    }// main()
}
