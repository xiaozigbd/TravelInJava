package patternPractice.core;

public class Student {

   private Integer rollNo;
   private String name;

   public Student() {
   }

   public Student(String name, Integer rollNo) {
      this.rollNo = rollNo;
      this.name = name;
   }

   public Integer getRollNo() {
      return rollNo;
   }
   public void setRollNo(Integer rollNo) {
      this.rollNo = rollNo;
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
}