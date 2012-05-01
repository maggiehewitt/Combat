package combat;
/** 
 * @author	DevB (DevB)
 * @version	$Id: ImmutableList.java,v 1.10 2003/05/30 18:39:47 DevB Exp $
 *
 * Revision History:
 *   $Log: ImmutableList.java,v $
 *   Revision 1.10  2003/05/30 18:39:47  DevB
 *   Reformatted.  Also removed some multiple return points
 *   from functions.
 *
 *   Revision 1.9  2000/05/12 00:13:29  DevC
 *   cleanup & comments
 *
 *   Revision 1.8  2000/05/09 18:17:27  DevB
 *   Now implements removeAll by removing every element from
 *   the list instead of handing a new empty one back.
 *   For some reason this works whereas the other didn't.
 *
 *   Revision 1.7  2000/05/09 07:57:47  DevB
 *   Made change to return value of removeAll.
 *
 *   Revision 1.6  2000/05/09 03:32:23  DevB
 *   Implemented removeAll.
 *
 *   Revision 1.5  2000/05/08 22:22:29  DevA
 *   Added stub for removeAll but need to talk to DevB
 *   before implementing this!!!
 *
 *   Revision 1.4  2000/05/06 19:52:42  DevB
 *   Fixed javadoc class header.
 *
 *   Revision 1.3  2000/05/06 19:44:26  DevB
 *   Added static keyword to elements method.
 *
 *   Revision 1.2  2000/05/06 19:42:20  DevB
 *   dos to unix
 *
 *   Revision 1.1  2000/05/06 17:08:06  DevB
 *   Initial revision
 *
 *
 */

import java.util.*;

/**
 * This class provides a list who's enumeration will not change when the
 * list is altered.  To get an up to date enumeration, call the elements
 * method again.
 */

public class ImmutableList
{
    ImmutableList next;      //current list
    Object item;             //current item on the list

    /**
     * Initialization constructor.
     * @param   next    The list to add to (can be null, signifying a new
     *                  list).
     * @param   item    The object to add.
     */
    private ImmutableList( ImmutableList next, Object item )
    {
        //initialize the next item and 
        this.next = next;
        this.item = item;
    }

    /**
     * This method adds an object to an immutable list, returning the altered
     * list.
     * @param   list    The list to add to.
     * @param   item    The object to add.
     * @return  A new immutable list with the element added.
     */
    public static ImmutableList add( ImmutableList list, Object item )
    {
        return new ImmutableList( list, item );
    }

    /**
     * This method removes an object from an immutable list, returning the
     * altered list.
     * @param   list    The list to remove from.
     * @param   target  The object to remove.
     * @return  A new immutable list which is missing that element.
     */
    public static ImmutableList remove( ImmutableList list, Object target )
    {
        ImmutableList returnVal = null;
        
        //as long as the list isn't empty, remove the target item from it
        if( list != null) returnVal = list.remove( target );
        
        return returnVal;
    }

    /**
     * This method removes all objects from an immutable list, returning the
     * altered list.
     * @param   list    The list to remove from.
     * @return  A new immutable list which is missing that element.
     */
    public static ImmutableList removeAll( ImmutableList list )
    {
        //loop thru the list, removing each item
        Enumeration e = elements( list );
        while( e.hasMoreElements() ) list.remove( e.nextElement() );
        return list;
    }

    /**
     * Actually performs the removal from the list.
     * @param   target  The object to remove.
     * @return  A new immutable list that is missing that element.
     */
    private ImmutableList remove( Object target )
    {
        ImmutableList returnVal = this;
        
        //loop thru the list, looking for the target item
        if( item == target )
        {
            returnVal = next;
        }
        else
        {
            ImmutableList new_next = remove( next, target );
            if( new_next != next )
                returnVal = new ImmutableList( new_next, item );
        }
        
        return returnVal;
    }

    /**
     * This method provides an enumeration of the elements in the list
     * at the time of the call, not changing despite adds or removes
     * called during iteration of the enumeration.
     * @return  An enumeration of the elements in the list.
     */
    public static Enumeration elements( ImmutableList list )
    {
        return new ImmutableListEnumeration( list );    
    }

}

/**
 * This class defines what an enumeration of an immutable list is.
 */
class ImmutableListEnumeration implements Enumeration {

    ImmutableList list;      //the immutable list to enumerate

    /**
     * Creates an immutable list enumeration by getting the list to
     * enumerate.
     * @param   list    The list to enumerate over.
     */
    ImmutableListEnumeration( ImmutableList list )
    {
        this.list = list;
    }

    /**
     * Whether or not there are more elements in the list.
     * @return  True if there are more elements, false otherwise.
     */
    public boolean hasMoreElements()
    {
        return ( list != null ); 
    }

    /**
     * Access to the next element in the immutable list.
     * @return  The next object in the immutable list.
     */
    public Object nextElement() {
        //get the next element on the list and rteturn it
        Object returnVal = list.item;
        list = list.next;
        return returnVal;
    }

}
