import java.sql.ResultSet;
import java.sql.SQLException;

public class Department {
    String deptID;
    String name;
    String building;
    boolean valid;

    public Department () {
        this.setEmpty();
    }
    public Department (String deptID) {
        this.fillByID(deptID);
    }

    public void setEmpty(){
        this.deptID = null;
        this.name = null;
        this.building = null;
        this.valid = false;
    }

    public boolean fillByID(String deptID){
        String query = "SELECT * FROM departments where deptID = '"+deptID+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                System.out.println("Department with ID "+deptID+" not found ");
                return false;
            }
            System.out.println("Found a department with ID "+deptID);
            this.deptID=deptID;
            this.name = results.getString("name");
            this.building = results.getString("building");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return false;
        }
        return valid=true;
    }

    public boolean pushToDB(){
        if(!this.isValid()) {
            return false;
        }

        String query = "INSERT INTO departments" +
                "(deptID , name, building) VALUES (" +
                "" + deptID + "," +
                "'" + name + "'," +
                "'" + building + "')";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public boolean isValid(){
        return valid;
    }

    public boolean delete() {
        if(!this.isValid()) {
            System.out.println("Fill department first!!");
            return false;
        }

        String query = "DELETE FROM departments where deptID = '"+this.deptID+"'";
        valid=!Main.sql.executeSimpleQuery(query);
        if(!valid) {
            System.out.println("Successfully deleted the record.");
            this.setEmpty();
        }
        return valid;
    }

    public boolean update(){
        if(!this.isValid()) {
            System.out.println("Fill department first!!");
            return false;
        }

        String query = "UPDATE departments SET "+
                "name = '"+this.name+"',"+
                "building = '"+this.building+"' WHERE deptID = '"+this.deptID+"'";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public String toString(){
        if(!this.isValid()){
            return "Empty department";
        }
        return "-------- Department Details --------\n"+
                "ID: "+this.deptID+
                "\nName: "+this.name+
                "\nBuilding: "+this.building+
                "\n---------------------------------";
    }

    /* to view the list of instructors teaching a course */
    public int viewInstructors(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of instructors in "+this.name+"department\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT instructorID FROM instructors where deptID = '"+this.deptID+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            while(results.next()){
                Instructor instructor = new Instructor(results.getString("instructorID"));
                if(!compact)
                    System.out.println(instructor);
                count++;
            }
            System.out.println("Found "+count+" instructors\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return count;
        }
        return count;
    }

    /* to view the list of courses offered by the department */
    public int viewCourses(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of courses offered by "+this.name+"department\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT courseID FROM courses where deptID = '"+this.deptID+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            while(results.next()){
                Course course = new Course(results.getString("courseID"));
                if(!compact)
                    System.out.println(course);
                count++;
            }
            System.out.println("Found "+count+" courses\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return count;
        }
        return count;
    }
}
