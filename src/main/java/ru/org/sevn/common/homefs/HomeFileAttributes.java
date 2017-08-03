package ru.org.sevn.common.homefs;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Formatter;

public class HomeFileAttributes implements BasicFileAttributes {
    
    private final HomeFileSystem.Entry e;

    HomeFileAttributes(HomeFileSystem.Entry e) {
        this.e = e;
    }

    ///////// basic attributes ///////////
    @Override
    public FileTime creationTime() {
        if (e.ctime != -1)
            return FileTime.fromMillis(e.ctime);
        return null;
    }

    @Override
    public boolean isDirectory() {
        return e.isDir();
    }

    @Override
    public boolean isOther() {
        return false;
    }

    @Override
    public boolean isRegularFile() {
        return !e.isDir();
    }

    @Override
    public FileTime lastAccessTime() {
        if (e.atime != -1)
            return FileTime.fromMillis(e.atime);
        return null;
    }

    @Override
    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(e.mtime);
    }

    @Override
    public long size() {
        return e.size;
    }

    @Override
    public boolean isSymbolicLink() {
        return false;
    }

    @Override
    public Object fileKey() {
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(1024);
        Formatter fm = new Formatter(sb);
        if (creationTime() != null)
            fm.format("    creationTime    : %tc%n", creationTime().toMillis());
        else
            fm.format("    creationTime    : null%n");

        if (lastAccessTime() != null)
            fm.format("    lastAccessTime  : %tc%n", lastAccessTime().toMillis());
        else
            fm.format("    lastAccessTime  : null%n");
        fm.format("    lastModifiedTime: %tc%n", lastModifiedTime().toMillis());
        fm.format("    isRegularFile   : %b%n", isRegularFile());
        fm.format("    isDirectory     : %b%n", isDirectory());
        fm.format("    isSymbolicLink  : %b%n", isSymbolicLink());
        fm.format("    isOther         : %b%n", isOther());
        fm.format("    fileKey         : %s%n", fileKey());
        fm.format("    size            : %d%n", size());
        fm.close();
        return sb.toString();
    }

}
