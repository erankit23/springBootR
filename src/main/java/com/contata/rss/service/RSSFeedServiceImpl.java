package com.contata.rss.service;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.contata.rss.DTO.ImportDTO;
import com.contata.rss.DTO.LinkDTO;
import com.contata.rss.DTO.MediaDTO;
import com.contata.rss.dao.FeedRepository;
import com.contata.rss.dao.ImportRepository;
import com.contata.rss.dao.LinkRepository;
import com.contata.rss.dao.MediaRepository;
import com.contata.rss.dao.UrlRepository;
import com.contata.rss.model.Feed;
import com.contata.rss.model.Import;
import com.contata.rss.model.Link;
import com.contata.rss.model.Media;
import com.contata.rss.model.Url;
import com.contata.rss.utility.FeedFinder;
import com.contata.rss.utility.ListLinks;

@Component
public class RSSFeedServiceImpl implements RSSFeedService{
	
	@Autowired
	private UrlRepository urlRepository;
	
	@Autowired
	private FeedRepository feedRepository;
	
	@Autowired
	private ImportRepository importRepository;
	
	@Autowired 
	private MediaRepository mediaRepository;
	
	@Autowired 
	private LinkRepository linkRepository;

	@Override
	public Object saveFeedToDatabase(String url) {
		Set<URL> dbContent=null;
		try {
			Url urlObj = new Url();
			urlObj.setUrl(url);
			urlObj.setSourceParser("listFeeder");
			
			String html = Jsoup.connect(url).get().html();
			 dbContent = FeedFinder.search(html);
			if(dbContent != null && dbContent.size() > 0){
				System.out.println("DbContent size isssssss  :"+dbContent.size());
				Url savedUrlObj=urlRepository.save(urlObj);
				Iterator content=dbContent.iterator();
				while(content.hasNext()){
					
					String importLink =content.next().toString();
					Feed feedObj = new Feed();
					feedObj.setUrlId(savedUrlObj.getUrlId());
					feedObj.setImports(importLink);
					feedRepository.save(feedObj);
				}
				
			}
			else{
				return dbContent;
			}
			
			
			return dbContent;

		} catch (Exception e) {
			e.printStackTrace();
			return "Input URL is already parsed";
		}
	}

	@Override
	public Object saveFeederFinderToDatabase(String url) {
		Map<String,Object> response=null;
		try {
			Url urlObj = new Url();
			urlObj.setUrl(url);
			urlObj.setSourceParser("feederFinder");
			
				response = ListLinks.listLinks(url);
			
				if(response!=null && response.size()>0)
				{
					
					for (Map.Entry<String, Object> entry : response.entrySet())
							{
								Url savedUrlObj=urlRepository.save(urlObj);
								
							    if(entry.getKey().equalsIgnoreCase("Media"))
							    {
							    	
							    	for(MediaDTO mediaObj :(List<MediaDTO>)entry.getValue())
							    	{
							    		Media mediaObject =new Media();
							    		mediaObject.setUrlId(savedUrlObj.getUrlId());
							    		mediaObject.setMediaType(mediaObj.getMediaType());
							    		mediaObject.setMediaURL(mediaObj.getMediaURL());
							    		mediaRepository.save(mediaObject);
							    	}
							    }
							    if(entry.getKey().equalsIgnoreCase("Links"))
							    {
							    	
							    	for(LinkDTO linkObj :(List<LinkDTO>)entry.getValue())
							    	{
							    		Link linkObject =new Link();
							    		linkObject.setUrlId(savedUrlObj.getUrlId());
							    		linkObject.setLinkUrl(linkObj.getLink());
														    		
							    		linkRepository.save(linkObject);
							    	}
							    }
							    if(entry.getKey().equalsIgnoreCase("Imports"))
							    {
							    	
							    	for(ImportDTO importObj :(List<ImportDTO>)entry.getValue())
							    	{
							    		Import importObject =new Import();
							    		importObject.setUrlId(savedUrlObj.getUrlId());
							    		importObject.setImportLink(importObj.getImportLink());
							    		importObject.setImportCategory(importObj.getImportCategory());
							    		
							    		
							    		importRepository.save(importObject);
							    	}
							    }
							    
							}
				}
		} catch (Exception e) {
			
			e.printStackTrace();
			return "Input URL is already parsed";
		}
		return response;
	}

}
