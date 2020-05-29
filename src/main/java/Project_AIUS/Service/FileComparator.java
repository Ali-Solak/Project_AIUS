package Project_AIUS.Service;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
        return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
    }
}
