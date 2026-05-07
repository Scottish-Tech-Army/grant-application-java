package org.tfg.grant_java.service;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.entity.Questions;
import org.tfg.grant_java.repository.QuestionRepository;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;   // IMPORTANT: Lucene 9.x location
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



import org.tfg.grant_java.request.RecommendRequest;
import org.tfg.grant_java.response.QuestionRecommendation;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionRecommender {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionRecommender.class);

    private static final String FIELD_ID = "id";
    private static final String FIELD_TEXT = "text";

    private final QuestionRepository questionRepository;
    private final Path indexPath;
    private final Analyzer analyzer;
    private final Directory directory;
    private final IndexWriter writer;
    private final SearcherManager searcherManager;

    public QuestionRecommender(
            QuestionRepository questionRepository,
            @Value("${lucene.index.path}") String luceneIndexPathStr,
            @Value("${lucene.analyzer}") String luceneAnalyzerStr
    ) throws IOException {
        LOGGER.trace("Initializing Constructor");
        this.questionRepository = questionRepository;
        indexPath = Path.of(luceneIndexPathStr);
        analyzer = chooseAnalyzer(luceneAnalyzerStr);
        this.directory = initializeLuceneDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        this.writer = new IndexWriter(directory, config);
        this.searcherManager = new SearcherManager(writer, null);
        LOGGER.trace("Terminating Constructor");
    }

    public void rebuild() throws IOException {
        LOGGER.trace("Initializing Rebuild");
        writer.deleteAll();
        List<Questions> toBeIndexed = questionRepository.findAll();
        LOGGER.trace("toBeIndexed:{}", toBeIndexed);
        for(Questions question : toBeIndexed) {
            writer.addDocument(toDocument(question));
        }

        writer.commit();                          // ✅ REQUIRED
        searcherManager.maybeRefreshBlocking();

        LOGGER.trace("Terminating Rebuild");
    }

    public void addOrUpdateInIndex(Questions q) throws IOException {
        LOGGER.trace("Initializing Add Or Update Index");
        Term idTerm = new Term(FIELD_ID, q.getQuestionId().toString());
        writer.updateDocument(idTerm, toDocument(q));
        writer.commit();
        searcherManager.maybeRefreshBlocking();
        LOGGER.trace("Terminating  Add Or Update Index");
    }

    public void deleteFromIndex(String id) throws IOException {
        LOGGER.trace("Initializing Delete Index");
        writer.deleteDocuments(new Term(FIELD_ID, id));
        writer.commit();
        searcherManager.maybeRefreshBlocking();
        LOGGER.trace("Terminating Delete Index");
    }

    public List<QuestionRecommendation> recommend(RecommendRequest recommendRequest) throws IOException {
        LOGGER.trace("Initializing Recommend");
        searcherManager.maybeRefresh();
        IndexSearcher searcher = null;
        try {
            searcher = searcherManager.acquire();
            MoreLikeThis mlt = new MoreLikeThis(searcher.getIndexReader());
            mlt.setAnalyzer(analyzer);
            mlt.setFieldNames(new String[]{FIELD_TEXT});

            // Tune these if you want more/less aggressive matching
            mlt.setMinDocFreq(1);
            mlt.setMinTermFreq(1);

            LOGGER.trace("QuestionText:{}", recommendRequest.getQuestionText());
            Query query = mlt.like(FIELD_TEXT, new StringReader(recommendRequest.getQuestionText()));
            LOGGER.trace("query:{}", query);
            TopDocs topDocs = searcher.search(query, recommendRequest.getTopN());
            LOGGER.trace("topDocs:{}", topDocs);
            LOGGER.trace("scoreDocs:{}", Arrays.toString(topDocs.scoreDocs));
            List<QuestionRecommendation> results = new ArrayList<>();
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);

                QuestionRecommendation rec = new QuestionRecommendation(
                        doc.get(FIELD_ID),
                        doc.get(FIELD_TEXT),
                        sd.score
                );

                results.add(rec);
            }
            LOGGER.trace("Terminating Recommend");
            return results;
        } finally {
            if (searcher != null) {
                LOGGER.trace("Initializing Release");
                searcherManager.release(searcher);
                LOGGER.trace("Terminating Release");
            }
        }
    }

    private Directory initializeLuceneDirectory() throws IOException {
        LOGGER.trace("Initializing Initialize Lucene Directory");

        LOGGER.trace("Lucene Index Path:{}",this.indexPath.toAbsolutePath());
        if(!Files.exists(this.indexPath)) {
            LOGGER.trace("Creating Lucene Index Path");
            Files.createDirectories(this.indexPath);
        }
        LOGGER.trace("Terminating Initialize Lucene Directory");
        return FSDirectory.open(this.indexPath);
    }

    private Document toDocument(Questions question) {
        LOGGER.trace("Initializing Document Conversion");
        LOGGER.trace("question:{}", question);
        Document document = new Document();
        document.add(new StringField(FIELD_ID, question.getQuestionId().toString(), Field.Store.YES));
        document.add(new TextField(FIELD_TEXT, question.getQuestionText(), Field.Store.YES));
//        document.add(new StringField(FIELD_TEXT, question.getText(), Field.Store.YES));
        LOGGER.trace("document:{}", document);
        LOGGER.trace("Terminating Document Conversion");
        return document;
    }

    private Analyzer chooseAnalyzer(String luceneAnalyzerStr) {
        LOGGER.trace("Initializing Analyzer Choice");
        Analyzer analyzer;
        if(luceneAnalyzerStr.equals("StandardAnalyzer")) {
            analyzer = new StandardAnalyzer();
        } else if(luceneAnalyzerStr.equals("LanguageAnalyzer")) {
            analyzer = new EnglishAnalyzer();
        } else {
            analyzer = new EnglishAnalyzer();
        }
        LOGGER.trace("analyzer:{}", analyzer.getClass().getSimpleName());
        LOGGER.trace("Terminating  Analyzer Choice");
        return analyzer;
    }

    @PreDestroy
    public void shutdown() throws IOException {
        // Clean shutdown for containers/tests
        LOGGER.trace("Initializing Shutdown");
        searcherManager.close();
        writer.close();
        directory.close();
        LOGGER.trace("Terminating Shutdown");
    }
}
