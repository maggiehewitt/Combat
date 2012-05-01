package combat;
/**
 * @author DevA
 * @version $Id: LevelBuilder.java,v 1.16 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: LevelBuilder.java,v $
 *   Revision 1.16  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.15  2003/05/30 18:54:18  DevB
 *   Reformatted.  Fixed a method that had multiple returns
 *   from it.
 *
 *   Revision 1.14  2000/05/12 04:10:22  DevA
 *    Barriers get cleaned up after a round now. (Using end methods.)
 *
 *   Revision 1.13  2000/05/11 23:19:16  DevA
 *   Commenting and deleted the setBoard method calls b/c this is completely
 *   redundant.
 *
 *   Revision 1.12  2000/05/11 07:12:04  DevA
 *   Removed a debug.
 *
 *   Revision 1.11  2000/05/11 06:29:12  DevA
 *   Removed most debugs from the system and now everything works
 *   great on a New Game or New Round...at least as far as the user
 *   can tell.  I think that whichever Player won a round is left
 *   hanging a bit under the hood.  It never redraws, but it
 *   never really goes away.  Seems like a waste of processor time
 *   so I'm stil trying to figure this out.
 *
 *   Revision 1.10  2000/05/10 22:05:03  DevA
 *   Removed debugs.
 *
 *   Revision 1.9  2000/05/09 18:56:25  DevA
 *   Changed the key commands for player1 :)
 *
 *   Revision 1.8  2000/05/09 17:20:22  DevA
 *    Threading support for scoring in place in Game and
 *    PlayerManager.
 *
 *   Revision 1.7  2000/05/09 16:35:27  DevA
 *   Starts the PlayerManager threads
 *
 *   Revision 1.6  2000/05/09 15:58:47  DevC
 *   fixed the key mappings so two players can play at once
 *
 *   Revision 1.5  2000/05/09 14:59:41  DevA
 *   Takes a JPanel instead of a Graphics object
 *
 *   Revision 1.4  2000/05/09 14:05:46  DevC
 *   tracking bugs
 *
 *   Revision 1.3  2000/05/09 04:58:23  DevA
 *   Don't think I actually changed anything.
 *
 *   Revision 1.2  2000/05/08 22:22:29  DevA
 *   General debugging and added some debug statements so
 *   we know things have parsed corectly.
 *
 *   Revision 1.1  2000/05/08 17:01:28  DevA
 *   Initial revision
 *
 */

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * The class that puts together the barriers and players for starting.
 */
public class LevelBuilder
{
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
    LinkedList barriers;

    /**
     * The JPanel this round will be show on.
     */
    JPanel canvas;
    
    /**
     * The command interpreter
     */
    CommandInterpreter ci;

    
    /**
     * The constructor 
     */
    public LevelBuilder( String filename, JPanel canvas, CommandInterpreter ci )
    {
        barriers = new LinkedList();
        this.ci = ci;
        try
        {
            BufferedReader input = new BufferedReader( new
            FileReader( filename ) );
            
            while( input.ready() )
            {
                String line = input.readLine();
                StringTokenizer tokens = new StringTokenizer( line );
                
                String type = tokens.nextToken();
                
                if( type.equals("Background") )
                {
                    ImageIcon tmp = new ImageIcon( tokens.nextToken() );
                    board = new Board( tmp.getImage(), canvas );
                }

                if( type.equals("Player1") ) addPlayer( 1, tokens );
                if( type.equals("Player2") ) addPlayer( 2, tokens );
                if( type.equals("Barrier") ) addBarrier(tokens);
            }
        }
        catch( Exception e )
        {
            System.err.println( e.toString() );
        }
    }

    /**
     * Builds another barrier from the given tokens
     * @param   tokens  The input data
     */
    private void addBarrier( StringTokenizer tokens )
    {
        Integer x = new Integer( tokens.nextToken() );
        Integer y = new Integer( tokens.nextToken() );
        Point location = new Point( x.intValue(), y.intValue() );
        ImageIcon tmp = new ImageIcon( tokens.nextToken() );
        DirectionalImage image = new DirectionalImage( tmp.getImage() );
        Barrier barrier = new Barrier( image, board );
        barrier.place( location );
        barriers.add( barrier );
    }

    /**
     * Builds the requested PlayerManager from the given tokens
     * @param   playerID    The player being parsed
     * @param   tokens      The input data
     */
    private void addPlayer( int playerID, StringTokenizer tokens )
    {
        Integer x = new Integer( tokens.nextToken() );
        Integer y = new Integer( tokens.nextToken() );
        Point location = new Point( x.intValue(), y.intValue() );
        ImageIcon tmp = new ImageIcon( tokens.nextToken() );
        DirectionalImage image = new DirectionalImage( tmp.getImage() );
        
        for( int i=1; i<9; i++ )
        {
            tmp = new ImageIcon( tokens.nextToken() );
            image.setImage( tmp.getImage(), i );
        }
        
        Player player = new Player( image, board );
        player.place( location );
        tmp = new ImageIcon( tokens.nextToken() );
        DirectionalImage bulletImg = new DirectionalImage( tmp.getImage() );
        Bullet bullet = new Bullet( bulletImg, board );
        
        switch(playerID)
        {
            case 1:  player1 = new PlayerManager( 1, KeyEvent.VK_W, 
                KeyEvent.VK_Z, KeyEvent.VK_D, KeyEvent.VK_A, 
                KeyEvent.VK_Q, player, bullet, ci );
                player1.start();
                break;
            case 2:  player2 = new PlayerManager( 2, KeyEvent.VK_UP, 
                KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, 
                KeyEvent.VK_SHIFT, player, bullet, ci );
                player2.start();
                break;
            default:
        }
    }

    /**
     * Provides a list of all Timed objects for this board
     * @return  A LinkedList containing all objects to be registered with
     *          the TimeManager
     */
    public LinkedList getTimed()
    {
        LinkedList result = new LinkedList( barriers );
        result.add( board );
        result.add( player1 );
        result.add( player2 );
        return result;
    }
    
    /**
     * Returns the current board object.
     * @return  Board  The current Board.
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * Determine whether or not the PlayerManager is still running.
     * @param    player  Which player is in question.
     * @return   True if the player is still alive.
     */
    public boolean playerAlive( int player )
    {
        return (player == 1? player1.isAlive() : player2.isAlive());
    }

    /**
     * End the given player
     * @param   player  The PlayerManager to end
     */
    public void endPlayer( int player )
    {
        if( player == 1 ) player1.end();
        else player2.end();
    }

    /**
     * Calls end on every barrier
     */
    public void cleanUp()
    {
        ListIterator list = barriers.listIterator(0);
        while( list.hasNext() )
        {
            Barrier tmp = (Barrier)list.next();
            tmp.end();
        }
    }

}//class LevelBuilder
