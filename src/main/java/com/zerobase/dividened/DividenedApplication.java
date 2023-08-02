package com.zerobase.dividened;

import com.zerobase.dividened.model.Company;
import com.zerobase.dividened.scraper.Scraper;
import com.zerobase.dividened.scraper.YahooFinanceScraper;
import java.io.IOException;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividenedApplication {

	public static void main(String[] args) {
		SpringApplication.run(DividenedApplication.class, args);

//		Trie trie = new PatriciaTrie();
//		AutoComplete autoComplete = new AutoComplete(trie);
//		AutoComplete autoComplete1 = new AutoComplete(trie);
//
//		autoComplete.add("hello");
//
//		System.out.println(autoComplete.get("hello"));
//		System.out.println(autoComplete1.get("hello"));

//		for (int i = 0; i < 10; i++) {
//			System.out.println("HELLO ->" + i);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//			}
//		}
	}
}
