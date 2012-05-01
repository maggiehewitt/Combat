package combat;
/**
 * @author DevA
 * @version $Id: Sprite.java,v 1.20 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: Sprite.java,v $
 *   Revision 1.20  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.19  2003/05/30 19:34:54  DevB
 *   Reformatted.  Also fixed a method with multiple return
 *   points.
 *
 *   Revision 1.18  2000/05/11 23:19:16  DevA
 *   Commenting and deleted setBoard b/c it was completely redundant.
 *
 *   Revision 1.17  2000/05/11 07:12:04  DevA
 *   Got rid of code that was originally supposed to help cleanup
 *   after a round, but, instead, was just causing a phantom
 *   player to stick around.  (the "last" boolean was useless)
 *
 *   Revision 1.16  2000/05/11 06:29:12  DevA
 *   Removed most debugs from the system and now everything works
 *   great on a New Game or New Round...at least as far as the user
 *   can tell.  I think that whichever Player won a round is left
 *   hanging a bit under the hood.  It never redraws, but it
 *   never really goes away.  Seems like a waste of processor time
 *   so I'm stil trying to figure this out.
 *
 *   Revision 1.15  2000/05/10 22:05:03  DevA
 *   repaint() returns the area to repaint immediately.
 *
 *   Revision 1.14  2000/05/09 16:35:27  DevA
 *   Added the isActive method
 *
 *   Revision 1.13  2000/05/09 15:26:35  DevA
 *   Removed debugs.
 *
 *   Revision 1.12  2000/05/09 14:35:33  DevC
 *   *** empty log message ***
 *
 *   Revision 1.11  2000/05/09 14:05:46  DevC
 *   tracking bugs
 *
 *   Revision 1.10  2000/05/08 22:27:19  DevA
 *   Added some debug statements.
 *
 *   Revision 1.9  2000/05/08 21:12:19  DevA
 *   Added getLocation
 *
 *   Revision 1.8  2000/05/08 21:07:43  DevC
 *   added bounding box method
 *
 *   Revision 1.7  2000/05/08 03:09:19  DevA
 *   Implemented, but still untested.
 *
 *   Revision 1.6  2000/05/08 02:28:18  DevA
 *   Removed direction member.  This should be retrieved from
 *   DirectionalImage now.
 *
 *   Revision 1.5  2000/05/08 02:26:12  DevA
 *   Now interface reflects the shift to using DirectionalImage instead
 *   of Area.
 *
 *   Revision 1.4  2000/05/06 17:16:53  DevA
 *   Added a direction class member.
 *
 *   Revision 1.3  2000/05/05 21:58:05  DevA
 *   Added a bit of implementation to place and tick.
 *
 *   Revision 1.2  2000/05/05 20:02:28  DevA
 *   Just some header cleanup.
 *
 *   Revision 1.1  2000/05/05 19:09:06  DevA
 *   Initial revision
 */

import java.awt.*;

/**
 * On every tick, a Sprite will execute one action or move, register
 * this action with its Board, and wait for a tick or pretick.  Sprites
 * actually do nothing on a pretick and handle collisions or repaint whenever
 * they are instructed to do so.
 */
abstract public class Sprite implements Timed
{
    /**
     * The image representing this sprite.
     */
    protected DirectionalImage image;

    /**
     * Whether or not this Sprite is currently on the Board
     */
    protected boolean onBoard;

    /**
     * The board to use
     */
    protected Board board;

    /**
     * The constructor 
     * @param   img         The image to use
     * @param   newBoard    The board to use
     */
    public Sprite( DirectionalImage img, Board newBoard )
    {
        image = img;
        board = newBoard;
        onBoard = false;
    }

    /**
     * The Sprite does nothing on a pretick.
     */
    public void pretick(){}

    /**
     * On every tick, a Sprite will execute one action or move, 
     * register this action with its Board.
     */
    public void tick() 
    {
        move();
        board.registerMove( image, this );
    }

    /**
     * Places the sprite on the board with its origin at the given point.
     * The sprite will also then reigster this location with the Board.
     * @param   location   Where to place the sprite
     * @return  True if the place was successful or false if the sprite
     *          could not be placed because it was already on the board.
     */
    public boolean place( Point location )
    {
        boolean result = false;
        if( !onBoard )
        {
            image.translate( location.x, location.y );
            board.registerMove( image, this );
            result = true;
            onBoard = true;
        }
        return result;
    }
    
    /**
     * The sprite repaints itself to the board.
     * @param   g   The Graphics object to paint to
     * @return  The bounding Rectangle of the area to be repainted immediately.
     */
    public Rectangle repaint( Graphics g )
    {
        return (onBoard? image.paint( g ) : new Rectangle( 0, 0, 0, 0));
    }

    /**
     * Instruct the sprite to handle the given conflict
     * @param   enemy   The sprite that is in conflict with this one.
     * <ul> If this is null, it means that the sprite moved off of the
     * playing field and must correct this. </ul>
     */
    abstract public void conflict( Sprite enemy );

    /**
     * The sprite makes and returns its next move
     */
    abstract protected void move();

    /**
     * Causes the sprite to remove itself from the board and end
     * operation.
     */
    abstract protected void end();
    
    /**
     * Get the upper left and lower right points; 
     * this is used to handle 
     * @return  The bounding box
     */
    public Rectangle getBoundingBox()
    {
        return( image.getBounds() );
    }

    /**
     * Returns the current location of this sprite
     * @return  A Point object reprsenting the origin of this Sprite
     */
    public Point getLocation()
    {
        return image.getLocation();
    }

    /**
     * Returns true if the Sprite is active
     * @return  True if the sprite is currently being shown on the board
     */
    public boolean isActive()
    {
        return onBoard;
    }

}//class Sprite
