import java.sql.ResultSet;
import java.sql.SQLException;

public class Enrollment {
    String rollNo;
    String courseID;

    // secondary fields
    String stdName;
    String courseName;
    boolean valid;

    public Enrollment(){
        this.setEmpty();
    }

    private void setEmpty(){
        this.rollNo = null;
        this.courseID = null;
        this.stdName = null;
        this.courseName = null;
        this.valid = false;
    }

    public boolean pushToDB(){
        if(!this.isValid()) {
            return false;
        }

        String query = "INSERT INTO enrollments" +
                "(rollNo , courseID) VALUES (" +
                "'" + this.rollNo + "'," +
                "'" + this.courseID + "')";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public boolean isValid() { return this.valid; }

    public boolean fill(String rollNo, String courseID){
        String query = "SELECT * FROM enrollments where rollNo = '"+rollNo+"' and courseID ='"+courseID+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                System.out.println("Enrollement not found");
                return false;
            }
            System.out.println("Found a matching enrollment");
            this.rollNo=rollNo;
            this.courseID = courseID;
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return false;
        }
        return valid=true;
    }

    public String toString(){
        if(!this.isValid()){
            return "Empty enrollment";
        }
        String res = "";
        res = res.concat("-------- Enrollment Details --------\n"+
                "Roll No: "+this.rollNo+
                "\nCourse ID: "+this.courseID);
        if(this.populateSecFields()) {
            res = res.concat("\nStudent Name: "+this.stdName+
                    "\nCourse Name: "+this.courseName);
        }
        res = res.concat("\n----------------------------------");
        return  res;
    }

    private boolean populateSecFields(){
        if(!this.isValid()) {
            return false;
        }
        String query = "SELECT students.rollNo, students.name as stdName, courses.name as courseName, courses.courseID FROM enrollments, students, courses where enrollments.rollNo = '"+rollNo+"' and enrollments.courseID ='"+courseID+"' and enrollments.rollNo=students.rollNo and courses.courseID = enrollments.courseID";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                return false;
            }
            this.stdName= results.getString("stdName");
            this.courseName = results.getString("courseName");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return false;
        }
        return true;
    }

    public boolean delete() {
        if(!this.isValid()) {
            System.out.println("Fill enrollment first!!");
            return false;
        }

        String query = "DELETE FROM enrollments where rollNo = '"+this.rollNo+"' and courseID = '"+this.courseID+"'";
        valid=!Main.sql.executeSimpleQuery(query);
        if(!valid) {
            System.out.println("Successfully deleted the record.");
            this.setEmpty();
        }
        return valid;
    }
}
