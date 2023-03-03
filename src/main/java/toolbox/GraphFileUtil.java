package toolbox;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import java.io.File;
import java.io.IOException;

public class GraphFileUtil {

    public static void toFile(Graph g, String filePath) {
        File file = new File(filePath);
        if (!file.exists() && !file.getParentFile().mkdirs()) {
            try {
                // This exception will be thrown if a part filePath of the path already exists
                throw new IOException("Unable to create " + file.getParentFile() + ". This may be, because part of the path already existed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean directed = false;
        if (g.getEdgeCount() > 0) {
            directed = g.edges().anyMatch(Edge::isDirected);
        }
        FileSinkDOT fs = new FileSinkDOT();

        try {
            fs.setDirected(directed);
            fs.writeAll(g, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Graph fromFile(String filePath) {

        Graph g = new SingleGraph("g");
        FileSource fs = null;
        try {
            fs = FileSourceFactory.sourceFor(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fs.removeSink(g);
        }
        fs.addSink(g);
        try {
            fs.readAll(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
