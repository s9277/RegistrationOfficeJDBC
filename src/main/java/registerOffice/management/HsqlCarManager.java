package registerOffice.management;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import registerOffice.businessObjects.cars.PersonCar;

public class HsqlCarManager implements ManagerInterface<PersonCar>{

	
	//podlaczenie DB
	
	Connection connection;
	
	String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
	String createTable = "create table Cars (" +
	"id bigint GENERATED BY DEFAULT AS IDENTITY," +
			"mark varchar(30), registerNumber varchar(10))";
	
	Statement stmt;
	PreparedStatement getAll;
	PreparedStatement getCar;
	PreparedStatement deleteCar;
	PreparedStatement saveCar;
	
	
	public HsqlCarManager(){
		try {
			connection = DriverManager.getConnection(url);
			stmt = connection.createStatement();
			
			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			boolean tableExists = false;
			
			
			while (rs.next()){
				 if (rs.getString("TABLE_NAME").equalsIgnoreCase("Cars"))
				 {
					 tableExists = true;
				 	break;
				 }
					
				 	
			}
		
		if(!tableExists)
		{
			stmt.executeUpdate(createTable);
		}
		
		
		getAll = connection.prepareStatement(" " + "select * from Cars");
		getCar = connection.prepareStatement(" " + "select * from Cars where registerNumber=?");
		deleteCar = connection.prepareStatement(" " + "delete * from Cars where registerNumber = ?" );
		saveCar = connection.prepareStatement(" " + "insert into Cars(mark, registerNumber) values(?,?)");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
			
	
	@Override
	public PersonCar get(int id) {
		PersonCar result = null;
		try {
			getCar.setInt(1, id);
			
			ResultSet rs = getCar.executeQuery();
			
			while (rs.next()){
				result =new PersonCar(rs.getString("mark"), rs.getString("registerNumber"));
				break;
			}
			
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public List getAll() {
		
		List<PersonCar> result = new ArrayList<PersonCar>();
		
		ResultSet rs;
		try {
			rs = getAll.executeQuery();
			while (rs.next())
			{
				result.add(new PersonCar(rs.getString("mark"), rs.getString("registerNumber")));
				
			}
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public boolean save(PersonCar car) {
		
		
		try {
			saveCar.setString(1, car.getMark());
			saveCar.setString(2, car.getRegisterNumber());
			return saveCar.execute();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}

	@Override
	public boolean delete(PersonCar car) {
		
		try {
			deleteCar.setString(2,car.getRegisterNumber());
			deleteCar.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}

	

}