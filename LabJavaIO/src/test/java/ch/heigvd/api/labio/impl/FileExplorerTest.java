package ch.heigvd.api.labio.impl;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * *** IMPORTANT WARNING : DO NOT EDIT THIS FILE ***
 * <p>
 * This file is used to specify what you have to implement. To check your work,
 * we will run our own copy of the automated tests. If you change this file,
 * then you will introduce a change of specification!!!
 *
 * @author Olivier Liechti, Juergen Ehrensberger
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileExplorerTest {

    private static final Logger LOG = Logger.getLogger(FileExplorerTest.class.getName());

    final String workingDirectory = "./workspace/testFileExplorer";
    final String filenamePrefix = "file-";

    private List<File> generateTestTree(Path path, int levels, int maxChildrenFolders, int maxChildrenFiles) throws IOException {
        List<File> dfsNodes = new ArrayList<>();
        File dir = path.toFile();
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not delete {0} : {1}", new Object[]{dir, ex.getMessage()});
        }
        dir.mkdirs();
        generateLevel(dir, 0, levels, maxChildrenFolders, maxChildrenFiles, dfsNodes);
        return dfsNodes;
    }

    private void generateLevel(File dir, int level, int maxLevels, int maxChildrenFolders, int maxChildrenFiles, List<File> dfsNodes) throws IOException {
        int childrenFolders = (int) (Math.random() * maxChildrenFolders);
        int childrenFiles = (int) (Math.random() * maxChildrenFiles);

        for (int i = 0; i < childrenFiles; i++) {
            File file = createFile(dir, filenamePrefix + i + ".utf8");
            dfsNodes.add(file);
        }

        for (int i = 0; i < childrenFolders; i++) {
            String dirName = dir.getName() + "." + (i + 1);
            File newDir = new File(dir, dirName);
            //dfsNodes.add(newDir);
            newDir.mkdirs();
            if (level < maxLevels) {
                generateLevel(newDir, level + 1, maxLevels, maxChildrenFolders, maxChildrenFiles, dfsNodes);
            }
        }
    }

    private File createFile(File dir, String filename) throws IOException {
        File file = Paths.get(dir.getPath(), filename).toFile();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        writer.write("Some text.");
        writer.close();
        return file;
    }

    @BeforeAll
    @AfterAll
    public void clearFiles() throws IOException {
        FileUtils.deleteDirectory(new File(workingDirectory));
    }

    @Test
    public void itShouldWorkWithMissingDirectory() {
        Path path = Paths.get(workingDirectory, "missingDir");
        FileExplorer explorer = new FileExplorer();
        explorer.explore(path.toFile());
    }

    @Test
    public void itShouldWorkWithEmptyDirectory() throws IOException {
        Path path = Paths.get(workingDirectory, "emptyDir");
        generateTestTree(path, 0, 0, 0);
        FileExplorer explorer = new FileExplorer();
        explorer.explore(path.toFile());
    }

    @Test
    public void itShouldExploreASingleFile() throws IOException {
        File dir = Paths.get(workingDirectory, "singleFile").toFile();
        dir.mkdirs();
        String filename = filenamePrefix + "0.utf8";
        createFile(dir, filename);
        FileExplorer explorer = new FileExplorer();
        explorer.explore(dir);

        File inputFile = Paths.get(dir.getPath(), filename).toFile();
        File outputFile = Paths.get(dir.getPath(), filename + ".out").toFile();
        assertTrue(inputFile.exists() && inputFile.isFile());
        assertTrue(outputFile.exists() && outputFile.isFile());
    }

    @Test
    public void itShouldExploreADirectoryHierarchy() throws IOException {
        Path path = Paths.get(workingDirectory, "dirHierarchy");
        List<File> files = generateTestTree(path, 5, 5, 5);
        FileExplorer explorer = new FileExplorer();
        explorer.explore(path.toFile());

        for (File file : files) {
            File outfile = Paths.get(file.getPath() + ".out").toFile();
            assertTrue(file.exists() && file.isFile());
            assertTrue(outfile.exists() && outfile.isFile());
        }
    }
}