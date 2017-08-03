package ru.org.sevn.common.homefs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

public class HomeFileStore extends FileStore {

    private final HomeFileSystem fs;
    
    public HomeFileStore(HomePath hpath) {
        fs = (HomeFileSystem)hpath.getFileSystem();
    }

    @Override
    public String name() {
        return fs.toString() + "/";
    }

    @Override
    public String type() {
        return "homefs";
    }

    @Override
    public boolean isReadOnly() {
        return fs.isReadOnly();
    }

    @Override
    public long getTotalSpace() throws IOException {
        return new HomeFileStoreAttributes(this).totalSpace();
    }

    @Override
    public long getUsableSpace() throws IOException {
        return new HomeFileStoreAttributes(this).usableSpace();
    }

    @Override
    public long getUnallocatedSpace() throws IOException {
        return new HomeFileStoreAttributes(this).unallocatedSpace();
    }

    @Override
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
        return (type == BasicFileAttributeView.class ||
                type == HomeFileAttributeView.class);
    }

    @Override
    public boolean supportsFileAttributeView(String name) {
        return name.equals("basic") || name.equals("home");
    }

    @Override
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
        if (type == null)
            throw new NullPointerException();
        return (V)null;
    }

    @Override
    public Object getAttribute(String attribute) throws IOException {
        if (attribute.equals("totalSpace"))
               return getTotalSpace();
         if (attribute.equals("usableSpace"))
               return getUsableSpace();
         if (attribute.equals("unallocatedSpace"))
               return getUnallocatedSpace();
         throw new UnsupportedOperationException("does not support the given attribute");
    }
    
    private static class HomeFileStoreAttributes {
        final FileStore fstore;
        final long size;

        public HomeFileStoreAttributes(HomeFileStore fileStore) throws IOException {
            Path path = FileSystems.getDefault().getPath(fileStore.name());
            this.size = Files.size(path);
            this.fstore = Files.getFileStore(path);
        }

        public long totalSpace() {
            return size;
        }

        public long usableSpace() throws IOException {
            if (!fstore.isReadOnly()) return fstore.getUsableSpace();
            return 0;
        }

        public long unallocatedSpace()  throws IOException {
            if (!fstore.isReadOnly()) return fstore.getUnallocatedSpace();
            return 0;
        }
    }
}
