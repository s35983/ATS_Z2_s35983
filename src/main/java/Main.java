public final class Main {
    private Main() {}
    public static void main(final String[] args) {
        System.out.println("Gradebook app running...");

        final Gradebook gradebook = new Gradebook();
        gradebook.addSubject("Math");
        gradebook.addGrade("Math", 5.0);

        final double avg = gradebook.calcAvgForAllSubjects();
        System.out.println("Avg: " + avg);
    }
}