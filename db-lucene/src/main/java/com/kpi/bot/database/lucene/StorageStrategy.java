package com.kpi.bot.database.lucene;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public enum StorageStrategy {
    MEMORY() {
        private Directory directory = new RAMDirectory();

        @Override
        public Directory getDirectory() {
            return directory;
        }
    }, FILE() {
        private static final String INDEX_NAME = "./index.dat";

        @Override
        public Directory getDirectory() {
            try {
                return FSDirectory.open(Paths.get(INDEX_NAME));
            } catch (IOException e) {
                throw new RuntimeException("Can not create file based index", e);
            }
        }
    };

    public abstract Directory getDirectory();
}
