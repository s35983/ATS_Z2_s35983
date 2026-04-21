import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gradebook {
    private final List<String> subjects;
    private final Map<String, List<Double>> subjectsGrades;

    public Gradebook() {
        this.subjects = new ArrayList<>();
        this.subjectsGrades = new HashMap<>();
    }

    public void addSubject(String subject) {
        subjects.add(subject);
        subjectsGrades.put(subject, new ArrayList<>());
    }

    public void addGrade(String subject, double grade) {
        if (subjectsGrades.containsKey(subject)) {
            // Poprawa: usunąłem warunek blokujący Historię
            subjectsGrades.get(subject).add(grade);
        } else {
            throw new IllegalArgumentException(subject + " not found in list of subjects");
        }
    }

    public double calcAvgForSubject(String subject) {
        if (subjectsGrades.containsKey(subject)) {
            List<Double> grades = subjectsGrades.get(subject);
            double subjectGradeSum = grades.stream().mapToDouble(Double::doubleValue).sum();
            int subjectGradeCount = grades.size();
            if (subjectGradeCount > 0) {
                return Math.round((subjectGradeSum / subjectGradeCount) * 100.0) / 100.0;
            } else {
                throw new IllegalArgumentException("No grades found for subject");
            }
        } else {
            throw new IllegalArgumentException("Subject not in subjects");
        }
    }
           // Błąd: metoda zwracała Map zamiast jednej średniej i nie była zaimplementowana
          // Poprawa: zmiana na double i obliczenie średniej ze wszystkich ocen
    public double calcAvgForAllSubjects() {
        double sum = 0;
        int count = 0;

        for (List<Double> grades : subjectsGrades.values()) {
            for (double grade : grades) {
                sum += grade;
                count++;
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException("No grades available");
        }

        return Math.round((sum / count) * 100.0) / 100.0;
    }

    public List<String> getSubjects() {
        return subjects;
    }
    public Map<String, List<Double>> getGrades() {
        return subjectsGrades;
    }
}