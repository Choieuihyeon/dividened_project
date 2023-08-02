package com.zerobase.dividened.scraper;

import com.zerobase.dividened.model.Company;
import com.zerobase.dividened.model.ScrapedResult;

public interface Scraper {
	Company scrapCompanyByTicker(String ticker);
	ScrapedResult scrap(Company company);

}
