import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main {
	public static Scanner scanner = new Scanner(System.in);
	public static SQLBackend sql = null;
	public static void main(String[] args) throws SQLException {
		setup();
		loop();
		//addStudent();
		//deleteStudentByRollNo();
		//System.out.println(findStudentByRollNo(null));
		//updateStudentByRollNo();
		//addDepartment();
		//deleteDepartmentByID();
		//updateDepartmentByID();
		//addInstructor();
		//updateInstructorByID();
		//deleteInstructorByID();
		//addCourse();
		//findCourseByID(null);
		//updateCourseByID();
		//deleteCourseByID();
		//addEnrollment();
		//System.out.println(findEnrollment(null, null));
		//findStudentByRollNo(null).viewCourses();
		//addDuty();
		//findCourseByID(null).viewStudents();
	}
	
	public static void setup() throws SQLException {
	// get sql driver. make sure to add mysql-connector to your project libraries
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// create a new sql backend. This automatically tries connection.
		sql = new SQLBackend();
		
		// check if the database exists. if not create one
		String database = new ServerCredentials().settings.get("database");
		if(!sql.checkDB(database)) {
			System.out.println("Don't worry! Trying to create one for you.");
			sql.createDatabase(database);
		} else {
			sql.setDatabase(database);
		}
		
		// check if the necessary tables exist. If not create them
		sql.checkTables();
		System.out.println("");
		
	}
	
	public static void loop() throws SQLException {
		// try login
		for(;;)
			mainMenu();
	}

	public static void mainMenu() {
		System.out.println("-- Main menu --");
		System.out.println("1. Instructor operations");
		System.out.println("2. Student operations");
		System.out.println("3. Department Operations");
		System.out.println("4. Course operations");
		System.out.println("5. Duty operations");
		System.out.println("6. Enrollment operations");
		System.out.println("7. Exit");
		System.out.print("Enter your choice (1-7):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
		case 1:
			instructorMenu();
			break;
		case 2:
			studentMenu();
			break;
		case 3:
			departmentMenu();
			break;
		case 4:
			courseMenu();
			break;
		case 5:
			dutyMenu();
			break;
		case 6:
			enrollmentMenu();
			break;
		case 7:
			System.out.println("Good bye!");
			System.exit(0);
		default:
			System.out.println("Invalid Choice! Try again.");
			break;
		}
	}

	public static void instructorMenu() {
		System.out.println("-- Instructor Menu --");
		System.out.println("1. Add Instructor");
		System.out.println("2. Update Instructor");
		System.out.println("3. Delete Instructor");
		System.out.println("4. View Instructor");
		System.out.println("5. List Instructors");
		System.out.println("6. View courses taught by an instructor");
		System.out.println("7. View students taught by an instructor");
		System.out.println("8. Back");
		System.out.print("Enter your choice (1-8):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
			case 1:
				addInstructor();
				break;
			case 2:
				updateInstructorByID();
				break;
			case 3:
				deleteInstructorByID();
				break;
			case 4:
				System.out.println(findInstructorByID(null));
				break;
			case 5:
				Main.listInstructors();
				break;
			case 6:
				Instructor instructor = Main.findInstructorByID(null);
				if(instructor!= null) {
					instructor.viewCourses();
				}
				break;
			case 7:
				Instructor ins = Main.findInstructorByID(null);
				if(ins!=null) {
					ins.viewStudents();
				}
				break;
			case 8:
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
				 instructorMenu();
		}
		mainMenu();
	}

	public  static void departmentMenu() {
		System.out.println("-- Department Menu --");
		System.out.println("1. Add Department");
		System.out.println("2. Update Department");
		System.out.println("3. Delete Department");
		System.out.println("4. View Department");
		System.out.println("5. List Departments");
		System.out.println("6. List instructors in a department");
		System.out.println("7. List courses offered by a department");
		System.out.println("8. Back");
		System.out.print("Enter your choice (1-8):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
			case 1:
				addDepartment();
				break;
			case 2:
				updateDepartmentByID();
				break;
			case 3:
				deleteDepartmentByID();
				break;
			case 4:
				System.out.println(findDeptByID(null));
				break;
			case 5:
				Main.listDepartments();
				break;
			case 6:
				Department dept = Main.findDeptByID(null);
				if(dept!=null) {
					dept.viewInstructors();
				}
				break;
			case 7:
				Department department = Main.findDeptByID(null);
				if(department!=null) {
					department.viewCourses();
				}
				break;
			case 8:
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
				departmentMenu();
		}
		mainMenu();
	}

	public  static void courseMenu() {
		System.out.println("-- Course Menu --");
		System.out.println("1. Add Course");
		System.out.println("2. Update Course");
		System.out.println("3. Delete Course");
		System.out.println("4. View Course");
		System.out.println("5. List Courses");
		System.out.println("6. Back");
		System.out.print("Enter your choice (1-6):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
			case 1:
				addCourse();
				break;
			case 2:
				updateCourseByID();
				break;
			case 3:
				deleteCourseByID();
				break;
			case 4:
				System.out.println(findCourseByID(null));
				break;
			case 5:
				Main.listCourses();
				break;
			case 6:
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
				courseMenu();
		}
		mainMenu();
	}

	public  static void dutyMenu() {
		System.out.println("-- Duty Menu --");
		System.out.println("1. Add duty");
		System.out.println("2. Delete duty");
		System.out.println("3. Back");
		System.out.print("Enter your choice (1-3):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
			case 1:
				addDuty();
				break;
			case 2:
				deleteDuty();
			case 3:
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
				dutyMenu();
				break;
		}
		mainMenu();
	}

	public  static void enrollmentMenu() {
		System.out.println("-- Enrollment Menu --");
		System.out.println("1. Add enrollment");
		System.out.println("2. Delete enrollment");
		System.out.println("3. Back");
		System.out.print("Enter your choice (1-3):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
			case 1:
				addEnrollment();
				break;
			case 2:
				deleteEnrollment();
			case 3:
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
				enrollmentMenu();
				break;
		}
		mainMenu();
	}

	public  static void studentMenu() {
		System.out.println("-- Student Menu --");
		System.out.println("1. Add Student");
		System.out.println("2. Update Student");
		System.out.println("3. Delete Student");
		System.out.println("4. View student");
		System.out.println(("5. List students"));
		System.out.println(("6. View subjects enrolled by a student"));
		System.out.println("7. Back");
		System.out.print("Enter your choice (1-7):");
		int choice = Integer.parseInt(scanner.nextLine());
		switch(choice) {
			case 1:
				addStudent();
				break;
			case 2:
				updateStudentByRollNo();
				break;
			case 3:
				deleteStudentByRollNo();
				break;
			case 4:
				System.out.println(findStudentByRollNo(null));
				break;
			case 5:
				Main.listStudents();
				break;
			case 6:
				Student student = Main.findStudentByRollNo(null);
				if(student!=null) {
					student.viewCourses();
				}
				break;
			case 7:
				break;
			default:
				System.out.println("Invalid Choice! Try again.");
				studentMenu();
		}
		mainMenu();
	}
	
	public static String readField(String fieldName, boolean isRequired) {
		String prompt = "Enter "+fieldName;
		if(isRequired) {
			prompt=prompt.concat(" (Required)");
		}
		prompt=prompt.concat(": ");
		System.out.print(prompt);
		String packet=null;
		while(true) {
			packet=scanner.nextLine();
			
			if(!isRequired) {
				break;
			}
			
			if(!packet.isBlank()) {
				break;
			}
			System.out.println(""+fieldName+" can't be left empty");
		}
		return packet;
	}
	
	public static String readDate(String fieldName, boolean isRequired) {
		String inputFormat = "dd/MM/yyyy";	// make sure to parse this date into required format
		fieldName = fieldName + "(" + inputFormat + ")";
		
		String date = null;
		while(true) {
			date = readField(fieldName, isRequired);
			// if date escapes readField checks, we simply return it
			if(date.isBlank()) {
				return date;
			}
			
			DateValidatorUsingDateFormat validator = new DateValidatorUsingDateFormat(inputFormat);
			if(validator.isValid(date)) {
				break;
			}
			System.out.println("Invalid date. Try again.");
		}
		// parsing date into correct format
		String tokens[] = date.split("/");
		return tokens[2]+"-"+tokens[1]+"-"+tokens[0];
	}
	
	public final static void clearConsole()
	{
	    try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
	
	public static boolean addStudent() {
		Student student = new Student();
		student.rollNo = Main.readField("Roll No.", true);
		// check if a student with same roll no exists
		while( findStudentByRollNo(student.rollNo) != null ) {
			System.out.println("Can't allow duplicate records.Try again.");
			student.rollNo = Main.readField("Roll No.", true);
		}
		System.out.println("Unique roll no. check passed. Go ahead to fill other details.");
		student.name = Main.readField("Name", true);
		student.dob = Main.readDate("Date of Birth", true);
		student.year_of_admission = Main.readField("Year of admission", true);
		student.programme = Main.readField("Programme", true);
		student.branch = Main.readField("Branch", true);
		student.email = Main.readField("Email", false);
		student.phone = Main.readField("Phone Number", false);
		student.valid = true;
		return student.pushToDB();
	}

	public static Student findStudentByRollNo(String rollNo) {
		if(rollNo == null)
			rollNo = Main.readField("Roll No.", true);
		Student student = new Student();
		return student.fillByRollNo(rollNo)?student:null;
	}

	public static boolean deleteStudentByRollNo() {
		Student student = findStudentByRollNo(null);
		if(student == null){
			return false;
		}
		return student.delete();
	}

	public static boolean updateStudentByRollNo() {
		Student student = findStudentByRollNo(null);
		if(student == null){
			return false;
		}

		// we never update the roll no
		student.name = wantUpdate("Name", student.name) ? readField("Name", true) : student.name;
		student.dob = wantUpdate("Date of Birth", student.dob) ? readDate("Date of Birth", true) : student.dob;
		student.year_of_admission = wantUpdate("Year of admission", student.year_of_admission) ? readField("Year of Admission", true) : student.year_of_admission;
		student.programme = wantUpdate("Programme", student.programme) ? readField("Programmer", true) : student.programme;
		student.branch = wantUpdate("Branch", student.branch) ? readField("Branch", true) : student.branch;
		student.email = wantUpdate("Email", student.email) ? readField("Email", true) : student.email;
		student.phone = wantUpdate("Phone", student.phone) ? readField("Phone", true) : student.phone;
		return student.update();
	}

	public static boolean wantUpdate(String fieldName, String oldValue) {
		System.out.println(fieldName+": "+oldValue);
		System.out.print("Do you want to update "+fieldName+"?(Y/N):");
		String want = Main.scanner.nextLine();
		while(!(want.equalsIgnoreCase("Y") || want.equalsIgnoreCase("N"))) {
			System.out.println("Invalid choice. Try again. Do you want to update "+fieldName+"?(Y/N):");
			want = Main.scanner.next();
		}

		// at this point we are sure that 'want' has either Y or N
		return want.equalsIgnoreCase("Y");
	}

	public static boolean addDepartment() {
		Department department = new Department();
		department.deptID = Main.readField("Department ID", true);
		// check if a student with same roll no exists
		while( findDeptByID(department.deptID) != null ) {
			System.out.println("Can't allow duplicate records.Try again.");
			department.deptID = Main.readField("Department ID", true);
		}

		department.name = Main.readField("Dept. Name", true);
		department.building = Main.readField("Building", true);
		department.valid = true;
		return department.pushToDB();
	}

	public static Department findDeptByID(String deptID) {
		if(deptID == null)
			deptID = Main.readField("Department ID", true);
		Department department = new Department();
		return department.fillByID(deptID)?department:null;
	}

	public static boolean deleteDepartmentByID() {
		Department department = findDeptByID(null);
		if(department == null){
			return false;
		}
		return department.delete();
	}

	public static boolean updateDepartmentByID() {
		Department department = findDeptByID(null);
		if(department == null){
			return false;
		}

		// we never update the deptID
		department.name = wantUpdate("Name", department.name) ? readField("Name", true) : department.name;
		department.building = wantUpdate("Building", department.building) ? readField("Building", true) : department.building;
		return department.update();
	}

	// instructor related operations
	public static boolean addInstructor() {
		Instructor instructor = new Instructor();
		instructor.instructorID = Main.readField(" Instructor ID", true);
		// check if a student with same roll no exists
		while( findInstructorByID(instructor.instructorID) != null ) {
			System.out.println("Can't allow duplicate records.Try again.");
			instructor.instructorID = Main.readField(" Instructor ID", true);
		}
		System.out.println("Unique instructor id check passed. Go ahead to fill other details.");
		instructor.name = Main.readField("Instructor Name", true);
		instructor.phone = Main.readField("Phone", false);
		instructor.deptID = Main.readField("Department ID", true);
		instructor.valid = true;
		return  instructor.pushToDB();
	}

	public static Instructor findInstructorByID(String instructorID) {
		if(instructorID == null)
			instructorID = Main.readField("Instructor ID", true);
		Instructor instructor = new Instructor();
		return instructor.fillByID(instructorID)?instructor:null;
	}

	public static boolean deleteInstructorByID() {
		Instructor instructor = findInstructorByID(null);
		if(instructor == null){
			return false;
		}
		return instructor.delete();
	}

	public static boolean updateInstructorByID() {
		Instructor instructor = findInstructorByID(null);
		if(instructor == null){
			return false;
		}

		// we never update the instructorID
		instructor.name = wantUpdate("Name", instructor.name) ? readField("Name", true) : instructor.name;
		instructor.phone = wantUpdate("Phone", instructor.phone) ? readField("Phone", true) : instructor.phone;
		instructor.deptID = wantUpdate("Department ID", instructor.deptID) ? readField("Department ID", true) : instructor.deptID;
		return instructor.update();
	}

	// course related operations
	public static boolean addCourse() {
		Course course = new Course();
		course.courseID = Main.readField("Course ID", true);
		// check if a student with same roll no exists
		while( findCourseByID(course.courseID) != null ) {
			System.out.println("Can't allow duplicate records.Try again.");
			course.courseID = Main.readField("Course ID", true);
		}

		course.name = Main.readField("Course Name", true);
		course.credits = Integer.parseInt(Main.readField("Credits", false));
		course.deptID = Main.readField("Department ID", true);
		course.valid = true;
		return  course.pushToDB();
	}

	public static Course findCourseByID(String courseID) {
		if(courseID == null)
			courseID = Main.readField("Course ID", true);
		Course course = new Course();
		return course.fillByID(courseID)?course:null;
	}

	public static boolean deleteCourseByID() {
		Course course = findCourseByID(null);
		if(course == null){
			return false;
		}
		return course.delete();
	}

	public static boolean updateCourseByID() {
		Course course = findCourseByID(null);
		if(course == null){
			return false;
		}

		// we never update the courseID
		course.name = wantUpdate("Course Name", course.name) ? readField("Course Name", true) : course.name;
		course.credits = wantUpdate("Credits", ""+course.credits) ? Integer.parseInt(readField("Credits", true)) : course.credits;
		course.deptID = wantUpdate("Department ID", course.deptID) ? readField("Department ID", true) : course.deptID;
		return course.update();
	}

	// enrollment related operations
	public static boolean addEnrollment() {
		Enrollment enrollment = new Enrollment();
		enrollment.rollNo = Main.readField("Roll No", true);
		enrollment.courseID = Main.readField("Course ID", true);
		// check if a student is already enrolled in this subject
		while( findEnrollment(enrollment.rollNo, enrollment.courseID) != null ) {
			System.out.println("Can't allow duplicate records.Try again.");
			enrollment.rollNo = Main.readField("Roll No", true);
			enrollment.courseID = Main.readField("Course ID", true);
		}

		System.out.println("Unique enrollment check passed. Pushing to Database!!");
		enrollment.valid = true;
		return  enrollment.pushToDB();
	}

	public static Enrollment findEnrollment(String rollNo, String courseID) {
		if(rollNo == null)
			rollNo = Main.readField("Roll No.", true);
		if(courseID == null)
			courseID = Main.readField("Course ID", true);

		Enrollment enrollment = new Enrollment();
		return enrollment.fill(rollNo, courseID)?enrollment:null;
	}

	public static boolean deleteEnrollment() {
		Enrollment enrollment = findEnrollment(null, null);
		if(enrollment == null){
			return false;
		}
		return enrollment.delete();
	}

	// duty related operations
	public static boolean addDuty() {
		Duty duty = new Duty();
		duty.instructorID = Main.readField("Instructor ID", true);
		duty.courseID = Main.readField("Course ID", true);
		// check if a student is already enrolled in this subject
		while( findDuty(duty.instructorID, duty.courseID) != null ) {
			System.out.println("Can't allow duplicate records.Try again.");
			duty.instructorID = Main.readField("Instructor ID", true);
			duty.courseID = Main.readField("Course ID", true);
		}
		System.out.println("Unique duty check passed. Pushing to Database!!");
		duty.valid = true;
		return  duty.pushToDB();
	}

	public static Duty findDuty(String instructorID, String courseID) {
		if(instructorID == null)
			instructorID = Main.readField("Instructor ID", true);
		if(courseID == null)
			courseID = Main.readField("Course ID", true);

		Duty duty = new Duty();
		return duty.fill(instructorID, courseID)?duty:null;
	}

	public static boolean deleteDuty() {
		Duty duty = findDuty(null, null);
		if(duty == null){
			return false;
		}
		return duty.delete();
	}

	public static boolean wantCompact() {
		System.out.print("Do you want compact results?(Y/N):");
		String want = Main.scanner.nextLine();
		while(!(want.equalsIgnoreCase("Y") || want.equalsIgnoreCase("N"))) {
			System.out.println("Invalid choice. Try again. Do you want compact results?(Y/N):");
			want = Main.scanner.nextLine();
		}

		// at this point we are sure that 'want' has either Y or N
		return want.equalsIgnoreCase("Y");
	}

	public static int listDepartments(){
		boolean compact = Main.wantCompact();
		int count = 0;
		System.out.println("List of departments in the institute\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		String query = "SELECT deptID FROM departments";
		ResultSet results = null;
		try {
			results = Main.sql.connection.createStatement().executeQuery(query);
			while(results.next()){
				Department dept = new Department(results.getString("deptID"));
				if(!compact)
					System.out.println(dept);
				count++;
			}
			System.out.println("Found "+count+" departments\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (SQLException e) {
			System.out.println("Error executing the query " + query);
			return count;
		}
		return count;
	}

	public static int listCourses(){
		boolean compact = Main.wantCompact();
		int count = 0;
		System.out.println("List of courses offered by the institute\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		String query = "SELECT courseID FROM courses";
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

	/* to view the list of instructors in the institute */
	public static int listInstructors(){
		boolean compact = Main.wantCompact();
		int count = 0;
		System.out.println("List of instructors in the institute\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		String query = "SELECT instructorID FROM instructors";
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

	/* to view the list of students in the institute */
	public static int listStudents(){
		boolean compact = Main.wantCompact();
		int count = 0;
		System.out.println("List of students in the institute\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		String query = "SELECT rollNo FROM students";
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
