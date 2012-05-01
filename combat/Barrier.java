package combat;
/**
 * @author DevA
 * @version $Id: Barrier.java,v 1.7 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: Barrier.java,v $
 *   Revision 1.7  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.6  2003/05/30 18:00:23  DevB
 *   Reformatted.
 *
 *   Revision 1.5  2000/05/12 04:10:22  DevA
 *   Barriers get cleaned up after a round now. (Using end methods.)
 *
 *   Revision 1.4  2000/05/12 01:54:40  DevA
 *   Fixed method headers to use javadoc comment blocks
 *
 *   Revision 1.3  2000/05/08 03:29:37  DevA
 *   Works with the addition of DirectionalImage.
 *
 *   Revision 1.2  2000/05/05 20:02:28  DevA
 *   Just some header cleanup.
 *
 *   Revision 1.1  2000/05/05 19:09:06  DevA
 *   Initial revision
 *
 */

//Source file: Barrier.java

/**
 * "In this same interlude it doth befall
 *  That I, one Snout by name, present a wall;"
 *	Shakespeare, A Midsummer Night's Dream, Act 5
 */
public class Barrier extends Sprite {
    
    /**
     * The constructor 
     * @param   img     The image to use
     * @param   new     Board The board to use
     */
    public Barrier( DirectionalImage img, Board newBoard )
    {
        super( img, newBoard );
    }

    /**
     * Instruct the barrier to handle the given conflict.  Because
     * barriers always win in collisions, this is a no-op. And because
     * barriers don't move, they can never move out of the playing 
     * area. (Assuming the client that created it checked to be sure
     * it placed it on the playing area to begin with)
     * @param   enemy   The sprite that is in conflict with this one.
     */
    public void conflict( Sprite enemy ){}

    /**
     * The barrier makes no moves so this is a no-op.
     */
    protected void move(){}

    /**
     * This is a no-op
     */
    protected void end()
    {
        onBoard = false;
    }

} //class Barrier
