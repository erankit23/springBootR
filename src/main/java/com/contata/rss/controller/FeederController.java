package com.contata.rss.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.contata.rss.DTO.InputUrlDTO;
import com.contata.rss.service.RSSFeedService;
import com.contata.rss.utility.FeedFinder;
import com.contata.rss.utility.ListLinks;

@Controller
public class FeederController {
	
	@Autowired
	RSSFeedService service;
	
	@RequestMapping("/")
	public String home(@ModelAttribute InputUrlDTO urlObject,Model model) {
		model.addAttribute("urlObject", urlObject);
		/*model.addAttribute("inputUrl", "helloURL");*/
		return "welcome";
	}
	
	@PostMapping(value="/savelistFeeder")
	public ResponseEntity savelistFeeder(@ModelAttribute InputUrlDTO urlObject,BindingResult bindingResult,
			Model model) {
		Object response = null;
		System.out.println(urlObject);
		System.out.println("URL ============> "+urlObject.getInputUrl());
		String url=urlObject.getInputUrl();
		if (url == null || url.equals("")){
			response = service.saveFeedToDatabase("http://www.feedforall.com/sample-feeds.htm");
		}
		else {
			response=service.saveFeedToDatabase(url);
		}
	
			
		return new ResponseEntity(response, HttpStatus.OK);
	}

	/*@RequestMapping(value="/savelistFeeder")
	public ResponseEntity savelistFeeder(@RequestParam(value = "url", required = false, defaultValue = "") String url,
			Model model) {
		Object response = null;
		System.out.println("URL ============> "+url);
		if (url == null || url.equals("")){
			response = service.saveFeedToDatabase("http://www.feedforall.com/sample-feeds.htm");
		}
		else {
			response=service.saveFeedToDatabase(url);
		}
	
			
		return new ResponseEntity(response, HttpStatus.OK);
	}*/
	
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveFeederFinder")
	public ResponseEntity saveFeederFinder(@RequestParam(value = "url", required = false) String url, Model model) {
		Object response = null;
		try {
			if (url == null || url.equals(""))
				response=service.saveFeederFinderToDatabase("http://news.ycombinator.com/");
				
			else
				response=service.saveFeederFinderToDatabase(url);
		} catch (Exception e) {

			e.printStackTrace();
			
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}*/
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/saveFeederFinder")
	public ResponseEntity saveFeederFinder(@ModelAttribute InputUrlDTO urlObject,BindingResult bindingResult , Model model) {
		Object response = null;
		try {
			String url=urlObject.getInputUrl();
			if (url == null || url.equals(""))
				response=service.saveFeederFinderToDatabase("http://news.ycombinator.com/");
				
			else
				response=service.saveFeederFinderToDatabase(url);
		} catch (Exception e) {

			e.printStackTrace();
			
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

}
