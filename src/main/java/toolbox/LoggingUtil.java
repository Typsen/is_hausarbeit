package toolbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * <p>Utility class to create log files.</p>
 *
 * <p>How to use:</p>
 * <p>1.) Create a new LoggingUtil-Object. You can specify a path, filename and charset you want to use.
 * If not specified, a directory and file will be created at the specified STANDARD_PATH named after the specified
 * STANDARD_FILENAME using the specified STANDARD_CHARSET.</p>
 * <p>If not changed the file will be located at ./logs/log.txt using the UTF-8 charset.</p>
 *
 * <p>2.) Use the write()-Methods and append()-Methods (as well as their variations) to add new information to {@code file}.
 * Multiple LoggingUtil-Objects can write to the same {@code file}.</p>
 *
 * @author Sebastian Wewer
 * @version 0.6
 */
public class LoggingUtil {
    private final File file;
    private static final String STANDARD_PATH = "." + File.separatorChar + "logs";
    private final String path;
    private static final String STANDARD_FILENAME = "log.txt";
    private final String filename;
    private final String filePath;
    private static final Charset STANDARD_CHARSET = StandardCharsets.UTF_8;
    private final Charset charset;
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String STANDARD_SEPARATOR = "\t";
    private String separator;
    private boolean strictNewLine = false;

    /**
     * <p>Creates a new LoggingUtil object.</p>
     *
     * @param filename The name of the file.
     * @param path     The path were the file is located. Not including the name of the file.
     * @param charset  The charset to be used for the file.
     */
    public LoggingUtil(String filename, String path, Charset charset) {
        try {
            if (path == null || path.equals("")) {
                throw new IOException("Path can not be null or an empty String");
            }
            if (filename == null || filename.equals("")) {
                throw new IOException("Filname can not be null or an empty String.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.path = path;
        this.filename = filename;
        this.filePath = path + filename;
        this.charset = charset;
        this.file = new File(path + filename);
    }

    /**
     * <p>Creates a new LoggingUtil object using the {@link  #STANDARD_PATH}.</p>
     *
     * @param filename The name of the file.
     * @param charset  The charset to be used for the file.
     */
    public LoggingUtil(String filename, Charset charset) {
        this(filename, STANDARD_PATH, charset);
    }

    /**
     * <p>Creates a new LoggingUtil object using the {@link  #STANDARD_CHARSET}.</p>
     *
     * @param filename The name of the file.
     */
    public LoggingUtil(String filename, String path) {
        this(filename, path, STANDARD_CHARSET);
    }

    /**
     * <p>Creates a new LoggingUtil object using the {@link  #STANDARD_PATH} and {@link  #STANDARD_CHARSET}.</p>
     *
     * @param filename The name of the file.
     */
    public LoggingUtil(String filename) {
        this(filename, STANDARD_PATH);
    }

    /**
     * <p>Creates a new LoggingUtil object using the {@link  #STANDARD_PATH}, {@link  #STANDARD_FILENAME} and {@link  #STANDARD_CHARSET}.</p>
     */
    public LoggingUtil() {
        this(STANDARD_FILENAME);
    }

    /**
     * <p>Writes {@code input} at the beginning of {@code file}. This method will overwrite anything currently written in {@code file}!</p>
     *
     * @param input This will be written at the start of {@code file}.
     */
    public void write(String input) {
        if (!file.exists()) {
            makeFile();
        }
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file, charset))) {
            bf.write(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Writes {@code input} at the beginning of {@code file}. This method will overwrite anything currently written in {@code file}!</p>
     *
     * @param input This will be written at the start of {@code file}.
     */
    public void write(Object input) {
        if(input instanceof Collection){
            write(listToString((Collection)input));
        } else {
            write(input.toString());
        }
    }

    /**
     * <p>Writes {@code input} at the beginning of {@code file}. This method will overwrite anything currently written in the file!</p>
     *
     * @param input This will be written at the start of {@code file}.
     */
    public void write(Object... input) {
        write(arrayToString(input));
    }

    /**
     * <p>Writes {@code input} at the beginning of {@code file} and creates a new line. This method will overwrite anything currently written in the file!</p>
     *
     * @param input This will be written at the start of {@code file}.
     */
    public void writenl(String input) {
        write(input);
        newLine();
    }

    /**
     * <p>Writes {@code input} at the beginning of {@code file} and creates a new line. This method will overwrite anything currently written in the file!</p>
     *
     * @param input This will be written at the start of {@code file}.
     */
    public void writenl(Object input) {
        if(input instanceof Collection){
            writenl(listToString((Collection)input));
        }else {
            writenl(input.toString());
        }
    }

    /**
     * <p>Writes {@code input} at the beginning of {@code file} and creates a new line. This method will overwrite anything currently written in the file!</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void writenl(Object... input) {
        writenl(arrayToString(input));
    }

    /**
     * <p>Appends {@code input} to the end of {@code file}.</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void append(String input) {
        if (!file.exists()) {
            makeFile();
        }

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file, charset, true))) {
            bf.append(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Appends {@code input} to the end of {@code file}.</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void append(Object input) {
        if(input instanceof Collection){
            append(listToString((Collection)input));
        } else {
            append(input.toString());
        }
    }

    /**
     * <p>Appends {@code input} to the end of {@code file}.</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void append(Object... input) {
        append(arrayToString(input));
    }

    /**
     * <p>Appends {@code input} to the end of {@code file} and creates a new line.</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void appendnl(String input) {
        append(input);
        newLine();
    }

    /**
     * <p>Appends {@code input} to the end of {@code file} and creates a new line.</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void appendnl(Object input) {
        if(input instanceof Collection){
            appendnl(listToString((Collection)input));
        } else {
            appendnl(input.toString());
        }
    }

    /**
     * <p>Appends {@code input} to the end of {@code file} and creates a new line.</p>
     *
     * @param input This will be appended to the end of {@code file}.
     */
    public void appendnl(Object... input) {
        appendnl(arrayToString(input));
    }

    /**
     * <p>Creates a new line at the end of the file.</p>
     */
    public void newLine() {
        if (!file.exists()) {
            makeFile();
        }
        append(LINE_SEPARATOR);
    }

    /**
     * <p>Deletes all lines from the file.</p>
     */
    public void clear() { deleteAllLinesAfter(0);}

    /**
     * <p>Deletes all lines from the file except for the first line, which is usually reserved as headline for the data.
     *  Also inserts a newLine(), so the next append-Methods starts in line 2.</p>
     */
    public void clearData() {
        deleteAllLinesAfter(1);
        newLine();
    }

    /**
     * <p>Deletes a line from the file.</p>
     *
     * @param index Index of the line to be deleted. Line indices start at 1.
     */
    public void deleteLine(int index) {
        try {
            List<String> fileLines = Files.readAllLines(file.toPath());
            if (fileLines.size() >= index - 1) {
                fileLines.remove(index - 1);
            } else {
                throw new IndexOutOfBoundsException("Index " + (index - 1) + " out of bounds for length " + fileLines.size());
            }
            write(String.join(LINE_SEPARATOR, fileLines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Deletes all lines specified in the {@code indices} array.</p>
     *
     * @param indices An array containing the indices of all lines to be deleted. Those indices can be in any order and don't need to be sequential.
     */
    public void deleteLines(int... indices) {
        try {
            String[] fileLinesArray = Files.readAllLines(file.toPath()).toArray(String[]::new);
            for (int line : indices) {
                fileLinesArray[line - 1] = null;
            }
            List<String> fileLines = Arrays.stream(fileLinesArray).filter(Objects::nonNull).toList();
            write(String.join(LINE_SEPARATOR, fileLines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Deletes all lines after a specified line from the file.</p>
     *
     * @param index Index of the line after which every line is going to be deleted.
     */
    public void deleteAllLinesAfter(int index) {
        try {
            List<String> fileLines = Files.lines(file.toPath()).limit(index).toList();
            write(String.join(LINE_SEPARATOR, fileLines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Creates the directory named by filePath, including any necessary but nonexistent parent directories.</p>
     *
     * @return true if and only if the directory was created, along with all necessary parent directories; false otherwise
     */
    public boolean makeDirs() {
        return file.getParentFile().mkdirs();
    }

    /**
     * <p>Creates an empty file names by filePath, including any necessary but nonexistent parent directories.</p>
     *
     * @return true if and only if the file was created, along with all necessary parent directories; false otherwise
     */
    public boolean makeFile() {
        if (file.exists()) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            makeDirs();
        }

        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Private Methods

    private String arrayToString(Object[] input) {
        return listToString(Arrays.stream(input).toList());
    }

    private String listToString(Collection<Object> input) {
        String separator;
        if (strictNewLine) {
            separator = LINE_SEPARATOR;
        } else {
            separator = this.separator == null ? STANDARD_SEPARATOR : this.separator;
        }

        List<String> inputStrings = input.stream()
                .map(entry -> { if (entry == null) { entry = "null";} return entry;})
                .map(Object::toString)
                .toList();

        return String.join(separator, inputStrings);
    }

    // Getter and Setter

    public String getDirectoryPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return new File(filePath);
    }

    public Charset getCharset() {
        return charset;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public boolean isStrictNewLine() {
        return strictNewLine;
    }

    /**
     * This methods sets the value of {@link #strictNewLine}.
     *
     * @param strictNewLine If true, makes it so that all "newLine()-methods" (method-name contains "nl")
     *                      will create a new line after every array-element or list-element. Otherwise "nl"-Methods
     *                      will only create a new line at the end of the array or list.
     */
    public void setStrictNewLine(boolean strictNewLine) {
        this.strictNewLine = strictNewLine;
    }

    @Override
    public String toString() {
        return "Path: " + path + LINE_SEPARATOR + "Filename: " + filename + LINE_SEPARATOR + "Filepath: " + filePath + LINE_SEPARATOR + "Charset: " + charset;
    }
}