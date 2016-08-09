package com.searchodyssey.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.ArrayList;


import redis.clients.jedis.Jedis;


/**
 * Encapsulates a map from search term to frequency (count).
 * 
 * @author downey
 *
 */
public class TermCounter {
	
	private Map<String, Integer> map;
    private Map<String, Integer> outLinkstoURLs;
	private String label;
	
	public TermCounter(String label) {
		this.label = label;
		this.map = new HashMap<String, Integer>();
        this.outLinkstoURLs = new HashMap<String, Integer>();
	}
	
	public String getLabel() {
		return label;
	}
	
	public int getIncomingLinkNum() {
		return incomingLinkNum;
	}

    public Map<String, Integer> getLinkMap(){
        return outLinkstoURLs;
    }
	
	/**
	 * Returns the total of all counts.
	 * 
	 * @return
	 */
	public int size() {
		int total = 0;
		for (Integer value: map.values()) {
			total += value;
		}
        for (Integer value: outLinkstoURLs.values()) {
            total += value;
        }
		return total;
	}

	/**
	 * Takes a collection of Elements and counts their words.
	 * 
	 * @param paragraphs
	 */
	public void processElements(Elements paragraphs) {
		for (Node node: paragraphs) {
			processTree(node);
		}
	}
	
	/**
	 * Finds TextNodes in a DOM tree and counts their words.
	 * 
	 * @param root
	 */
	public void processTree(Node root) {
		// NOTE: we could use select to find the TextNodes, but since
		// we already have a tree iterator, let's use it.

		for (Node node: new WikiNodeIterable(root)) {
			if (node instanceof Element) {
				Elements links=((Element) node).select("a");
				for(Element f : links) {
        			String url=((Element) node).attr("href");
             		if(url.startsWith("/wiki/")) {
                        url="https://en.wikipedia.org"+url;
                        Integer count = outLinkstoURLs.get(url);
                        Integer newCount = count == null ? 1 : (count+1);
                        outLinkstoURLs.put(url, newCount);
             		}
             	}
			}
			if (node instanceof TextNode) {
				processText(((TextNode) node).text());
			}
		}
        
        
	}

	/**
	 * Splits `text` into words and counts them.
	 * 
	 * @param text  The text to process.
	 */
	public void processText(String text) {
		// replace punctuation with spaces, convert to lower case, and split on whitespace
		String[] array = text.replaceAll("\\pP", " ").toLowerCase().split("\\s+");
		
		for (int i=0; i<array.length; i++) {
			String term = array[i];
			incrementTermCount(term);
		}
	}
	

	/**
	 * Increments the counter associated with `term`.
	 * 
	 * @param term
	 */
	public void incrementTermCount(String term) {
		// System.out.println(term);
		put(term, get(term) + 1);
	}

	/**
	 * Adds a term to the map with a given count.
	 * 
	 * @param term
	 * @param count
	 */
	public void put(String term, int count) {
		map.put(term, count);
	}

	/**
	 * Returns the count associated with this term, or 0 if it is unseen.
	 * 
	 * @param term
	 * @return
	 */
	public Integer get(String term) {
		Integer count = map.get(term);
		return count == null ? 0 : count;
	}

	/**
	 * Returns the set of terms that have been counted.
	 * 
	 * @return
	 */
	public Set<String> keySet() {
		return map.keySet();
	}
	
	/**
	 * Print the terms and their counts in arbitrary order.
	 */
	public void printCounts() {
		for (String key: keySet()) {
			Integer count = get(key);
			System.out.println(key + ", " + count);
		}
		System.out.println("Total of all counts = " + size());
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
        Jedis jedis = JedisMaker.make();
        JedisIndex index = new JedisIndex(jedis);
		String start = "https://en.wikipedia.org/wiki/Space";
        
		// create a crawler, crawl (should get 100 links) 
		WikiCrawler wc = new WikiCrawler(start);
       // ArrayList<TermCounter> tcList = new ArrayList<TermCounter>();
        private Map<String, TermCounter> tcListMap = new HashMap<String, TermCounter>();
        index.flush();
		wc.crawl(false);
		// grab the queue
		Queue<String> queue = wc.getQueue();
		// loop thorugh the queue of links we need to make term counters
		while(wc.queueSize() > 0) {
			String url = queue.remove();
			// using wikifetcher for every link to actually get the paragraphs for that link
			WikiFetcher wf = new WikiFetcher();
            if(index.isIndexed(url))
                continue;
			Elements paragraphs = wf.fetchWikipedia(url);
			// then use termcounter to count the number of terms in each link
			TermCounter counter = new TermCounter(url);
			counter.processElements(paragraphs);
			tcListMap.put(counter.label(), counter);
                
		}
        
		//print counts before
        for(TermCounter t : tcList){
            t.printCounts();
        }

        for(String currLabel : tcListMap.keySet()){
            TermCounter t = tcList.get(currLabel);
                for(String url: t.getLinkMap().keySet()){
                    for(String otherLabel : tcListMap.keySet()){
                        TermCounter t2 = tcListMap.get(otherLabel);
                        if(!t.equals(t2)){
                            if(url.equals(otherLabel){
                                for(String term : t2.keySet()){
                                    t2.incrementTermCount(term);
                                }
                            }
                        }
                    }
                }
            }
        
        //print counts after
        for(String label : tcListMap.keySet()){
            t.printCounts();
        } 
        for(String label : tcListMap.keySet()){
            System.out.println("Indexing " + label);
            index.pushTermCounterToRedis(tcListMap.get(label));
        }
	}
}
