package com.kpi.bot.database.lucene;

import com.kpi.bot.data.Repository;
import com.kpi.bot.data.SearchableRepository;
import com.kpi.bot.entity.data.Identifiable;
import com.kpi.bot.entity.search.SearchCriteria;
import com.kpi.bot.entity.search.SearchPredicate;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;


public class LuceneDatabase<T extends Identifiable> implements SearchableRepository<T> {

    private static final String ID_FIELD_NAME = "id";
    private StorageStrategy storageStrategy = StorageStrategy.MEMORY;

    private Set<String> indexedFields;
    private Set<String> fullTextSearchFields;
    private Repository<T> backingRepository;
    private IndexWriter writer;
    private IndexSearcher searcher;

    public LuceneDatabase(Repository<T> backingRepository, List<String> indexedFields, List<String> fullTextSearchFields) {
        this.backingRepository = backingRepository;

        this.indexedFields = new HashSet<>(indexedFields);
        this.fullTextSearchFields = new HashSet<>(fullTextSearchFields);

        try {
            Directory directory = storageStrategy.getDirectory();

            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            this.writer = new IndexWriter(directory, writerConfig);
            writer.commit();
            this.searcher = new IndexSearcher(DirectoryReader.open(directory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Document transformToDocument(T object) {
        try {
            Document document = new Document();

            document.add(new StringField(ID_FIELD_NAME, object.getId(), Field.Store.YES));

            Class<?> cls = object.getClass();
            while (!Object.class.equals(cls)) {
                for (java.lang.reflect.Field objectField : cls.getDeclaredFields()) {
                    Class<?> type = objectField.getType();
                    objectField.setAccessible(true);
                    if (fullTextSearchFields.contains(objectField.getName())) {
                        if (type.isAssignableFrom(CharSequence.class)) {
                            document.add(new TextField(objectField.getName(), objectField.get(object).toString(), Field.Store.NO));
                        }
                    } else if (indexedFields.contains(objectField.getName())) {
                        if (type.isAssignableFrom(Collection.class)) {
                            try {
                                Class<?> elementType = (Class<?>) ((ParameterizedType) objectField.getGenericType()).getActualTypeArguments()[0];
                                IndexableFieldFactory factory = IndexableFieldFactory.getIndexableFieldFactory(elementType);
                                Collection collection = (Collection) objectField.get(object);
                                for (Object item : collection) {
                                    document.add(factory.getField(objectField.getName(), item));
                                }
                            } catch (ClassCastException e) {
                                throw new RuntimeException("Can not determine generic type for field " + objectField.getName(), e);
                            }
                        } else {
                            document.add(IndexableFieldFactory.getIndexableFieldFactory(type).getField(objectField.getName(), objectField.get(object)));
                            System.out.println("Indexed field!");
                        }
                    }
                }
            }

            return document;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T save(T entity) {
        try {
            delete(entity.getId());
            writer.addDocument(transformToDocument(entity));
            writer.commit();
            backingRepository.save(entity);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        List<T> results = new LinkedList<T>();
        for (T entity: entities) {
            results.add(save(entity));
        }

        return results;
    }

    @Override
    public T find(String key) {
        return backingRepository.find(key);
    }

    @Override
    public List<T> findAll() {
        return backingRepository.findAll();
    }

    @Override
    public void delete(String key) {
        try {
            backingRepository.delete(key);
            Term term = new Term(ID_FIELD_NAME, key);
            writer.deleteDocuments(term);
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            backingRepository.deleteAll();
            writer.deleteAll();
            writer.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> findByQuery(Query query, Long offset, Long limit) {
        TopScoreDocCollector collector = TopScoreDocCollector.create(Math.toIntExact(offset + limit));
        try {
            searcher.search(query, collector);
            ScoreDoc[] docs = collector.topDocs().scoreDocs;
            List<T> results = new LinkedList<T>();
            for (int i = Math.toIntExact(offset); i < offset + limit && i < docs.length; i++) {
                results.add(backingRepository.find(searcher.doc(docs[i].doc).get(ID_FIELD_NAME)));
            }
            return results;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findByCriteria(SearchCriteria criteria, Long offset, Long limit) {
        try {
            System.out.println(searcher.getIndexReader().getDocCount("id"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BooleanQuery.Builder query = new BooleanQuery.Builder();
        for (SearchPredicate predicate : criteria.getPredicates()) {
            Query fieldQuery;
            switch (predicate.getType()) {
                case HIGHER:
                    fieldQuery = TermRangeQuery.newStringRange(predicate.getField(), predicate.getValue().toString(), null, true, true);
                    break;
                case EQUALS:
                    fieldQuery = new TermQuery(new Term(predicate.getField(), predicate.getValue().toString()));
                    break;
                case LOWER:
                    fieldQuery = TermRangeQuery.newStringRange(predicate.getField(), null, predicate.getValue().toString(), true, true);
                    break;
                case LIKE:
                    if (fullTextSearchFields.contains(predicate.getField())) {
                        fieldQuery = new FuzzyQuery(new Term(predicate.getField(), predicate.getValue().toString()));
                    } else {
                        throw new RuntimeException("You should mark field " + predicate.getField() + " as text search field");
                    }
                    break;
                default:
                    throw new RuntimeException("Unrecognised type " + predicate.getType());
            }

            BooleanClause.Occur occur;
            if (predicate.isMatch()) {
                occur = BooleanClause.Occur.MUST;
            } else {
                occur = BooleanClause.Occur.MUST_NOT;
            }

            query.add(fieldQuery, occur);
        }

        return findByQuery(query.build(), offset, limit);
    }

    public List<T> findByQuery(String query, Long offset, Long limit) {
        try {
            return findByQuery(new QueryParser("body", new StandardAnalyzer()).parse(query), offset, limit);
        } catch (ParseException e) {
            throw new RuntimeException("Can not parse query", e);
        }
    }
}
