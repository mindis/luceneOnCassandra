package org.apache.lucene.cassandra.nio;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

public class CassandraFileSystemProvider extends FileSystemProvider {
    
    private static final String USER_DIR = "user.dir";
    private final CassandraFileSystem theFileSystem;

    public CassandraFileSystemProvider() {
        String userDir = System.getProperty(USER_DIR);
        theFileSystem = new CassandraFileSystem(this, userDir);
    }

    @Override
    public String getScheme() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path getPath(URI uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path,
            Set<? extends OpenOption> options, FileAttribute<?>... attrs)
            throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir,
            Filter<? super Path> filter) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs)
            throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(Path path) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options)
            throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void move(Path source, Path target, CopyOption... options)
            throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path,
            Class<V> type, LinkOption... options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path,
            Class<A> type, LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes,
            LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value,
            LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public FileChannel newFileChannel(Path obj,
                                      Set<? extends OpenOption> options,
                                      FileAttribute<?>... attrs)
        throws IOException
    {
        CassandraPath file = checkPath(obj);
        int mode = CassandraFileModeAttribute
            .toCassandraMode(CassandraFileModeAttribute.ALL_READWRITE, attrs);
        try {
            return CassandraChannelFactory.newFileChannel(file, options, mode);
        } catch (CassandraException x) {
            x.rethrowAsIOException(file);
            return null;
        }
    }
    
    CassandraPath checkPath(Path obj) {
        if (obj == null)
            throw new NullPointerException();
        if (!(obj instanceof CassandraPath))
            throw new ProviderMismatchException();
        return (CassandraPath)obj;
    }
    
}
