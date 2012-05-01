package combat;
/**
 * @author	DevB (DevB)
 * @version	$Id: TimeManager.java,v 1.10 2003/05/30 19:39:50 DevB Exp $
 *
 * Revision History:
 *   $Log: TimeManager.java,v $
 *   Revision 1.10  2003/05/30 19:39:50  DevB
 *   Reformatted.
 *
 *   Revision 1.9  2000/05/12 00:07:21  DevC
 *   cleanup & comments
 *
 *   Revision 1.8  2000/05/11 06:29:12  DevA
 *   Removed most debugs from the system and now everything works
 *   great on a New Game or New Round...at least as far as the user
 *   can tell.  I think that whichever Player won a round is left
 *   hanging a bit under the hood.  It never redraws, but it
 *   never really goes away.  Seems like a waste of processor time
 *   so I'm stil trying to figure this out.
 *
 *   Revision 1.7  2000/05/09 14:05:46  DevC
 *   tracking bugs
 *
 *   Revision 1.6  2000/05/09 07:58:06  DevB
 *   The removeAll method now works.  Wasn't reassigning the
 *   ImmutableList.
 *
 *   Revision 1.5  2000/05/09 07:18:38  DevA
 *   Just took out a debug.
 *
 *   Revision 1.4  2000/05/09 03:54:25  DevA
 *   Fixed the loop in run().  No longer while ( !paused ).  Instead
 *   while( true) { if (!paused) ...
 *
 *   Revision 1.3  2000/05/08 22:27:19  DevA
 *   Added sme debugs statements and removeAll.
 *
 *   Revision 1.2  2000/05/06 19:52:42  DevB
 *   Fixed javadoc class header.
 *
 *   Revision 1.1  2000/05/06 19:47:19  DevB
 *   Initial revision
 *
 *
 */

import java.util.*;

/**
 * This class controls the system time.  It cycles through a pretick
 * action on all objects, a tick action on all objects registered to
 * it, a tick action on all objects registered to it, and a delay until
 * it starts again.
 */

public class TimeManager extends Thread
{
    private volatile boolean paused = false;         //am I paused?
    private volatile int delay;                      //delay between ticks
    private volatile ImmutableList clocked = null;   //the clocked objects

    /**
     * This constructor sets the clock cycle delay.
     * @param   d               The time to set as the delay.
     * @throws  TimeException   If a negative delay is specified.
     */
    public TimeManager( int d ) throws TimeException
    {
        //set the delay time, if it is not negative
        if( d < 0 ) throw TimeException.NegativeDelay;
        delay = d;
    }

    /**
     * Registers a timed object with the TimeManager.  This object will
     * now recieve pretick and tick events until it becomes unregistered
     * or time stops.
     * @param   el  The element to register.
     */
    public void addTimed( Timed el )
    {
        clocked = ImmutableList.add( clocked, el );
    }

    /**
     * Empty the ImmutableList
     */
    public void removeAll()
    {
        clocked = ImmutableList.removeAll( clocked );
    }

    /**
     * This unregisters a timed object with the time manager.
     * @param   el  The element to unregister.
     */
    public void removeTimed(Timed el)
    {
        clocked = ImmutableList.remove( clocked, el );
    }

    /**
     * This method sets the clock cycle delay.
     * @param   d   The time to wait between one pretick/tick cycle and the
     *              next.
     * @throws  TimeException   If a negative delay is specified.
     */
    public void changeDelay(int d) throws TimeException
    {
        //set the delay time, if not negative
        if( d < 0 ) throw TimeException.NegativeDelay;
        delay = d;
    }

    /**
     * This method starts the manager running, overriding the Thread run
     * method.
     */
    public void run()
    {
        try
        {
            //loop indefiDevBly
            while( true )
            {
                //as longh as we're not paused...
                if( !paused )
                {
                    //get the list (enumeration) of timed objects
                    Enumeration e = ImmutableList.elements( clocked );
                    
                    // do a pretick broadcast
                    while( e.hasMoreElements() )
                        ((Timed) e.nextElement()).pretick();
                    
                    e = ImmutableList.elements( clocked );
                    // do a tick broadcast
                    while( e.hasMoreElements() )
                        ((Timed) e.nextElement()).tick();
                }
            //delay for the specified time
            Thread.sleep( delay );
            }
        }
        catch( InterruptedException e ){}
    }

    /**
     * This method stops time temporarily, between ticks.  If time is stopped,
     * it has no effect.
     */
    public void pause()
    {
        paused = true;
    }

    /**
     * Ths method resumes time when paused.  If time is running it has no
     * effect.
     */
    public void unpause()
    {
        paused = false;
    }

}
