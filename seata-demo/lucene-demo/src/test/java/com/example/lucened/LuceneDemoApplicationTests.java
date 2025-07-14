package com.example.lucened;

import com.example.lucene.LuceneDemoApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(classes = LuceneDemoApplication.class)
class LuceneDemoApplicationTests {

    @Test
    void buildIndex() throws IOException {
        Directory index = new MMapDirectory(FileSystems.getDefault().getPath("d://lucene-index", "demo")); // 使用内存中的目录，实际使用中可以替换为FSDirectory指向磁盘路径
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        try (IndexWriter writer = new IndexWriter(index, config)) {
            Document doc = new Document();
            doc.add(new StringField("id", "1", Field.Store.YES)); // StringField是不可分词的字段，适合用作ID等
            doc.add(new TextField("content", "This is the text to be indexed.", Field.Store.YES)); // TextField是可分词的字段，适合全文搜索的文本内容
            writer.addDocument(doc);
            //
            doc = new Document();
            doc.add(new StringField("id", "2", Field.Store.YES)); // StringField是不可分词的字段，适合用作ID等
            doc.add(new TextField("content", "This is the text to be indexed.", Field.Store.YES)); // TextField是可分词的字段，适合全文搜索的文本内容
            writer.addDocument(doc);
            writer.commit(); // 提交更改到索引中
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            index.close(); // 关闭目录以释放资源
        }
    }

    @Test
    void queryIndex() throws IOException {
        Directory index = new MMapDirectory(FileSystems.getDefault().getPath("d://lucene-index", "demo")); // 使用内存中的目录，实际使用中可以替换为FSDirectory指向磁盘路径
        try (IndexReader reader = DirectoryReader.open(index)) {
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
            Query parse = new QueryParser("content", standardAnalyzer).parse("text");
            TopDocs search = indexSearcher.search(parse, 10);
            for (int i = 0; i < search.scoreDocs.length; i++) {
                Document doc = indexSearcher.doc(search.scoreDocs[i].doc);
                System.out.println(doc.get("id"));
                System.out.println(doc.get("content"));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            index.close(); // 关闭目录以释放资源
        }
    }

    //准备一下用来测试的数据
    //用来标识文档
    private Integer ids[] = {1, 2, 3};
    private String citys[] = {"上海", "南京", "青岛"};
    private String descs[] = {
            "上海是个繁华的城市。",
            "南京是一个文化的城市南京，简称宁，是江苏省会，地处中国东部地区，长江下游，濒江近海。全市下辖11个区，总面积6597平方公里，2013年建成区面积752.83平方公里，常住人口818.78万，其中城镇人口659.1万人。[1-4] “江南佳丽地，金陵帝王州”，南京拥有着6000多年文明史、近2600年建城史和近500年的建都史，是中国四大古都之一，有“六朝古都”、“十朝都会”之称，是中华文明的重要发祥地，历史上曾数次庇佑华夏之正朔，长期是中国南方的政治、经济、文化中心，拥有厚重的文化底蕴和丰富的历史遗存。[5-7] 南京是国家重要的科教中心，自古以来就是一座崇文重教的城市，有“天下文枢”、“东南第一学”的美誉。截至2013年，南京有高等院校75所，其中211高校8所，仅次于北京上海；国家重点实验室25所、国家重点学科169个、两院院士83人，均居中国第三。[8-10]。",
            "青岛是一个美丽的城市。"
    };

    @Test
    void buildChineseIndex() throws IOException {

        Directory index = new MMapDirectory(FileSystems.getDefault().getPath("d://lucene-index", "demo01"));
        IndexWriterConfig config = new IndexWriterConfig(new SmartChineseAnalyzer());
        try (IndexWriter writer = new IndexWriter(index, config)) {
            for (int i = 0; i < ids.length; i++) {
                Document doc = new Document();
                // 添加 IntPoint 用于范围查询
                doc.add(new IntPoint("id", ids[i]));
//                doc.add(new StringField("id", ids[i]+"", Field.Store.YES));
//                // 添加 StoredField 用于存储原始值
                doc.add(new StoredField("id", ids[i]));
//                // 添加 NumericDocValuesField 用于排序或聚合
//                doc.add(new NumericDocValuesField("id", ids[i]));
                doc.add(new StringField("city", citys[i], Field.Store.YES));
                doc.add(new TextField("desc", descs[i], Field.Store.YES));
                //添加文档
                writer.updateDocument(new Term("id", ids[i]+""), doc);
            }
            writer.commit(); // 提交更改到索引中
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            index.close(); // 关闭目录以释放资源
        }
    }

    @Test
    void queryChineseIndex() throws IOException {
        Directory index = new MMapDirectory(FileSystems.getDefault().getPath("d://lucene-index", "demo01")); // 使用内存中的目录，实际使用中可以替换为FSDirectory指向磁盘路径
        try (IndexReader reader = DirectoryReader.open(index)) {
            SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
//            Query parse = new QueryParser("desc", analyzer).parse("上海");
            Query parse = IntPoint.newRangeQuery("id", 0, 1);
            //如果不指定参数的话，默认是加粗，即 <b><b/>
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color=red>", "</font></b>");
            //根据查询对象计算得分，会初始化一个查询结果最高的得分
            QueryScorer scorer = new QueryScorer(parse);
            //根据这个得分计算出一个片段
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            //将这个片段中的关键字用上面初始化好的高亮格式高亮
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
            //设置一下要显示的片段
            highlighter.setTextFragmenter(fragmenter);

            IndexSearcher indexSearcher = new IndexSearcher(reader);
            TopDocs search = indexSearcher.search(parse, 10);//取出符合条件的前十条
            //取出每条查询结果
            List<String> list = new ArrayList<>();
            for (ScoreDoc scoreDoc : search.scoreDocs) {
                //scoreDoc.doc 相当于 docID，根据这个 docID 来获取文档
                Document doc = indexSearcher.doc(scoreDoc.doc);
                log.info("id:{}", doc.get("id"));
                log.info("city:{}", doc.get("city"));
                log.info("desc:{}", doc.get("desc"));
                String desc = doc.get("desc");
                //显示高亮
                if (desc != null) {
                    TokenStream tokenStream = analyzer.tokenStream("desc", new StringReader(desc));
                    String summary = highlighter.getBestFragment(tokenStream, desc);
                    log.info("高亮后的desc:{}", summary);
                    list.add(summary);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            index.close(); // 关闭目录以释放资源
        }
    }

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

}
