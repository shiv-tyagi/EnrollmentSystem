import java.sql.ResultSet;
import java.sql.SQLException;

public class Duty {
    String instructorID;
    String courseID;

    // secondary fields
    String insName;
    String courseName;
    boolean valid;

    public Duty(){
        this.setEmpty();
    }

    private void setEmpty(){
        this.instructorID = null;
        this.courseID = null;
        this.insName = null;
        this.courseName = null;
        this.valid = false;
    }

    public boolean pushToDB(){
        if(!this.isValid()) {
            return false;
        }

        String query = "INSERT INTO duties" +
                "(instructorID , courseID) VALUES (" +
                "'" + this.instructorID + "'," +
                "'" + this.courseID + "')";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public boolean isValid() { return this.valid; }

    public boolean fill(String instructorID, String courseID){
        String query = "SELECT * FROM duties where instructorID = '"+instructorID+"' and courseID ='"+courseID+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                System.out.println("Duty not found");
                return false;
            }
            System.out.println("Found a matching duty");
            this.instructorID=instructorID;
            this.courseID = courseID;
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return false;
        }
        return valid=true;
    }

    public String toString(){
        if(!this.isValid()){
            return "Empty duty";
        }
        String res = "";
        res = res.concat("----------- Enrollment Details -----------\n"+
                "Instructor: "+this.instructorID+
                "\nCourse ID: "+this.courseID);
        if(this.populateSecFields()) {
            res = res.concat("\nInstructor Name: "+this.insName+
                    "\nCourse Name: "+this.courseName);
        }
        res = res.concat("\n-----------------------------------------");
        return  res;
    }

    private boolean populateSecFields(){
        if(!this.isValid()) {
            return false;
        }
        String query = "SELECT instructors.instructorID, instructors.name as insName, courses.name as courseName, courses.courseID FROM duties, instructors, courses where duties.instructorID = '"+instructorID+"' and duties.courseID ='"+courseID+"' and duties.instructorID=instructors.instructorID and courses.courseID = duties.courseID";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                return false;
            }
            this.insName= results.getString("insName");
            this.courseName = results.getString("courseName");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return false;
        }
        return true;

    }

    public boolean delete() {
        if(!this.isValid()) {
            System.out.println("Fill duty first!!");
            return false;
        }

        String query = "DELETE FROM duties where instructorID = '"+this.instructorID+"' and courseID = '"+this.courseID+"'";
        valid=!Main.sql.executeSimpleQuery(query);
        if(!valid) {
            System.out.println("Successfully deleted the record.");
            this.setEmpty();
        }
        return valid;
    }
}
