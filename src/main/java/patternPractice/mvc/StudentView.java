package patternPractice.mvc;

public class StudentView {
   public void printStudentDetails(String studentName, Integer studentRollNo){
      System.out.println("Student: ");
      System.out.println("Name: " + studentName);
      System.out.println("Roll No: " + studentRollNo);
   }
}