package combat;
/** 
 * @author DevC
 * @version $Id: Bullet.java,v 1.12 2012/04/08 03:50:18 DevA Exp $
 *
 * This class defines a bullet object; it holds the visual representation of 
 *  the object, as well as the logic for how this object interacts with 
 *  other objects
 *
 * Revision History:
 *   $Log: Bullet.java,v $
 *   Revision 1.12  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.11  2003/05/30 18:12:12  DevB
 *   Reformatted.
 *
 *   Revision 1.10  2000/05/11 22:10:55  DevC
 *   FINALLY got the bullet algorithm (I hope!) and cleaned up,
 *   commented the code
 *
 *   Revision 1.9  2000/05/11 06:23:41  DevC
 *   worked on bounce algorithm...still not quite right
 *
 *   Revision 1.8  2000/05/10 04:58:08  DevA
 *   Made the amount of travel per tick a class variable so it's easier to change
 *
 *   Revision 1.7  2000/05/09 17:31:14  DevA
 *   Diagnol bouncing works correctly now.
 *
 *   Revision 1.6  2000/05/09 16:35:27  DevA
 *   Fires and ends when it hits a player or goes out of bounds.
 *
 *   Revision 1.5  2000/05/09 16:17:05  DevA
 *   Bullets fire and go in the correct direction :)
 *
 *   Revision 1.4  2000/05/09 01:19:45  DevC
 *   did all of the conflict handling procedure
 *
 *   Revision 1.3  2000/05/08 21:22:42  DevC
 *   changed constructor
 *
 *   Revision 1.2  2000/05/08 04:29:25  DevC
 *   changed the signature of the move() method;
 *   added some code for the conflict resolution.
 *
 *   Revision 1.1  2000/05/05 21:36:50  DevC
 *   Initial revision
 *
 */

import java.awt.*;

public class Bullet extends Sprite
{
    
    /**
     * The amount of travel per tick
     */
    int step = 7;

    /**
     * The last upper left & lower Right
     */
    Point lastUpL;
    Point lastLowR;
    
    /**
     * Initialization constructor.
     * @param   image       The image to use.
     * @param   newBoard    The board to use.
     */
    public Bullet( DirectionalImage image, Board newBoard )
    {
        //call the sprite constructor
        super( image, newBoard );
    }

    /**
     * Places myself on the board, if my position is visible.
     */
    public boolean place( Point location, int direction )
    {
        //places myself on the board.
        boolean result = false;
        
        //If my position is on the baord, I will need to translate myself
        //into view, register my move onto the board, and set the appropriate
        //indicators
        if( !onBoard )
        {
            image.translate( location );
            board.registerMove( image, this );
            result = true;
            onBoard = true;
            image.setDirection( direction );
        }
        return result;
    }

    /**
     * Instruct the player to handle the given conflict.  
     * @param   enemy   The sprite that is in conflict with this one.
     */
    public void conflict( Sprite enemy )
    {
        //determine what the object is that I am conflicting with and 
        // take the appropriate action
        if( enemy == null )
        {
            onBoard = false;
        }
        
        //On a bullet, nothing happens
        else if( enemy instanceof Bullet ){}
        
        else if( enemy instanceof Player )
        {
            //on another player, I bounce disappear
            onBoard = false;
        }
        
        //on a barrier, i bounce.  this is the tricky one, since my bounce 
        //depends on the direction i'm heading and the direction of the barrier
        else if( enemy instanceof Barrier )
        {
            //get the direction for checking purposes
            int dir = image.getDirection();
            
            //first, we check to see if we are heading in a "straight" or dead-on
            //direction, in which case we simply reverse direction.
            if( (dir == DirectionalImage.North) || 
                (dir == DirectionalImage.South) ||
                (dir == DirectionalImage.East) ||
                (dir == DirectionalImage.West) )
            {        
                //this factors in the "0" default direction
                image.setDirection( (( dir + 3 ) % 8) + 1 ); 
                board.registerMove( image, this );
            }
            else
            {
                //otherwise, we are moving diagonally, and so we need to figure our
                //next direction by which part of the box we hit.  To do that, we 
                //get the bounding box of the barrier and check some points
                Rectangle barrierBox = enemy.getBoundingBox(); 
                Point barUpperLeft = barrierBox.getLocation();
                Point barLowerRight = new Point( barUpperLeft.x + barrierBox.width, 
                                                 barUpperLeft.y + barrierBox.height );
                Point barUpperRight = new Point( barUpperLeft.x, 
                                                 barUpperLeft.y + barrierBox.height );
                Point barLowerLeft = new Point( barUpperLeft.x + barrierBox.width, 
                                                barUpperLeft.y );
                
                //and we need my position
                Rectangle bulletBox = getBoundingBox(); 
                Point bulUpperLeft = bulletBox.getLocation();
                Point bulLowerRight = new Point( bulUpperLeft.x + bulletBox.width, 
                                                 bulUpperLeft.y + bulletBox.height );
                Point bulUpperRight = new Point( bulUpperLeft.x, 
                                                 bulUpperLeft.y + bulletBox.height );
                Point bulLowerLeft = new Point( bulUpperLeft.x + bulletBox.width, 
                                                bulUpperLeft.y );
                
                //now, we decide which way to bounce case by case
                
                //Basically, the bouncing algorithm checks first to see which
                //we're heading, then from that tries to determine if I crossed
                //the barrier from on the x plane or the y plane (since in each 
                //direction it is only possible to cross each plae one way).
                //this will work along as I'm not moving in real large incements
                //(i.e. real fast)
                
                //northern movement
                if( (dir == DirectionalImage.NorthWest) )
                {
                    if( (bulUpperLeft.x <= barLowerRight.x ) &&
                        (!(lastUpL.x >= barLowerRight.x)) )
                    {
                        image.setDirection( DirectionalImage.SouthWest );
                    }
                    else if( (bulUpperLeft.y <= barLowerRight.y ) )
                    {
                        image.setDirection( DirectionalImage.NorthEast );
                    }
                    else
                    {
                        System.err.println( "ERROR - NW" );
                    }
                }
                
                if( (dir == DirectionalImage.NorthEast) )
                {
                    if( (bulUpperLeft.x >= barLowerRight.x ) &&
                        (!(lastUpL.x >= barLowerRight.x)) )
                    {
                        image.setDirection( DirectionalImage.SouthEast );
                    }
                    else if( (bulLowerRight.y >= barUpperLeft.y ) )
                    {
                        image.setDirection( DirectionalImage.NorthWest );
                    }
                    else
                    {
                        System.err.println( "ERROR - NE" );
                    }
                }
                
                //now for southernly movement
                if( (dir == DirectionalImage.SouthWest) )
                {
                    if( (bulLowerRight.x <= barUpperLeft.x ) &&
                        (!(lastLowR.x <= barUpperLeft.x)) )
                    {
                        image.setDirection( DirectionalImage.NorthWest );
                    }
                    else if( (bulUpperLeft.y <= barLowerRight.y ) )
                    {
                        image.setDirection( DirectionalImage.SouthEast );
                    }
                    else
                    {
                        System.err.println( "ERROR - SW" );
                    }
                }
                
                if( (dir == DirectionalImage.SouthEast) )
                {
                    if( (bulLowerRight.x >= barUpperLeft.x ) &&
                        (!(lastLowR.x <= barUpperLeft.x)) )
                    {
                        image.setDirection( DirectionalImage.NorthEast );
                    }
                    else if( (bulLowerRight.y >= barUpperLeft.y ) )
                    {
                        image.setDirection( DirectionalImage.SouthWest );
                    }
                    else
                    {
                        System.err.println( "ERROR - SE" );
                    }
                }
                
                //register the move with the board
                board.registerMove( image, this );
            }
        }
        
        //here for completeness
        else{}
    }

    /**
     * Forward movement of the Bullet.  This is also the only movement of the
     * bullet that does anything, since bullets travel in a straight line.
     */
    public void moveForward()
    {
        if( onBoard )
        {
            //store the last upper right & lower left; i use this in barrier
            //conflicts to tell which way I hit the barrier
            Rectangle bulletBox = getBoundingBox(); 
            lastUpL = bulletBox.getLocation();
            lastLowR = new Point( lastUpL.x + bulletBox.width, 
            lastUpL.y + bulletBox.height ); 
            
            
            //get the direction of the sprite to find what is foreward;
            int dir = image.getDirection();
            
            //now check to see which way to move
            switch( dir )
            {
                case( 1 ):
                    image.translate( 0, -step );
                    break;
                case( 2 ):
                    image.translate( step, -step );
                    break;
                case( 3 ):
                    image.translate( step, 0 );
                    break;
                case( 4 ):
                    image.translate( step, step );
                    break;
                case( 5 ):
                    image.translate( 0, step );
                    break;
                case( 6 ):
                    image.translate( -step, step );
                    break;
                case( 7 ):
                    image.translate( -step, 0 );
                    break;
                case( 8 ):
                    image.translate( -step, -step );
                    break;
                default:
            }
            
            //register my move with the board
            board.registerMove( image, this );
        }
    }

   /*
    * Backward movement of the Bullet;  will never be called in current
    * implementation, as our bullets do not move backward.  here for 
    * completeness
    */
    public void moveBackward(){}

   /*
    * Right turn of the Bullet;  will never be called in current
    * implementation, as our bullets do not turn on command.  here for 
    * completeness
    */
    public void turnRight(){}

   /*
    * left turn of the Bullet;  will never be called in current
    * implementation, as our bullets do not turn on command.  here for 
    * completeness
    */
    public void turnLeft(){}

   /*
    * The player makes and returns its next move
    * this will never get called, since Players aren't ticked - 
    * their managers are.  Here for consistancy
    * @return	The player's new location.
    */
    protected void move(){}

    /*
    * This is a no-op
    */
    protected void end(){}

}
