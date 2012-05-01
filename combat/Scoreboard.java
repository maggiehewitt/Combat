package combat;
/**
 * @author      DevB
 * @version     $Id: Scoreboard.java,v 1.4 2012/04/08 03:50:18 DevA Exp $
 *
 * In refactoring the system, this is the new location where the game score
 * will be placed.  It is being added because the current location is the main
 * class and doesn't really fit there.  It causes some coupling in the system
 * that leaves a bad taste in my mouth.
 *
 * Revision History:
 *   $Log: Scoreboard.java,v $
 *   Revision 1.4  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.3  2003/05/30 19:53:34  DevB
 *   Minor changes, more reformatting.
 *
 *   Revision 1.2  2003/05/30 14:50:55  DevB
 *   Added refreshing of scores to the screen.
 *
 *   Revision 1.1  2003/05/30 14:27:00  DevB
 *   Initial revision
 *
 */

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.GridLayout;

public class Scoreboard extends JPanel
{
	private static final long serialVersionUID = -1;
    private int player1Score;
    private int player2Score;
    
    private static String PLAYER_1 = "Player 1:   ";
    private static String PLAYER_2 = "Player 2:   ";
    private JLabel player1Display;
    private JLabel player2Display;
    
    /**
     *  This constructor initializes both players' scores to zero and sets up
     *  the layout of the board.
     */
    public Scoreboard()
    {
        super(new GridLayout(1, 2));
        player1Score = player2Score = 0;
        
        player1Display = new JLabel(PLAYER_1 + player1Score, JLabel.LEFT);
        player2Display = new JLabel(PLAYER_2 + player2Score, JLabel.RIGHT);
        
        add(player1Display);
        add(player2Display);
    }
    
    /**
     *  The accessor for a player's score.
     *  @param  playerNum   The player number to return the score to: 1 or 2.
     *  @return The specified player's score.
     *  @throws IllegalArgumentException    If the player number is not 1 or 2.
     */
    public int getScoreForPlayer(int playerNum) throws IllegalArgumentException
    {
        if(playerNum < 1 || playerNum > 2)
        {
            String errorMsg = "The player number must be 1 or 2.";
            throw new IllegalArgumentException(errorMsg);
        }
        
        return (playerNum == 1? player1Score : player2Score);
    }
    
    /**
     *  Increments the given player's score by 1.
     *  @param  playerNum   The player number to set the score of: 1 or 2.
     *  @throws IllegalArgumentException    If the player number is not 1 or 2.
     */
    public void incrementScoreForPlayer(int playerNum)
        throws IllegalArgumentException
    {
        if(playerNum < 1 || playerNum > 2)
        {
            String errorMsg = "The player number must be 1 or 2.";
            throw new IllegalArgumentException(errorMsg);
        }
        
        if(playerNum == 1) player1Score++;
        else player2Score++;
        
        refreshDisplay();
    }
    
    /**
     * Resets both players' scores to 0.
     */
    public void resetScores()
    {
        player1Score = player2Score = 0;
        
        refreshDisplay();
    }
    
    /**
     * Refreshes the display after a score change.
     */
    private void refreshDisplay()
    {
        // Reset the text in the label.
        player1Display.setText(PLAYER_1 + player1Score);
        player2Display.setText(PLAYER_2 + player2Score);
        
        //Redraw the labels.
        player1Display.update(getGraphics());
        player2Display.update(getGraphics());
    }

}
