package jdbc;
import java.sql.*;
import java.util.Calendar;

public class PackagesByInvoices {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/?user=root/hw7";
	private Connection conn; 
	
	public void packagesTodayInvoices(String date) throws SQLException {
		Statement listTodayInvoices = conn.createStatement();
		String sql = "select i.id, c.contains, c.count, p.weight, p.volume"
				+ " from Invoice i, Product p, Containment c, Grouping g" +
				" where i.shippedOn = '" + date + "' and i.id = g.id and" +
				" c.contains = p.id and c.isContainedIn = g.id" +
				" order by i.id";
		ResultSet rsInvoices = listTodayInvoices.executeQuery(sql);
		
		sql = "select g.id, b.id, b.maximumWeight, b.volume"
				+ " from Grouping g, Box b where b.id = g.id and b.fulfills is null ";
		ResultSet rsBoxes = listTodayInvoices.executeQuery(sql);
		
		
		int lastInvoiceID = 0;
		int remain = 0;
		while (rsBoxes.next()) {
			int boxGID = rsBoxes.getInt(1);
			int boxID = rsBoxes.getInt(2);
			double maximumWeight = rsBoxes.getDouble(3);
			double maximumVolume = rsBoxes.getDouble(4);

			while (rsInvoices.next()) {
				int invoiceID = rsInvoices.getInt(1);
				int contains = rsInvoices.getInt(2);
				int count = rsInvoices.getInt(3);
				double weight = rsInvoices.getDouble(4);
				double volume = rsInvoices.getDouble(5);
				
				if (rsInvoices.getRow() == 1) {
					lastInvoiceID = invoiceID;
				}
				
				if (lastInvoiceID != invoiceID) {
					lastInvoiceID = invoiceID;
					rsInvoices.previous();
					break;
				}
				
				if (remain != 0) {
					count = remain;
				}
				
				if ((long)count * weight <= maximumWeight && (long)count * volume <= maximumVolume) {
					Statement update = conn.createStatement();
					sql = "update Box set fulfills = " + Integer.toString(invoiceID)
							+ " where id = " + Integer.toString(boxID);
					update.executeUpdate(sql);
					
					sql = "update Containment set contains = " + Integer.toString(contains)
							+ ", count = " + Integer.toString(count) 
							+ " where isContainedIn = " + Integer.toString(boxGID);	
					update.executeUpdate(sql);
					maximumWeight = maximumWeight - (long)count * weight;
					maximumVolume = maximumVolume - (long)count * volume;
					remain = 0;
				}
				else {
					while ((long)count * weight > maximumWeight || (long)count * volume > maximumVolume) {
						count--;
						remain++;
					}
					Statement update = conn.createStatement();
					sql = "update Box set fulfills = " + Integer.toString(invoiceID)
							+ " where id = " + Integer.toString(boxID);
					update.executeUpdate(sql);
					
					sql = "update Containment set contains = " + Integer.toString(contains)
							+ ", count = " + Integer.toString(count) 
							+ " where isContainedIn = " + Integer.toString(boxGID);	
					update.executeUpdate(sql);
					rsInvoices.previous();
					break;
				}
			}	
		}
		
		int endRow = rsInvoices.getRow();
		rsInvoices.last();
		int lastRow = rsInvoices.getRow();
		if (endRow < lastRow && remain != 0) {
			System.out.println("We run out of boxes!");
			System.exit(0);
		}
		
		listTodayInvoices.close();
		conn.close();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PackagesByInvoices pbi = new PackagesByInvoices();
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			pbi.conn = DriverManager.getConnection(DB_URL);
			System.out.println("Connected database successfully...");
	
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE);
			
			String today = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
			
			
			pbi.packagesTodayInvoices(today);
		}catch(SQLException se) {
			se.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pbi.conn != null)
					pbi.conn.close();
			}catch(SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
