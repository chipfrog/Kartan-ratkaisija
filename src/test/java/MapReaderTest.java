import org.junit.Test;
import shortest_path_visualizer.IO.MapReader;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapReaderTest {
    File testMap = new File("src/test/resources/kartat/testikartta.txt");

    @Test
    public void matrixCanBeCreatedFromFile() throws FileNotFoundException {
        IOStub io = new IOStub();
        MapReader mapReader = new MapReader(io);
        mapReader.createMatrix(testMap);
        char[][] matrix = mapReader.getMapArray();
        assertTrue(matrix instanceof char[][]);

    }

    @Test
    public void createdMatrixHasCorrectNumberOfRowsAndColumns() throws FileNotFoundException {
        IOStub io = new IOStub();
        MapReader mapReader = new MapReader(io);
        mapReader.createMatrix(testMap);
        char[][] matrix = mapReader.getMapArray();
        assertEquals(matrix.length, 49);
        assertEquals(matrix[0].length, 49);
    }

    @Test
    public void matrixCanBePrinted() throws FileNotFoundException {
        IOStub io = new IOStub();
        MapReader mapReader = new MapReader(io);
        mapReader.createMatrix(testMap);
        mapReader.printMap();
        assertTrue(io.stringOutputs.contains("rows: " + 49));
    }


}
