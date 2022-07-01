import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
    String courseID;
    String name;
    int credits;
    String deptID;
    boolean valid;

    public Course () {
        this.setEmpty();
    }

    public Course (String courseID) {
        this.fillByID(courseID);
    }

    public void setEmpty(){
        this.courseID = null;
        this.name = null;
        this.deptID = null;
        this.credits = 0;
        this.valid = false;
    }

    public boolean fillByID(String courseID){
        String query = "SELECT * FROM courses where courseID = '"+courseID+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                System.out.println("Course with ID "+courseID+" not found ");
                return false;
            }
            System.out.println("Found a course with ID="+courseID);
            this.courseID=courseID;
            this.name = results.getString("name");
            this.deptID = results.getString("deptID");
            this.credits = results.getInt("credits");
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

        String query = "INSERT INTO courses" +
                "(courseID , name, deptID, credits) VALUES (" +
                "" + courseID + "," +
                "'" + name + "'," +
                "'" + deptID + "'," +
                "'" + credits + "')";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public boolean isValid(){
        return valid;
    }

    public boolean delete() {
        if(!this.isValid()) {
            System.out.println("Fill course object first!!");
            return false;
        }

        String query = "DELETE FROM courses where courseID = '"+this.courseID+"'";
        valid=!Main.sql.executeSimpleQuery(query);
        if(!valid) {
            System.out.println("Successfully deleted the record.");
            this.setEmpty();
        }
        return valid;
    }

    public boolean update(){
        if(!this.isValid()) {
            System.out.println("Fill course object first!!");
            return false;
        }

        String query = "UPDATE courses SET "+
                "name = '"+this.name+"',"+
                "credits = '"+this.credits+"',"+
                "deptID = '"+this.deptID+"' WHERE courseID = '"+this.courseID+"'";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public String toString(){
        if(!this.isValid()){
            return "Empty course";
        }
        return "-------- Course Details --------\n"+
                "ID: "+this.courseID+
                "\nCourse Name: "+this.name+
                "\nCredits: "+this.credits+
                "\nDepartment ID: "+this.deptID+
                "\n---------------------------------";
    }

    /* to view the list of instructors teaching a course */
    public int viewInstructors(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of instructors teaching "+this.name+"("+this.courseID+")\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT instructorID FROM duties where courseID = '"+courseID+"'";
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

    /* to view the list of s the student is enrolled in the course*/
    public int viewStudents(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of students enrolled in "+this.name+"("+this.courseID+")\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT rollNo FROM enrollments where courseID = '"+courseID+"'";
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
}
