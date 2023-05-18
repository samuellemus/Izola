package mycompany.app;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



import com.google.gson.Gson;


class Student {

    private long studentId;
    private String studentName;
    private String[] studentClasses;

    public Student (long studentId, String studentName) {
        super();
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Student (long studentId, String studentName, String[] studentClasses) {
        super();
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentClasses = studentClasses;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String[] getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(String[] studentClasses) {
        this.studentClasses = studentClasses;
    }
}

class MealObject {
    private String mealName;
    private String[] mealIngredients;

}

public class CustomJsonObject {
    public static void main (String [] args) throws FileNotFoundException, IOException {

        List < Student >  students = new ArrayList <> ();

        Student student1 = new Student(100, "student1");
        Student student2 = new Student(200, "student2");
        Student student3 = new Student(300, "student3");
        Student student4 = new Student(400, "student4");
        Student student5 = new Student(500, "student5");
        String[] classes = {"math", "physics", "chemistry", "history", "language"};
        Student student6 = new Student(600, "student6", classes);

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);

        Gson gson = new Gson();
        String json = gson.toJson(students);
        json = json.replace("{", "{\n");
        json = json.replace("}", "\n}");
        System.out.println(json);
    }
}
