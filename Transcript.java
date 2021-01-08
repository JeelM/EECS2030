package Assignment2;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

/**
 * This class generates a transcript for each student, whose information is in
 * the text file.
 * 
 *
 */

public class Transcript {
	private ArrayList<Object> grade = new ArrayList<Object>();
	private File inputFile;
	private String outputFile;

	/**
	 * This the the constructor for Transcript class that initializes its instance
	 * variables and call readFie private method to read the file and construct
	 * this.grade.
	 * 
	 * @param inFile  is the name of the input file.
	 * @param outFile is the name of the output file.
	 */
	public Transcript(String inFile, String outFile) {
		inputFile = new File(inFile);
		outputFile = outFile;
		grade = new ArrayList<Object>();
		this.readFile();
	}// end of Transcript constructor

	/**
	 * This method reads a text file and add each line as an entry of grade
	 * ArrayList.
	 * 
	 * @exception It throws FileNotFoundException if the file is not found.
	 */
	private void readFile() {
		Scanner sc = null;
		try {
			sc = new Scanner(inputFile);
			while (sc.hasNextLine()) {
				grade.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	} // end of readFile

	/**
	 * This method creates and returns an ArrayList, whose element is an object of
	 * class Student. The object at each element is created by aggregating ALL the
	 * information, that is found for one student in the grade Arraylist of class
	 * Transcript.
	 * 
	 * @return students will throw an InvalidTotalException if sum of elements of
	 *         gradeList > 100 or sum of weightList != 100
	 * @throws Exception
	 */
	public ArrayList<Student> buildStudentArray() throws Exception {
		ArrayList<Student> students = new ArrayList<>();

		for (int i = 0; i < grade.size(); i++) {

			ArrayList<String> tokens = new ArrayList<>();
			String line = grade.get(i).toString();
			StringTokenizer st = new StringTokenizer(line, ",");
			while (st.hasMoreTokens()) {
				String t = st.nextToken();
				tokens.add(t);
			}
			if (tokens.isEmpty()) {
				return students;
			}
			String studentID = tokens.get(2);
			String name = tokens.get(tokens.size() - 1);
			Student student = findStudent(studentID, students);
			if (student == null) {
				student = new Student(studentID, name, new ArrayList<Course>());
				students.add(student);
			}
			String courseCode = tokens.get(0);
			double courseCredit = Double.parseDouble(tokens.get(1));
			ArrayList<Assessment> a = new ArrayList<>();
			Course courseTaken = new Course(courseCode, a, courseCredit);

			ArrayList<Integer> weightList = new ArrayList<>();
			ArrayList<Double> gradesList = new ArrayList<>();
			for (int j = 3; j < tokens.size() - 1; j++) {
				String token = tokens.get(j);
				char type = token.charAt(0);
				Integer weight = Integer.parseInt(token.substring(1, token.indexOf("(")));
				Double grades = Double.parseDouble(token.substring(token.indexOf("(") + 1, token.indexOf(")")));
				weightList.add(weight);
				gradesList.add(grades);
				a.add(Assessment.getInstance(type, weight));
			}
			student.addGrade(gradesList, weightList);
			student.addCourse(courseTaken);

		}

		return students;
	}

	/**
	 * This is a private helper method that helps to find whether or not a student
	 * is in the list or not
	 * 
	 * @param studentID the student ID that we get from the input.txt file
	 * @param studentList       arraylist of students returns the student if id is found in
	 *                  the arraylist studentList, and null otherwise(new student id that
	 *                  needs to be added to list)
	 * @return		returns the student if the ID is found and null otherwise.
	 */
	private Student findStudent(String studentID, ArrayList<Student> studentList) {
		for (Student student : studentList) {
			if (student.getStudentID().equals(studentID))
				return student;
		}
		return null;
	}

	/**
	 * This is the method that prints the transcript to the given file
	 * 
	 * @param students
	 */
	public void printTranscript(ArrayList<Student> students) {
		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			System.out.println(student.getName() + "\t" + student.getStudentID());
			System.out.println("--------------------");
			ArrayList<Course> courses = student.getCourseTaken();
			ArrayList<Double> grades = student.getFinalGrade();
			// System.out.println(courses.get(1).getCode());
			for (int j = 0; j < courses.size(); j++) {
				System.out.println(courses.get(j).getCode() + "\t" + grades.get(j));
			}
			System.out.println("--------------------");
			System.out.println("GPA: " + student.weightedGPA());
			System.out.println();

		}
	}

} // end of Transcript

//<------------------------------------------------------------------------------------>

//start of Student class
class Student {
	/**
	 * Student ID and Name
	 */
	private String studentID;
	private String name;

	/**
	 * First arrayllist:Creates an empty arraylist of type Course to store the
	 * courses taken second arraylist:Creates an empty arraylist of type Double to
	 * store the final grades
	 * <p>
	 * courseTaken and finalGrades are parallel arrays. Two (or more) arrays are
	 * called parallel if A) they have the same number of elements, B) items in the
	 * same indexes are related. In our case, finalGrade at index i, shows the grade
	 * that the student received for the courseTaken at index i.
	 */
	private ArrayList<Course> courseTaken = new ArrayList<>();
	private ArrayList<Double> finalGrade = new ArrayList<>();

	public Student() {
	}

	/**
	 * Initializes the students details like studentId, name and the courses taken
	 * 
	 * @param studentID student id number
	 * @param name      name of the student
	 * @param courses   an arraylist of courses that the student has.
	 */
	public Student(String studentID, String name, ArrayList<Course> courses) {
		this.studentID = studentID;
		this.name = name;
		this.courseTaken = courses;
	}

	/**
	 * returns the student ID number
	 * 
	 * @return student ID
	 */

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * returns the name of the student
	 * 
	 * @return the name of student
	 */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * returns the arraylist of the courses taken by student
	 * 
	 * @return list of courses taken
	 */
	public ArrayList<Course> getCourseTaken() {
		return courseTaken;
	}

	public void setCourseTaken(ArrayList<Course> courseTaken) {
		this.courseTaken = courseTaken;
	}

	/**
	 * returns the arraylist of the final grades achieved in courses
	 * 
	 * @return list of final grades
	 */
	
	public ArrayList<Double> getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(ArrayList<Double> finalGrade) {
		this.finalGrade = finalGrade;
	}

	/**
	 * This method gets an array list of the grades and their weights, computes the
	 * true value of the grade based on its weight and add it to finalGrade
	 * attribute. In case the sum of the weight was not 100, or the sum of grade was
	 * greater 100, it throws InvalidTotalException.
	 * 
	 * @param grade     the grades received by student
	 * @param weightage the weightage of the assessments of the courses
	 * @throws Exception throws InvalidTotalException (defined by us)
	 */
	public void addGrade(ArrayList<Double> grade, ArrayList<Integer> weightage) throws Exception {
		int weightSum = 0;

		for (Integer i : weightage) {
			weightSum += i;
		}

		if (weightSum != 100) {
			throw new InvalidTotalException("Invlaid Weightage Total");
		}

		double calc = 0.0;
		for (int i = 0; i < grade.size(); i++) {
			double g = grade.get(i);
			double w = weightage.get(i);
			calc += (g * w) / 100;
			weightSum += w;
		}
		calc = Math.floor(calc * 10) / 10;// rounding to one decimal place
		if (calc > 100) {
			throw new InvalidTotalException("Invalid Grade Total");
		}
		finalGrade.add(calc);
	}// end of addGrade

	/**
	 * Computes the GPA from the gradesPoints, courses credits and the grades
	 * obtained and returns the final computed GPA of a student.
	 * 
	 * @return weightedGPA of a student.
	 */
	public double weightedGPA() {
		double gradePoint = 0.0;
		double totalCredits = 0;

		for (int i = 0; i < finalGrade.size(); i++) {
			double g = finalGrade.get(i);
			double c = (courseTaken.get(i)).getCredit();
			totalCredits += c;
			if (g > 90) {
				gradePoint += 9 * c;
			} else if (g >= 80 && g < 90) {
				gradePoint += 8 * c;
			} else if (g >= 75 && g < 80) {
				gradePoint += 7 * c;
			} else if (g >= 70 && g < 75) {
				gradePoint += 6 * c;
			} else if (g >= 65 && g < 70) {
				gradePoint += 5 * c;
			} else if (g >= 60 && g < 65) {
				gradePoint += 4 * c;
			} else if (g >= 55 && g < 60) {
				gradePoint += 3 * c;
			} else if (g >= 50 && g < 55) {
				gradePoint += 2 * c;
			} else if (g >= 47 && g < 50) {
				gradePoint += 1 * c;
			} else {
				gradePoint += 0 * c;
			}
		}
		double gpa = gradePoint / totalCredits;
		return Math.floor(gpa * 10) / 10;
	}// end of weightedGPA

	/**
	 * Adds a course to the list of courses taken by the student
	 * 
	 * @param c
	 */
	public void addCourse(Course c) {
		this.courseTaken.add(c);
	}// end of addCourse

}// end of student class

//<---------------------------------------------------------------------------------------------------->

//start of Course
class Course extends Student {
	private String code;
	private ArrayList<Assessment> assignment = new ArrayList<>();
	private double credit;

	public Course() {

	}

	/**
	 * Initializes the course details like course code, the assessments and the
	 * course credit
	 * 
	 * @param courseCode   the course code
	 * @param a            the assessments of the course
	 * @param courseCredit number of credits of the course
	 */
	public Course(String courseCode, ArrayList<Assessment> a, double courseCredit) {
		this.code = courseCode;
		this.assignment = a;
		this.credit = courseCredit;
	}// end of course method

	
	/**
	 * Returns the course code
	 * 
	 * @return course code
	 */

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	/**
	 * Returns a list of assessments in the course
	 * 
	 * @return list of assessments
	 */
	public ArrayList<Assessment> getAssignment() {
		return assignment;
	}

	public void setAssignment(ArrayList<Assessment> assignment) {
		this.assignment = assignment;
	}

	/**
	 * Returns the number of credits of the course
	 * 
	 * @return the credits of the course
	 */

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public Course(Course other) {

	}// end of the method

	/**
	 * <p>
	 * This is an overridden method for Object’s equals() method that returns true,
	 * if all the instance variables of two objects have the same value.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.assignment, this.code, this.credit);

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (assignment == null) {
			if (other.assignment != null)
				return false;
		} else if (!assignment.equals(other.assignment))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (Double.doubleToLongBits(credit) != Double.doubleToLongBits(other.credit))
			return false;
		return true;
	}

}// end of Course class

//<----------------------------------------------------------------------------------------------------->

//start of Assessment class
class Assessment extends Course {

	/**
	 * The type of assessment
	 */
	private char type;

	/**
	 * the weightage of the assessment
	 */
	private int weight;

	private Assessment() {

	}// end of constructor

	/**
	 * Initialize the type of the assessment and its weightage
	 * 
	 * @param assessType type of assessment(practical or exam)
	 * @param weightage  the weightage of the assessment
	 */
	private Assessment(char assessType, int weightage) {
		this.type = assessType;
		this.weight = weightage;

	}
	
	/**
	 * Returns the type of the assessment
	 * 
	 * @return assessment type
	 */

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}
	
	/**
	 * returns the weight of the assessment
	 * 
	 * @return weight of assessment
	 */
	
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * This is a static factory method for class Assessment.
	 */
	public static Assessment getInstance(char type, int weight) {
		return new Assessment(type, weight);
	}// end of getInstance

	/**
	 * it is an overridden method for Object’s equals() method that returns true, if
	 * all the instance variables of two objects have the same value.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.type, this.weight);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assessment other = (Assessment) obj;
		if (type != other.type)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

}// end of Assessment class

//<----------------------------------------------------------------------------------------------------->
/**
 * This is the exception that is thrown if the total weight of the assessments
 * does not add up to 100, or of the total grade of a student is more than 100.
 */
class InvalidTotalException extends Exception {

	public InvalidTotalException(String message) {

	}
}
