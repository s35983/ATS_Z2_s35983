public class Main {
    public static void main(String[] args) {
        System.out.println("Gradebook app running...");

        Gradebook gb = new Gradebook();
        gb.addSubject("Math");
        gb.addGrade("Math", 5.0);

        double avg = gb.calcAvgForAllSubjects();
        System.out.println("Avg: " + avg);
    }
}