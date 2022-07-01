import java.sql.ResultSet;
import java.sql.SQLException;

public class Instructor {
    String instructorID;
    String name;
    String phone;
    String deptID;
    String deptName;
    boolean valid;
    public Instructor () {
        this.setEmpty();
    }

    public Instructor (String instructorID) {
        this.fillByID(instructorID);
    }

    public boolean pushToDB(){
        if(!this.isValid()) {
            return false;
        }

        String query = "INSERT INTO instructors" +
                "(instructorID , name, phone, deptID) VALUES (" +
                "" + instructorID + "," +
                "'" + name + "'," +
                "'" + phone + "'," +
                "" + deptID + ")";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public boolean fillByID(String instructorID){
        String query = "SELECT instructors.name as insName, phone, departments.name as deptName, departments.deptID as deptID FROM instructors, departments where instructorID = '"+instructorID+"' and instructors.deptID = departments.deptID";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                System.out.println("Instructor with ID "+instructorID+" not found ");
                return false;
            }
            System.out.println("Found an instructor with ID="+instructorID);
            this.instructorID = instructorID;
            this.name = results.getString("insName");
            this.phone = results.getString("phone");
            this.deptID = results.getString("deptID");
            this.deptName = results.getString("deptName");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return false;
        }
        return valid=true;
    }

    public boolean isValid(){
        return valid;
    }

    public String toString(){
        if(!this.isValid()){
            return "Empty instructor";
        }
        return "-------- Instructor Details -------\n"+
                "ID: "+this.deptID+
                "\nName: "+this.name+
                "\nPhone: "+this.phone+
                "\nDepartment: "+this.deptName+
                "\n--------------------------------";
    }

    public boolean delete() {
        if(!this.isValid()) {
            System.out.println("Fill department first!!");
            return false;
        }

        String query = "DELETE FROM instructors where instructorID = "+this.instructorID +"";
        valid=!Main.sql.executeSimpleQuery(query);
        if(!valid) {
            System.out.println("Successfully deleted the record.");
            this.setEmpty();
        }
        return valid;
    }

    private void setEmpty(){
        this.deptID = null;
        this.name = null;
        this.phone = null;
        this.instructorID = null;
        this.valid = false;
        this.deptName = null;
    }

    public boolean update(){
        if(!this.isValid()) {
            System.out.println("Fill department first!!");
            return false;
        }

        String query = "UPDATE instructors SET "+
                "name = '"+this.name+"',"+
                "phone = '"+this.phone+"',"+
                "deptID = "+this.deptID+" WHERE instructorID = "+this.instructorID +"";
        System.out.println(query);
        return valid=Main.sql.executeSimpleQuery(query);
    }

    /* to view the list of students being taught by this instructor */
    public int viewStudents(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of students being taught by "+this.name+"("+this.instructorID+")\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT rollNo FROM enrollments where enrollments.courseID  IN " +
                "(SELECT courseID from duties where instructorID ='"+this.instructorID+"')";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            while(results.next()){
                Student student = new Student(results.getString("rollNo"));
                if(!compact)
                    System.out.println(student);
                count++;
            }
            System.out.println("Found "+count+" students\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        } catch (SQLException e) {
            System.out.println("Error executing the query " + query);
            return count;
        }
        return count;
    }

    /* to view the list of subjects the instructor is teaching*/
    public int viewCourses(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of courses "+this.name+"("+this.instructorID+") is teaching\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT courseID FROM duties where instructorID = '"+this.instructorID+"'";
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
