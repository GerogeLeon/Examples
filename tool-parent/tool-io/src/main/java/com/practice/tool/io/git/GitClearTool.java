package com.practice.tool.io.git;

import java.io.File;
import java.util.Stack;

/**
 * @author Luo Bao Ding
 * @since 2018/6/20
 */
public class GitClearTool {


    private Stack<File> deleteFailedGitStack = new Stack<File>();
    private File rootDir = new File("C:\\xxx");

    public static void main(String[] args) {
        new GitClearTool().clearGit();

    }

    public void clearGit() {
        try {
            unitDel(rootDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (deleteFailedGitStack.size() == 0) {
            System.out.println("clear git successful! ");
        } else {
            System.err.println("clear git failed!");
            System.err.println("deleteFailedGitStack:\n" + deleteFailedGitStack);
        }

    }

    private void unitDel(File path) {
        File[] files = path.listFiles((file) -> {
            String name = file.getName();

            boolean isGit = name.equalsIgnoreCase(".git") && file.isDirectory();
            if (isGit) {
                deleteDir(file);
            }
            return !name.equalsIgnoreCase(".git") &&
                    !name.equalsIgnoreCase("target") &&
                    !name.equalsIgnoreCase("gradle") &&
                    !name.equalsIgnoreCase("src") &&
                    !name.equalsIgnoreCase(".mvn") &&
                    !name.equalsIgnoreCase(".idea") &&
                    file.isDirectory();
        });
        assert files != null;
        for (File file : files) {
            unitDel(file);
        }
    }

    private void deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    boolean delete = file.delete();
                    if (!delete) {
                        String message = "delete failed : " + file.getAbsolutePath();
                        System.err.println(message);
                        deleteFailedGitStack.add(file);
                        throw new RuntimeException(message);
                    }
                }
            }
        }
        boolean delete = dir.delete();
        if (!delete) {
            System.err.println("delete failed : " + dir.getAbsolutePath());
            deleteFailedGitStack.add(dir);
        }
    }
}
