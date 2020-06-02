package com.blog.lucene;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.blog.entity.Blog;
import com.blog.util.DateUtil;
import com.blog.util.StringUtil;

/**
 * 使用lucene对博客进行增删改查
 * 相当于在本地文件里增加一个备份，能使查找的时候更快
 * 
 *
 */
public class BlogIndex {
	
	private Directory dir = null;
	private String lucenePath = "D://javatest//lucene";
	
	
	/**
	 * 获取对lucene的写入方法
	 * @throws Exception 
	 */
	public IndexWriter getWriter() throws Exception{
		//设置保存目录
		dir = FSDirectory.open(Paths.get(lucenePath,new String[0]));
		
		//得到写入方法，"SmartChineseAnalyzer"这个是中文的Analyzer
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir, iwc);
		return writer;
	}
	
	/**
	 * 增加索引
	 * @throws Exception 
	 */
	public void addIndex(Blog blog) throws Exception{
		//得到写入方法
		IndexWriter writer = getWriter();
		Document doc = new Document();
		//把整个Blog作为一个字段("Filed")放进doc
		doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		//把整个标题作为一个字段("Filed")放进doc
		doc.add(new TextField("title", blog.getTitle(),Field.Store.YES));
		//把当前时间作为一个字段("Filed")放进doc
		doc.add(new StringField("releaseDate",DateUtil.formatDate(new Date(),"YYYY-MM-dd"),Field.Store.YES));
		//把内容作为一个字段("Filed")放进doc,这里用getContentNoTag是为了传数据的时候不带html的数据
		doc.add(new TextField("content", blog.getContentNoTag(),Field.Store.YES));
		//把关键字作为一个字段("Filed")放进doc
		doc.add(new TextField("keyWord", blog.getKeyWord(),Field.Store.YES));
		writer.addDocument(doc);
		writer.close();
	}
	
	/**
	 * 更新索引
	 */
	public void updateIndex(Blog blog) throws Exception{
		IndexWriter writer = getWriter();
		Document doc = new Document();
		doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
		doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
		doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), "YYYY-MM-dd"), Field.Store.YES));
		doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
		doc.add(new TextField("keyWord", blog.getKeyWord(),Field.Store.YES));
		writer.updateDocument(new Term("id", String.valueOf(blog.getId())),doc);
		writer.close();
	}
	
	/**
	 * 删除索引
	 */
	public void deleteIndex(String blogId) throws Exception{
		IndexWriter writer = getWriter();
		//根据id删除
		writer.deleteDocuments(new Term[]{new Term("id",blogId)});
		writer.forceMergeDeletes();
		writer.commit();
		writer.close();
	}
	
	/**
	 * 搜索索引
	 * @param q		查询条件
	 * @return
	 * @throws Exception
	 */
	public List<Blog> searchBlog(String q) throws Exception{
		List<Blog> blogList = new LinkedList<Blog>();
		//获取目录（dir）
		dir = FSDirectory.open(Paths.get(lucenePath,new String[0]));
		//获取reader
		IndexReader reader = DirectoryReader.open(dir);
		//可以理解为从reader里获取一个流
		IndexSearcher is = new IndexSearcher(reader);
		//设置查询条件
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		//因为有可能按不同条件查询，所以把应该用到的条件都放进去
		QueryParser parser =new QueryParser("title",analyzer);
		Query query = parser.parse(q);
		QueryParser parser2 =new QueryParser("content",analyzer);
		Query query2 = parser2.parse(q);
		QueryParser parser3 =new QueryParser("keyWord",analyzer);
		Query query3 = parser3.parse(q);
		
		//Occur.*是常量，Occur.SHOULD(或者有这个查询条件)
		booleanQuery.add(query, Occur.SHOULD);
		booleanQuery.add(query2, Occur.SHOULD);
		booleanQuery.add(query3, Occur.SHOULD);
		
		//创建booleanQuery，最多返回100个查询结果
		TopDocs hits = is.search(booleanQuery.build(), 100);
		
		//在查询结果高亮显示搜索额关键字
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		//相当于查询得到的关键字，前面以及后面加上什么,这里是加粗以及变红
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter,scorer);
		highlighter.setTextFragmenter(fragmenter);
		
		//遍历查询结果，放入blogList
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc = is.doc(scoreDoc.doc);
			Blog blog = new Blog();
			
			//设置blog属性
			blog.setId(Integer.parseInt(doc.get("id")));
			blog.setReleaseDateStr(doc.get("releaseDate"));
			
			//得到上面新增、更新索引等操作所得到的title、content、keyWord
			String title = doc.get("title");
			String content = StringEscapeUtils.escapeHtml(doc.get("content"));
			String keyWord = doc.get("keyWord");
			
			//判定keyWord
			if(title!=null){
				TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
				String hTitle = highlighter.getBestFragment(tokenStream, title);
				//判断hTitle是否为空
				if (StringUtil.isEmpty(hTitle)) {
					blog.setTitle(title);
				}else {
					blog.setTitle(hTitle);
				}
			}
			
			//判定content
			if(content!=null){
				TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
				String hContent = highlighter.getBestFragment(tokenStream, content);
				//判断hContent是否为空
				if (StringUtil.isEmpty(hContent)) {
					//只显示前200个字
					if (content.length()<=200) {
						blog.setContent(content);
					}else {
						blog.setContent(content.substring(0, 200));
					}
				}else {
					blog.setContent(hContent);
				}
			}
			
			//判定keyWord
			if(keyWord!=null){
				TokenStream tokenStream = analyzer.tokenStream("keyWord", new StringReader(keyWord));
				String hKeyWord = highlighter.getBestFragment(tokenStream, keyWord);
				//判断hKeyWord是否为空
				if (StringUtil.isEmpty(hKeyWord)) {
					blog.setKeyWord(keyWord);
				}else {
					blog.setKeyWord(hKeyWord);
				}
			}
			//把blog放进blogList
			blogList.add(blog);
		}
		
		return blogList;
	}
}
