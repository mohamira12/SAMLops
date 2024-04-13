package com.SAMLops.projetDevOps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
// import java.util.Arrays;
// import java.util.List;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataFrameTest extends TestCase {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public DataFrameTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(DataFrameTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setOut(new PrintStream(outContent));
    }

    @Override
    protected void tearDown() throws Exception {
        System.setOut(originalOut);
        super.tearDown();
    }

    public void testDetermineColumnTypes() {
        List<String> columns = Arrays.asList("Name", "Age", "Salary", "PartTime");
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("Alice", 30, 2500.0, true),
                Arrays.asList("Bob", 29, 3200, false), // Mixed integer and double for Salary
                Arrays.asList("Charlie", 25, 2800.0, false));

        DataFrame df = new DataFrame(columns, data);

        // Assuming getColumnTypes is a public method you've added for testing
        List<Class<?>> columnTypes = df.getColumnTypes();

        assertEquals(String.class, columnTypes.get(0)); // Name column type
        assertEquals(Integer.class, columnTypes.get(1)); // Age column type
        assertEquals(Double.class, columnTypes.get(2)); // Salary column type, expecting Double because of mix
        assertEquals(Boolean.class, columnTypes.get(3)); // PartTime column type
    }

    public void testDisplay() {
        List<String> columns = Arrays.asList("Name", "Age", "Travail", "Salaire");
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("Alice", 30, "caissiere", 1800),
                Arrays.asList("Bob", 29, "comptable", 2500),
                Arrays.asList("Charlie", 25, "developpeur", 3000));

        DataFrame df = new DataFrame(columns, data);
        df.display();

        String expectedOutput = "Name\tAge\tTravail\tSalaire\t\nAlice\t30\tcaissiere\t1800\t\nBob\t29\tcomptable\t2500\t\nCharlie\t25\tdeveloppeur\t3000\t\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    public void testDisplayFirstRows() {
        List<String> columns = Arrays.asList("Name", "Age", "Score");
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("Alice", 30, 85.5),
                Arrays.asList("Bob", 22, 89.2),
                Arrays.asList("Charlie", 25, 92),
                Arrays.asList("Diana", 28, 88.8));

        DataFrame df = new DataFrame(columns, data);

        // L'affichage des 2 premières lignes
        df.displayFirstRows(2);

        String expectedOutput = "Name\tAge\tScore\t\nAlice\t30\t85.5\t\nBob\t22\t89.2\t\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());

        // Nettoyage pour le prochain test
        outContent.reset();

        // L'affichage avec un nombre de lignes demandé supérieur au nombre de lignes
        // disponibles
        df.displayFirstRows(5);

        expectedOutput = "Name\tAge\tScore\t\nAlice\t30\t85.5\t\nBob\t22\t89.2\t\nCharlie\t25\t92\t\nDiana\t28\t88.8\t\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }

    public void testDisplayLastRows() {
        List<String> columns = Arrays.asList("Name", "Age", "Score");
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("Alice", 30, 85.5),
                Arrays.asList("Bob", 22, 89.2),
                Arrays.asList("Charlie", 25, 92),
                Arrays.asList("Diana", 28, 88.8));

        DataFrame df = new DataFrame(columns, data);

        // Affichage des 2 dernières lignes
        df.displayLastRows(2);

        String expectedOutput = "Name\tAge\tScore\t\nCharlie\t25\t92\t\nDiana\t28\t88.8\t\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());

        // Nettoyage pour le prochain test
        outContent.reset();

        // Affichage des dernières lignes avec un nombre de lignes demandé supérieur au
        // nombre de lignes disponibles
        df.displayLastRows(5);

        expectedOutput = "Name\tAge\tScore\t\nAlice\t30\t85.5\t\nBob\t22\t89.2\t\nCharlie\t25\t92\t\nDiana\t28\t88.8\t\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
    }



    public void testSelectRowsByIndices() {
        List<String> columns = Arrays.asList("Name", "Age", "Score");
        List<List<Object>> data = Arrays.asList(
                Arrays.asList("Alice", 30, 85.5),
                Arrays.asList("Bob", 22, 89.2),
                Arrays.asList("Charlie", 25, 92));
    
        DataFrame df = new DataFrame(columns, data);
        DataFrame selectedDf = df.selectRowsByIndices(Arrays.asList(0, 2)); // Selecting rows for Alice and Charlie
    
        List<List<Object>> expectedData = Arrays.asList(
                Arrays.asList("Alice", 30, 85.5),
                Arrays.asList("Charlie", 25, 92));
    
        DataFrame expectedDf = new DataFrame(columns, expectedData);
    
        assertEquals(expectedDf.getData(), selectedDf.getData());
    }
    
    public void testCSVConstructor() throws IOException {
        String path = "src/test/resources/csvFile.csv"; // Chemin vers un
        // fichier CSV
        // fictif pour le test
        DataFrame df = new DataFrame(path);

        // Test de lecture des noms de colonnes
        List<String> expectedColumns = Arrays.asList("Nom", "Age", "Ville");
        assertEquals(expectedColumns, df.getColumns());

        // Test des types de colonnes déduits
        List<Class<?>> expectedTypes = Arrays.asList(String.class, Integer.class, String.class);
        assertEquals(expectedTypes, df.getColumnTypes());

        // Test de conversion des données
        List<List<Object>> expectedData = Arrays.asList(
                Arrays.asList("John Doe", 30, "New York"),
                Arrays.asList("Jane Smith", 25, "London"),
                Arrays.asList("Alice Johnson", 35, "Paris"),
                Arrays.asList("Bob Anderson", 40, "Tokyo")); // Remove space after 'Anderson'
        assertEquals(expectedData, df.getData());
    }

}
