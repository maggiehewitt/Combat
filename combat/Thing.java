package combat;
/**
 * @author DevA
 * @version $Id: Thing.java,v 1.4 2012/04/08 03:50:18 DevA Exp $
 * Revisions:
 *   $Log: Thing.java,v $
 *   Revision 1.4  2012/04/08 03:50:18  DevA
 *   Cleaned up the code to run with Java 1.6: removed unused imports,
 *   fixed some UI focus issues (introduced by new focus "features" in Java since
 *   our original implementation), and made the CommandInterpreter not a Singleton
 *
 *   Revision 1.3  2003/05/30 18:57:02  DevB
 *   Reformatted.  Added some comments.
 *
 *   Revision 1.2  2000/05/06 17:08:25  DevA
 *   Just made it fit the template used in other classes.  Still needs comments, etc.
 *
 */

/**
 * Interface for powerups.  Yeah, it has a bad name at the moment.
 */
public interface Thing {
	
	/**
	 * Given the player that picked up the powerup so that the appropriate
     * modifications can take effect.
	 */
	public void pickedUp(Player player);

}
