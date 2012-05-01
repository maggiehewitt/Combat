package combat;
/**
 * @author DevA
 * @version $Id: Board.java,v 1.19 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: Board.java,v $
 *   Revision 1.19  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.18  2003/05/30 18:16:16  DevB
 *   Reformatted.
 *
 *   Revision 1.17  2000/05/11 23:29:21  DevA
 *   Commenting.  (Just making sure all the method headers are
 *   up to date)
 *
 *   Revision 1.16  2000/05/11 06:29:12  DevA
 *   Removed most debugs from the system and now everything works
 *   great on a New Game or New Round...at least as far as the user
 *   can tell.  I think that whichever Player won a round is left
 *   hanging a bit under the hood.  It never redraws, but it
 *   never really goes away.  Seems like a waste of processor time
 *   so I'm stil trying to figure this out.
 *
 *   Revision 1.15  2000/05/11 05:44:25  DevA
 *   Trailing issue solved in DirectionalImage.  Using
 *   paintImmediately correctly here.
 *
 *   Revision 1.14  2000/05/10 22:13:57  DevC
 *   tried to work with buffered images
 *
 *   Revision 1.12  2000/05/09 15:26:35  DevA
 *   Fixed conflict searching. (Was initializing "block"
 *   incorrectly).
 *
 *   Revision 1.11  2000/05/09 14:57:56  DevA
 *   Takes a JPanel instead of a Graphics object, but not extending JPanel.
 *
 *   Revision 1.8  2000/05/09 07:28:40  DevA
 *   Just trying to figure out why painting isn't working right.
 *   No real progress.
 *
 *   Revision 1.7  2000/05/09 04:11:18  DevA
 *   At this point, we get Sprites on the screen, but we're not
 *   drawing the background.
 *
 *   Revision 1.6  2000/05/08 22:22:29  DevA
 *   Fixed some errors in pretick.
 *     - Looping issue and changed how to get bounds
 *
 *   Revision 1.5  2000/05/08 03:09:19  DevA
 *   Implemented, but still untested.
 *
 *   Revision 1.4  2000/05/08 02:26:12  DevA
 *   Now interface reflects the shift to using DirectionalImage instead
 *   of Area.
 *
 *   Revision 1.3  2000/05/05 21:58:40  DevA
 *   Uses Area now.
 *
 *   Revision 1.2  2000/05/05 20:02:28  DevA
 *   Just some header cleanup.
 *
 *   Revision 1.1  2000/05/05 19:09:06  DevA
 *   Initial revision
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;

//Source file: Board.java
/**
 * On every pretick, a Board finds conflicts between all moves registered 
 * with it since the last pretick, asks the Sprites involved to handle the 
 * conflict, and then asks all the Sprites which registered to repaint 
 * themselves.
 */
public class Board implements Timed
{
    /**
     * The background of this board
     * <ul>The background image should really just be a Rectangle
     * representing the area of the board since we never draw the bg.  
     * We could draw it, but it just looks better without it.</ul>
     */
    private Image background;

    /**
     * The graphics object to draw to
     */
    private JPanel area;

    /**
     * A HashTable containing all the Sprites which have registered since
     * the last pretick and their images.
     */
    Hashtable spriteMoves;

    /**
     * The constructor 
     * @param   bg          The background image to use.
     * @param   playArea    The graphics object to draw to.
     */
    public Board( Image bg, JPanel playArea )
    {
        background = bg;
        area = playArea;
        spriteMoves = new Hashtable();
        area.setDoubleBuffered( true );
    }

    /**
     * Allows a Sprite to register the area it just covered.
     * @param   move    The area just covered.
     * @param   owner   The Sprite that just covered this area.
     */
    public void registerMove( DirectionalImage image, Sprite owner )
    {
        spriteMoves.put( owner, image );
    }

    /**
     * Returns the Graphics object being used
     * @return  The Graphics object used by this Board.
     */
    public Graphics getGraphics()
    {
        return area.getGraphics();
    }

    /**
     * Calculates which Sprites have collided, notifies them to handle
     * these collisions, and then tells as the of the Sprites which
     * have registered since the last pretick to repaint.  Finally,
     * the Board resets the Hashtable.
     */
    public void pretick() 
    {
        int height = background.getHeight( null );
        int width = background.getWidth( null );
        Rectangle bounds = new Rectangle( 0, 0, width, height );
        Enumeration sprites = spriteMoves.keys();
        for( int i=0; sprites.hasMoreElements(); i++ )
        {
            Sprite sprite = (Sprite)sprites.nextElement();
            DirectionalImage attempt =
                (DirectionalImage)spriteMoves.get( sprite );
            Enumeration enemies = spriteMoves.keys();
            
            if( !attempt.within( bounds ) )
            {
                sprite.conflict( null );
            }
            
            for( int j=0; enemies.hasMoreElements(); j++ )
            {
                Sprite enemy = (Sprite)enemies.nextElement();
                if( i!=j )
                {
                    DirectionalImage block = 
                        (DirectionalImage)spriteMoves.get( enemy );
                    
                    if( attempt.overlap( block ) )
                    {
                        sprite.conflict( enemy );
                    }
                }
            }
        }
        
        sprites = spriteMoves.keys();
        while( sprites.hasMoreElements() )
        {
            Sprite next = (Sprite)sprites.nextElement();
            area.paintImmediately( next.repaint( area.getGraphics() ) );
        }
        spriteMoves = new Hashtable();
    }

    /**
    * The Board does nothing on a tick.
    */
    public void tick(){}

}//class Board
