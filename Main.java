package Assignment2;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	public static void main(String args[]) throws Exception {
		Transcript transcript = new Transcript("location/path of input.txt", "location/path of wronginput.txt");
		ArrayList<Student> s = transcript.buildStudentArray();
		transcript.printTranscript(s);
	}
}
