package ru.org.sevn.common.homefs;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import java.util.Arrays;

public class HomeFileSystem extends FileSystem {

    static class IndexNode {
        byte[] name;
        int    hashcode;  // node is hashable/hashed by its name
        int    pos = -1;  // postion in cen table, -1 menas the
                          // entry does not exists in zip file
        IndexNode(byte[] name, int pos) {
            name(name);
            this.pos = pos;
        }

        final static IndexNode keyOf(byte[] name) { // get a lookup key;
            return new IndexNode(name, -1);
        }

        final void name(byte[] name) {
            this.name = name;
            this.hashcode = Arrays.hashCode(name);
        }

        final IndexNode as(byte[] name) {           // reuse the node, mostly
            name(name);                             // as a lookup "key"
            return this;
        }

        boolean isDir() {
            return name != null &&
                   (name.length == 0 || name[name.length - 1] == '/');
        }

        public boolean equals(Object other) {
            if (!(other instanceof IndexNode)) {
                return false;
            }
            return Arrays.equals(name, ((IndexNode)other).name);
        }

        public int hashCode() {
            return hashcode;
        }

        IndexNode() {}
        IndexNode sibling;
        IndexNode child;  // 1st child
    }

    static class Entry extends IndexNode {
        int    version;
        int    flag;
        int    method = -1;    // compression method
        long   mtime  = -1;    // last modification time (in DOS time)
        long   atime  = -1;    // last access time
        long   ctime  = -1;    // create time
        long   size   = -1;    // size of entry data
    }
    
    @Override
    public FileSystemProvider provider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isReadOnly() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSeparator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Path getPath(String first, String... more) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
