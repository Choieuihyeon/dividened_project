package com.zerobase.dividened.scheduler;

import static com.zerobase.dividened.model.constrants.CacheKey.KEY_FINANCE;

import com.zerobase.dividened.model.Company;
import com.zerobase.dividened.model.ScrapedResult;
import com.zerobase.dividened.persist.CompanyRepository;
import com.zerobase.dividened.persist.DividendRepository;
import com.zerobase.dividened.persist.entity.CompanyEntity;
import com.zerobase.dividened.persist.entity.DividendEntity;
import com.zerobase.dividened.scraper.Scraper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@EnableCaching
public class ScraperScheduler {

	private final CompanyRepository companyRepository;
	private final Scraper yahooFinanceScraper;
	private final DividendRepository dividendRepository;

//	@Scheduled(fixedDelay = 1000)
//	public void test1() throws InterruptedException {
//		Thread.sleep(10000);
//		System.out.println(Thread.currentThread().getName() + " -> 테스트 1 : " + LocalDateTime.now());
//	}
//
//	@Scheduled(fixedDelay = 1000)
//	public void test2() throws InterruptedException {
//		System.out.println(Thread.currentThread().getName() + " -> 테스트 2 : " + LocalDateTime.now());
//	}

	// 일정 주기마다 수행
	@CacheEvict(value = KEY_FINANCE, allEntries = true)
	@Scheduled (cron = "${scheduler.scrap.yahoo}")
	public void yahooFinanceScheduling() {
		log.info("scraping scheduler is started");
		// 저장된 회사 목록을 조회
		List<CompanyEntity> companies = this.companyRepository.findAll();

		// 회사마다 배당금 정보를 새로 스크래핑
		for (var company : companies) {
			log.info("scraping scheduler is started -> " + company.getName());
			ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
				new Company(company.getTicker(), company.getName()));

			// 스크래핑된 배당금 정보 중 데이터베이스에 없는 값은 저장
			scrapedResult.getDividends().stream()
				// dividend 모델을 dividendEntity로 매핑
				.map(e -> new DividendEntity(company.getId(), e))
				// 엘리먼트를 하나씩 디비든 레파지토리에 삽입
				.forEach(e -> {
					boolean exists = this.dividendRepository.existsByCompanyIdAndDate(
						e.getCompanyId(), e.getDate());
					if (!exists) {
						this.dividendRepository.save(e);
						log.info("insert new dividend -> " + e.toString());
					}
				});

			// 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
			try {
				Thread.sleep(3000); // 3 seconds 간 정지
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
