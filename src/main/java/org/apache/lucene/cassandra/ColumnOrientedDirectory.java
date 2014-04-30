package org.apache.lucene.cassandra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.utils.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>ColumnOrientedDirectory</code> captures the mapping of the
 * concepts of a directory to a column family in Cassandra. Specifically, it
 * treats each row in the column family as a file underneath the directory.
 * 
 * <p>
 * This class in turn relies on the {@link CassandraClient} for all
 * low-level gets and puts to the Cassandra server. More importantly, it
 * does not require that the {@link CassandraClient} to be familiar with the
 * notion of Lucene directories. Rather, it transparently translates those
 * notions to column families. In so doing, it ends up hiding the Cassandra
 * layer from its consumers.
 * </p>
 */
public class ColumnOrientedDirectory {
    
    private static Logger logger = LoggerFactory.getLogger(ColumnOrientedDirectory.class);
    
    // The name of the column that holds the file descriptor.
    protected static final String descriptorColumn = "DESCRIPTOR";
    
    // The list of meta-columns currently defined for each file (or row).
    protected static final List<byte[]> systemColumns = new ArrayList<byte[]>();
    static {
        systemColumns.add(descriptorColumn.getBytes());
    }
    
    CassandraClient cassandraClient;
    int blockSize;
    
    public ColumnOrientedDirectory(CassandraClient cassandraClient, int blockSize) {
        this.cassandraClient = cassandraClient;
        this.blockSize = blockSize;
    }
    
    /**
     * @return the names of the files in this directory
     * @throws IOException
     */
    public String[] getFileNames() throws IOException {
        byte[][] keys = cassandraClient.getKeys(systemColumns);
        List<String> fileNames = new ArrayList<String>();
        for (byte[] key : keys) {
            fileNames.add(new String(key));
        }
        return fileNames.toArray(new String[] {});
    }

    /**
     * Return the file descriptor for the file of the given name. If the
     * file cannot be found, then return null, instead of trying to create
     * it.
     * 
     * @param fileName
     *            the name of the file
     * @return the descriptor for the given file
     * @throws IOException
     */
    protected FileDescriptor getFileDescriptor(String fileName)
            throws IOException {
        return getFileDescriptor(fileName, false);
    }

    /**
     * Return the file descriptor for the file of the given name.
     * 
     * @param fileName
     *            the name of the file
     * @param createIfNotFound
     *            if the file wasn't found, create it
     * @return the descriptor for the given file
     * @throws IOException
     */
    protected FileDescriptor getFileDescriptor(String fileName,
            boolean createIfNotFound) throws IOException {
        logger.trace("fileName {} createIfNotFound {}", fileName, createIfNotFound);
        FileDescriptor fileDescriptor =
                FileDescriptorUtils.fromBytes(cassandraClient.getColumn(
                        fileName.getBytes(), descriptorColumn.getBytes()), blockSize);
        if (fileDescriptor == null && createIfNotFound) {
            logger.trace("creating empty fd");
            fileDescriptor = new FileDescriptor(fileName, blockSize);
            setFileDescriptor(fileDescriptor);
        }
        return fileDescriptor;
    }

    /**
     * Save the given file descriptor.
     * 
     * @param fileDescriptor
     *            the file descriptor being saved
     * @throws IOException
     */
    public void setFileDescriptor(FileDescriptor fileDescriptor)
            throws IOException {
        BlockMap blockMap = new BlockMap();
        blockMap.put(descriptorColumn,
                FileDescriptorUtils.toString(fileDescriptor));
        cassandraClient.setColumns(
                ByteBufferUtil.bytes(fileDescriptor.getName()), blockMap);
    }
}