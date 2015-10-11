import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import static java.util.Calendar.*;

/**
 * Solution to Assignment #7
 * <p>
 * <pre>
 * Loop over unfulfilled invoices to be shipped today.
 *   Loop over products included in an invoice.
 *     Loop over items for a product.
 *       Obtain an unused, empty box that can hold at least one of the items
 *       If there is no such box, then print an error message and exit.
 *       Otherwise, compute how many items can fit in the box.
 *       Place the items in the box.
 * </pre>
 * @author Xiao Wang
 */
public class Asst7 {
    public static void main(String... args) throws Exception {

	// Connect to the database.

	Class.forName("com.mysql.jdbc.Driver");
	Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/testdb", "root", "abcd");

	// Prepare all statements.  The getInvoices and getProducts statements
	// could be combined but the program is a little more complicated.

	PreparedStatement getInvoices = connection.prepareStatement
	    ("select i.id, i.shippedOn from Invoice i where not exists (select * from Box b where b.fulfills = i.id)");
	PreparedStatement getProducts = connection.prepareStatement
	    ("select p.id, p.weight, p.volume, c.count from Containment c, Product p " +
	     "where c.isContainedIn = ? and c.contains = p.id and c.count > 0");
	PreparedStatement getBox = connection.prepareStatement
	    ("select b.id, b.maximumWeight, b.volume from Box b " +
	     "where b.maximumWeight >= ? and b.volume >= ? and b.fulfills is null " +
	     "and not exists (select * from Containment c where c.isContainedIn = b.id) " +
	     "limit 1");
	PreparedStatement fulfillInvoice = connection.prepareStatement
	    ("update Box set fulfills = ? where id = ?");
	PreparedStatement packItems = connection.prepareStatement
	    ("insert into Containment(contains, isContainedIn, count) values(?,?,?)");

	// Today.

	Calendar today = Calendar.getInstance();
	today.setTime(new Date());
	int year = today.get(Calendar.YEAR);
	int day = today.get(Calendar.DAY_OF_YEAR);

	// The current box information.

	Integer boxId = null;
	double remainingWeight = 0;
	double remainingVolume = 0;

	// Loop over invoices.

	ResultSet invoices = getInvoices.executeQuery();
	while (invoices.next()) {
	    int invoiceId = invoices.getInt(1);
	    Calendar shippedOn = Calendar.getInstance();
	    shippedOn.setTime(invoices.getDate(2));
	    if (shippedOn.get(YEAR) == year && shippedOn.get(DAY_OF_YEAR) == day) {

		// Loop over products in the invoice.

		getProducts.setInt(1, invoiceId);
		ResultSet products = getProducts.executeQuery();
		while (products.next()) {
		    int productId = products.getInt(1);
		    double weight = products.getDouble(2);
		    double volume = products.getDouble(3);
		    int count = products.getInt(4);
		    
		    // Loop over the items for this product.

		    while (count > 0) {

			// Get a box if necessary.

			if (boxId == null || remainingWeight < weight || remainingVolume < volume) {
			    getBox.setDouble(1, weight);
			    getBox.setDouble(2, volume);
			    ResultSet box = getBox.executeQuery();
			    if (!box.next()) {
				System.err.println("Unable to pack the invoices.");
				return;
			    }
			    boxId = box.getInt(1);
			    remainingWeight = box.getDouble(2);
			    remainingVolume = box.getDouble(3);
			    box.close();

			    // This box fulfills the invoice

			    fulfillInvoice.setInt(1, invoiceId);
			    fulfillInvoice.setInt(2, boxId);
			    fulfillInvoice.executeUpdate();
			}

			// Compute the number of items that will fit in this box.
			
			int itemsToPack = Math.min(count, Math.min((int)(remainingWeight / weight),
								   (int)(remainingVolume / volume)));
			
			// Pack the items.

			packItems.setInt(1, productId);
			packItems.setInt(2, boxId);
			packItems.setInt(3, itemsToPack);
			packItems.executeUpdate();
			
			// Update the count, remaining weight and remaining volume for this box.

			count = count - itemsToPack;
			remainingWeight = remainingWeight - weight * itemsToPack;
			remainingVolume = remainingVolume - volume * itemsToPack;
		    }
		}
		products.close();
	    }
	}
	invoices.close();

	// Close the prepared statements.

	getInvoices.close();
	getProducts.close();
	getBox.close();
	fulfillInvoice.close();
	packItems.close();

	// Close the connection.

	connection.close();
    }
}
