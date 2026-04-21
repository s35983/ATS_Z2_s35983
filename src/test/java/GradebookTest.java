import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;// zamina z BeforeAll
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GradebookTest {
    // Poprawa: Usunięcie modyfikatora static tam gdzie niepotrzebny. Obiekt jest teraz instancyjny,co zapobiega współdzieleniu stanu między testami.
    private Gradebook gradebook;

    @BeforeEach // Poprawa: zmiana z BeforeAll, każdy test ma swój własny, świeży Gradebook
    public void setUp() {
        gradebook = new Gradebook();
    }

    @Test
    public void testAddSubject() {
        gradebook.addSubject("Physics");
        List<String> expectedList = List.of("Physics");
        assertEquals(expectedList, gradebook.getSubjects());
    }

    @Test
    public void testAddGradeToSubject() {
        gradebook.addSubject("Math");
        gradebook.addGrade("Math", 5.0);
        Map<String, List<Double>> expectedMap = new HashMap<>();
        expectedMap.put("Math", List.of(5.0));
        assertEquals(expectedMap, gradebook.getGrades());
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForSubjects() {
        // Przygotowanie danych, dynamiczne testy dla przedmiotów
        List<String> testSubjects = List.of("Math", "Chemistry", "Biology", "Physics", "History");
        testSubjects.forEach(gradebook::addSubject);

        return testSubjects.stream().map(subject ->
                DynamicTest.dynamicTest("Test for subject: " + subject, () -> {
                    // 1. Dodanie ocen
                    gradebook.addGrade(subject, 4.0);
                    gradebook.addGrade(subject, 6.0);

                    // 2. Sprawdzenie czy oceny zostały dodane poprawnie
                    assertEquals(2, gradebook.getGrades().get(subject).size(),
                            " Test amount of grades for subject: %s".formatted(subject));

                    // 3. Sprawdzenie średniej czy poprawnie policzona  ( (4.0 + 6.0) / 2 = 5.0 )
                    double expectedAverage = 5.0;
                    double actualAverage = gradebook.calcAvgForSubject(subject);
                    assertEquals(expectedAverage, actualAverage, 0.001,
                            "Grade average test for subject: %s".formatted(subject));
                })
        );
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForTotalAverage() {
        return Stream.of(
                new Object[]{List.of(4.0, 2.0, 5.0), 3.67},
                new Object[]{List.of(3.0, 3.0, 3.0), 3.0}
        ).map(data ->
                DynamicTest.dynamicTest("Average test: " + data[1], () -> {

                    Gradebook gb = new Gradebook();
                    gb.addSubject("Test");

                    for (double g : (List<Double>) data[0]) {
                        gb.addGrade("Test", g);
                    }

                    double result = gb.calcAvgForAllSubjects();
                    assertEquals((double) data[1], result, 0.01);
                })
        );}
    //Testy DODATKOWE
    @Test
    public void testAddGradeToNonExistingSubject() {
        assertThrows(IllegalArgumentException.class, () -> {
            gradebook.addGrade("Unknown", 5.0);
        });
    }

    @Test
    public void testCalcAvgForEmptySubject() {
        gradebook.addSubject("Math");

        assertThrows(IllegalArgumentException.class, () -> {
            gradebook.calcAvgForSubject("Math");
        });
    }

    @Test
    public void testCalcAvgForAllSubjectsNoGrades() {
        gradebook.addSubject("Math");

        assertThrows(IllegalArgumentException.class, () -> {
            gradebook.calcAvgForAllSubjects();
        });
    }
}