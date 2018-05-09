package com.kpi.bot.database.lucene;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public enum StorageStrategy {
    MEMORY() {
        private RAMDirectory directory = new RAMDirectory();

        @Override
        public RAMDirectory getDirectory() {
            return directory;
        }
    }, FILE() {

        @Override
        public FSDirectory getDirectory() {
            try {
                return FSDirectory.open(Paths.get(INDEX_NAME));
            } catch (IOException e) {
                throw new RuntimeException("Can not create file based index", e);
            }
        }
    };

    public static final String INDEX_NAME = "./index.dat";

    public abstract Directory getDirectory();

    public static StorageStrategy getStorageStrategy() {
        return StorageStrategy.MEMORY;
    }
}
