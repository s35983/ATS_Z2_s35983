import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class GradebookTest {

    // Poprawa: Usunięcie modyfikatora static tam gdzie niepotrzebny.
    // Obiekt jest teraz instancyjny, co zapobiega współdzieleniu stanu między testami.
    private Gradebook gradebook;

    private static final double FIVE = 5.0;
    private static final double FOUR = 4.0;
    private static final double SIX = 6.0;
    private static final double DELTA = 0.001;

    @BeforeEach
    public void setUp() {
        gradebook = new Gradebook();
    }

    @Test
    public void testAddSubject() {
        gradebook.addSubject("Physics");
        final List<String> expectedList = List.of("Physics");
        assertEquals(expectedList, gradebook.getSubjects());
    }

    @Test
    public void testAddGradeToSubject() {
        gradebook.addSubject("Math");
        gradebook.addGrade("Math", FIVE);

        final Map<String, List<Double>> expectedMap = new HashMap<>();
        expectedMap.put("Math", List.of(FIVE));

        assertEquals(expectedMap, gradebook.getGrades());
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForSubjects() {

        final List<String> testSubjects = List.of(
                "Math", "Chemistry", "Biology", "Physics", "History"
        );

        testSubjects.forEach(gradebook::addSubject);

        return testSubjects.stream().map(subject ->
                DynamicTest.dynamicTest(
                        "Test for subject: " + subject,
                        () -> {
                            gradebook.addGrade(subject, FOUR);
                            gradebook.addGrade(subject, SIX);

                            assertEquals(
                                    2,
                                    gradebook.getGrades().get(subject).size(),
                                    "Test amount of grades for subject: %s".formatted(subject)
                            );

                            final double expectedAverage = FIVE;
                            final double actualAverage =
                                    gradebook.calcAvgForSubject(subject);

                            assertEquals(
                                    expectedAverage,
                                    actualAverage,
                                    DELTA,
                                    "Grade average test for subject: %s"
                                            .formatted(subject)
                            );
                        }
                )
        );
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForTotalAverage() {

        return Stream.of(
                new Object[]{List.of(FOUR, 2.0, FIVE), 3.67},
                new Object[]{List.of(3.0, 3.0, 3.0), 3.0}
        ).map(data ->
                DynamicTest.dynamicTest(
                        "Average test: " + data[1],
                        () -> {

                            final Gradebook gb = new Gradebook();
                            gb.addSubject("Test");

                            for (double g : (List<Double>) data[0]) {
                                gb.addGrade("Test", g);
                            }

                            final double result = gb.calcAvgForAllSubjects();

                            assertEquals((double) data[1], result, 0.01);
                        }
                )
        );
    }

    // Testy DODATKOWE

    @Test
    public void testAddGradeToNonExistingSubject() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gradebook.addGrade("Unknown", FIVE)
        );
    }

    @Test
    public void testCalcAvgForEmptySubject() {
        gradebook.addSubject("Math");

        assertThrows(
                IllegalArgumentException.class,
                () -> gradebook.calcAvgForSubject("Math")
        );
    }

    @Test
    public void testCalcAvgForAllSubjectsNoGrades() {
        gradebook.addSubject("Math");

        assertThrows(
                IllegalArgumentException.class,
                () -> gradebook.calcAvgForAllSubjects()
        );
    }
}