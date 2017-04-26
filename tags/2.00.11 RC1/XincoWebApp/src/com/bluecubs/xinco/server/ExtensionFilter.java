package com.bluecubs.xinco.server;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class ExtensionFilter implements FilenameFilter {

    private String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith(extension));
    }
}
