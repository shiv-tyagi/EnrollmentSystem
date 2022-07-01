import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
    String rollNo;
    String name;
    String dob;
    String year_of_admission;
    String programme;
    String branch;
    String email;
    String phone;
    int totalCredits;
    boolean valid;
    public Student () {
        this.setEmpty();
    }

    public Student (String rollNo){
        this.fillByRollNo(rollNo);
    }

    public boolean pushToDB(){
        if(!this.isValid()) {
            return false;
        }

        String query = "INSERT INTO STUDENTS" +
                "(rollNo , name, dob, year_of_admission, programme, branch, email, phone) VALUES (" +
                "'" + rollNo + "'," +
                "'" + name + "'," +
                "'" + dob + "'," +
                year_of_admission + "," +
                "'" + programme + "'," +
                "'" + branch + "'," +
                "'" + email + "'," +
                "'" + phone + "')";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    public boolean fillByRollNo(String rollNo){
        String query = "SELECT * FROM students where rollNo = '"+rollNo+"'";
        ResultSet results = null;
        try {
            results = Main.sql.connection.createStatement().executeQuery(query);
            if(!results.next()) {
                System.out.println("Student with Roll No. "+rollNo+" not found ");
                return false;
            }
            System.out.println("Found a student with roll no.="+rollNo);
            this.rollNo=rollNo;
            this.name = results.getString("name");
            this.dob = results.getString("dob");
            this.year_of_admission = results.getString("year_of_admission");
            this.programme = results.getString("programme");
            this.branch = results.getString("branch");
            this.email = results.getString("email");
            this.phone = results.getString("phone");
            this.totalCredits = results.getInt("totalCredits");
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
            return "Empty student";
        }
        return "-------- Student Details --------\n"+
                "Roll No: "+this.rollNo+
                "\nName: "+this.name+
                "\nDOB: "+this.fixDOBFormat()+
                "\nYear of admission: "+this.year_of_admission+
                "\nProgramme: "+this.programme+
                "\nBranch: "+this.branch+
                "\nEmail: "+this.email+
                "\nPhone: "+this.phone+
                "\nTotal Credits: "+this.totalCredits+
                "\n---------------------------------";
    }

    public String fixDOBFormat() {
        if(this.dob == null){
            return null;
        }
        String tokens[] = dob.split("-");
        return tokens[2]+"/"+tokens[1]+"/"+tokens[0];
    }

    public boolean delete() {
        if(!this.isValid()) {
            System.out.println("Fill student first!!");
            return false;
        }

        String query = "DELETE FROM students where rollNo = '"+this.rollNo+"'";
        valid=!Main.sql.executeSimpleQuery(query);
        if(!valid) {
            System.out.println("Successfully deleted the record.");
            this.setEmpty();
        }
        return valid;
    }

    private void setEmpty(){
        this.rollNo = null;
        this.name = null;
        this.dob = null;
        this.year_of_admission = null;
        this.programme = null;
        this.branch = null;
        this.email = null;
        this.phone = null;
        this.totalCredits = 0;
        this.valid = false;
    }

    public boolean update(){
        if(!this.isValid()) {
            System.out.println("Fill student first!!");
            return false;
        }

        String query = "UPDATE students SET "+
                "name = '"+this.name+"',"+
                "dob = '"+this.dob+"',"+
                "year_of_admission = '"+this.year_of_admission+"',"+
                "programme = '"+this.programme+"',"+
                "branch = '"+this.branch+"',"+
                "email = '"+this.email+"',"+
                "phone = '"+this.phone+"' WHERE rollNo = '"+this.rollNo+"'";
        return valid=Main.sql.executeSimpleQuery(query);
    }

    /* to view the list of subjects the student is enrolled in */
    public int viewCourses(){
        boolean compact = Main.wantCompact();
        int count = 0;
        System.out.println("List of courses "+this.name+"("+this.rollNo+") is enrolled in\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT courseID FROM enrollments where rollNo = '"+rollNo+"'";
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
