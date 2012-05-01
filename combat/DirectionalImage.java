package combat;
/**
 * @author DevA
 * @version $Id: DirectionalImage.java,v 1.19 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: DirectionalImage.java,v $
 *   Revision 1.19  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.18  2003/05/30 18:32:12  DevB
 *   Reformatted.
 *
 *   Revision 1.17  2000/05/11 23:08:25  DevA
 *   Commenting.
 *
 *   Revision 1.16  2000/05/11 06:29:12  DevA
 *   Removed most debugs from the system and now everything works
 *   great on a New Game or New Round...at least as far as the user
 *   can tell.  I think that whichever Player won a round is left
 *   hanging a bit under the hood.  It never redraws, but it
 *   never really goes away.  Seems like a waste of processor time
 *   so I'm stil trying to figure this out.
 *
 *   Revision 1.15  2000/05/11 05:43:30  DevA
 *   Finally fixed trail issue.  Should have been copying
 *   currentLocation when storing lastLocation.  Forgot about Java
 *   reerences somehow.  Works great now.
 *
 *   Revision 1.14  2000/05/10 22:05:03  DevA
 *   Supporting minimizing trails by repainting last covered
 *   area if there has been a change and returning the bounds
 *   of this area.
 *
 *   Revision 1.13  2000/05/09 17:20:22  DevA
 *   Removed debugs.
 *
 *   Revision 1.12  2000/05/09 16:35:27  DevA
 *   Removed debugs
 *
 *   Revision 1.11  2000/05/09 15:26:35  DevA
 *   Fixed a bug in rotate
 *
 *   Revision 1.10  2000/05/09 14:35:33  DevC
 *   *** empty log message ***
 *
 *   Revision 1.9  2000/05/09 14:05:46  DevC
 *   tracking bugs
 *
 *   Revision 1.8  2000/05/09 08:00:15  DevA
 *   I was silly and didn't check parameter names.  It compiles now.
 *
 *   Revision 1.7  2000/05/09 07:56:43  DevA
 *   Had the same mistake in within.
 *
 *   Revision 1.6  2000/05/09 07:55:36  DevA
 *   Fixed a really dumb mistake in overlap.
 *
 *   Revision 1.5  2000/05/09 07:27:16  DevA
 *   Better debugs.
 *
 *   Revision 1.4  2000/05/08 22:06:40  DevA
 *   Got rid of some debugs and added setDirection
 *
 *   Revision 1.3  2000/05/08 21:12:19  DevA
 *   Added getLocation
 *
 *   Revision 1.2  2000/05/08 03:09:19  DevA
 *   Implemented, but still untested.
 *
 *   Revision 1.1  2000/05/08 02:26:12  DevA
 *   Initial revision
 *
 */

import java.util.*;
import java.awt.*;

/**
 * This class is, in effect, a proxy to the image currently representing
 * a sprite.  It can hold up to 9 images: a default image and one per
 * possible direction.  As far as clients of this class are concerned, they
 * have one image which can be translated and rotated, the location and
 * direction retrieved, and the image painted to a give Graphics object.
 */
public class DirectionalImage
{
    /**
     * Constants
     */
    static public int Default = 0;
    static public int North = 1;
    static public int NorthEast = 2;
    static public int East = 3;
    static public int SouthEast = 4;
    static public int South = 5;
    static public int SouthWest = 6;
    static public int West = 7;
    static public int NorthWest = 8;
    static public int Left = -1;
    static public int Right = 1;

    /**
     * The last location this image was at
     */
    Point lastLocation;

    /**
     * The last direction this image was facing
     */
    int lastDirection;

    /**
     * Whether or not this image has changed since the last paint
     */
    boolean changed;

    /**
     * The images hashed by their direction (and default is at 0)
     */
    Hashtable allImages;

    /**
     * The active image
     */
    int active;

    /* 
     * The location of this image
     */
    Point currentLocation;

    /**
     * The constructor 
     *	<ul> The direction is initialized to North </ul>
     * @param   base    The default image for this set.
     */
    public DirectionalImage( Image base )
    {
        currentLocation = new Point(0,0);
        lastLocation = new Point(0,0);
        lastDirection = 1;
        changed = false;
        allImages = new Hashtable();
        allImages.put(new Integer(0),base);
        active = 1;
    }

    /**
     * Sets the image to be used for the given direction
     * @param   image       The Image to use
     * @param   direction   The direction associated with this image
     * <ul> <em>direction</em> can vary from 1-8 which range from 
     * N->NE->E->SE->S->SW->W->NW . If an attempt is made to use
     * an image which has not been set, the default image is used.</ul>
     */
    public void setImage( Image image, int direction )
    {
        allImages.put( new Integer(direction), image );
    }

    /**
     * Sets the current direction represented by this object
     * @param   dir     The current direction ("active")
     */
    public void setDirection( int dir )
    {
        lastLocation = new Point( currentLocation );
        lastDirection = active;
        changed = true;
        active = dir;
    }

    /**
     * Provides the current direction represented by this object
     * @return	The current direction ("active")
     */
    public int getDirection( )
    {
        return active;
    }

    /**
     * Translates the image by the given amounts.
     * @param   dx  How much to shift in the x direction.
     * @param   dy  How much to shift in the y direction.
     */
    public void translate( int dx, int dy )
    {
        lastLocation = new Point( currentLocation );
        lastDirection = active;
        changed = true;
        currentLocation.x += dx;
        currentLocation.y += dy;
    }

    /**
     * Translates the image so that the origin is on the given location
     * @param   location    The point to translate to
     */
    public void translate( Point location )
    {
        lastLocation = new Point( currentLocation );
        lastDirection = active;
        changed = true;
        currentLocation = location;
    }

    /**
     * Rotates to the image to the left or right.
     * @param   turn    Whether to turn left or right.
     */
    public void rotate( int turn )
    {
        lastLocation = new Point( currentLocation );
        lastDirection = active;
        changed = true;
        active += turn;
        if( active == 0 ) active = 8;
        if( active > 8 ) active = 1;
    }

    /**
     * Determines whether or not the image overlaps with the one given.
     * @param   other   The image to compare to
     * @return  True if this image overlaps with (including borders) the given
     *          image.
     */
    public boolean overlap( DirectionalImage other )
    {
        Rectangle currentBounds = getBounds();
        Rectangle otherBounds = other.getBounds();
        return currentBounds.intersects( otherBounds );
    }

    /**
     * Determines whether or not the image is within the one given.
     * @param   bounds  What the image should fit within
     * @return  True if this image fits within (including borders) the given
     *          image.
     */
    public boolean within( Rectangle bounds )
    {
        Rectangle currentBounds = getBounds();
        return bounds.contains( currentBounds );
    }

    /**
     * Paints the active image to the give graphics object
     * @param   g   The Graphics object to paint to
     * @return  Returns the bounding Rectangle of the area that should
     *          be repainted immediately.
     */
    public Rectangle paint( Graphics g )
    {
        Rectangle result = new Rectangle( 0, 0, 0, 0 );
        if( changed )
        {
            Rectangle bounds = getLastBounds();
            g.fillRect( bounds.x, bounds.y, bounds.width, bounds.height );
            result = bounds;
            changed = false;
        }
        
        Image toDraw = (Image)allImages.get( new Integer(active) );
        if( toDraw != null )
        {
            g.drawImage( toDraw, 
            currentLocation.x, currentLocation.y, null, null );
        }
        else
        {
            g.drawImage( (Image)allImages.get(new Integer(0) ), 
            currentLocation.x, currentLocation.y, null, null );
        }
        
        return result;
    }

    /**
     * Returns the boundaries of the active image
     * @return  The bounding rectangle
     */
    public Rectangle getBounds( )
    {
        Image current = (Image)allImages.get( new Integer(active) );
        if( current == null ) current = (Image)allImages.get( new Integer(0) );
        int height = current.getHeight( null );
        int width = current.getWidth( null );
        return new Rectangle( currentLocation.x,
                              currentLocation.y,
                              width, height );
    }

    /**
     * Returns the boundaries of the last active image
     * @return  The bounding rectangle
     */
    private Rectangle getLastBounds()
    {
        Image current = (Image)allImages.get( new Integer(lastDirection) );
        if( current == null ) current = (Image)allImages.get( new Integer(0) );
        int height = current.getHeight( null );
        int width = current.getWidth( null );
        return new Rectangle( lastLocation.x, lastLocation.y, width, height );
    }

    /**
     * Returns the current location of this image
     * @return  Returns currentLocation
     */
    public Point getLocation()
    {
        return currentLocation;
    }

}//class DirectionalImage
