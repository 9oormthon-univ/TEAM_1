package today.seasoning.seasoning.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.notification.service.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolarTermUtil {

	private final NotificationService notificationService;

	@Value("${OPEN_API_KEY}")
	private String API_KEY;

	@Getter
	private static int currentYear;


	/*
	 * 기록장이 열린 경우 : 열린 절기장에 해당하는 절기의 순번 [1, 24]
	 * 기록장이 열리지 않은 경우 : -1 반환
	 * */
	@Getter
	private static int currentTerm;

	private final Map<String, Integer> solarTerms = Map.ofEntries(
		Map.entry("입춘", 1),
		Map.entry("우수", 2),
		Map.entry("경칩", 3),
		Map.entry("춘분", 4),
		Map.entry("청명", 5),
		Map.entry("곡우", 6),
		Map.entry("입하", 7),
		Map.entry("소만", 8),
		Map.entry("망종", 9),
		Map.entry("하지", 10),
		Map.entry("소서", 11),
		Map.entry("대서", 12),
		Map.entry("입추", 13),
		Map.entry("처서", 14),
		Map.entry("백로", 15),
		Map.entry("추분", 16),
		Map.entry("한로", 17),
		Map.entry("상강", 18),
		Map.entry("입동", 19),
		Map.entry("소설", 20),
		Map.entry("대설", 21),
		Map.entry("동지", 22),
		Map.entry("소한", 23),
		Map.entry("대한", 24));

	@PostConstruct
	private void initialize() {
		currentTerm = findCurrentTerm(); // 실제 절기 순번
		currentYear = LocalDate.now().getYear();
		log.info("절기 초기화 : " + currentTerm + " / 연도 초기화 : " + currentYear);

		// 해커톤 기간 동안, 입동으로 고정
		currentTerm = 19;
	}

	/* 매일 자정에 절기 갱신 */
	@Scheduled(cron = "1 0 0 * * ?")
	private void updateTerm() {
		int todayTerm = findCurrentTerm(); // 실제 절기 순번
		currentTerm = todayTerm;
		log.info("절기 갱신 : " + todayTerm);

		if (todayTerm != currentTerm) {
			notificationService.registerArticleOpenNotification(todayTerm);
		}

		// 해커톤 기간 동안, 입동으로 고정
		currentTerm = 19;
	}

	/* 매년 1월 1일에 연도 갱신 */
	@Scheduled(cron = "1 0 0 1 1 ?")
	private void updateYear() {
		currentYear = LocalDate.now().getYear();
		log.info("연도 갱신 : " + currentYear);
	}

	private int findCurrentTerm() {
		LocalDate currentDate = LocalDate.now();
		String year = String.valueOf(currentDate.getYear());
		String month = String.format("%02d", currentDate.getMonthValue());

		MonthTerms monthTerms = findMonthTerms(year, month);

		long firstTermDiff = ChronoUnit.DAYS.between(monthTerms.firstDate, currentDate);
		long secondTermDiff = ChronoUnit.DAYS.between(currentDate, monthTerms.secondDate);

		if (Math.abs(firstTermDiff) < 3) {
			return solarTerms.get(monthTerms.firstTerm);
		}

		if (Math.abs(secondTermDiff) < 3) {
			return solarTerms.get(monthTerms.secondTerm);
		}

		return -1;
	}

	private MonthTerms findMonthTerms(String year, String month) {
		try {
			String xmlString = getXmlString(year, month);
			return parseMonthTerms(xmlString);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "절기 조회 실패");
		}
	}

	private String getXmlString(String year, String month) throws Exception {
		URL url = getUrl(year, month);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");

		BufferedReader rd = (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) ?
			new BufferedReader(new InputStreamReader(conn.getInputStream())) :
			new BufferedReader(new InputStreamReader(conn.getErrorStream()));

		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		return sb.toString();
	}

	private MonthTerms parseMonthTerms(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		ByteArrayInputStream input = new ByteArrayInputStream(
			xmlString.getBytes(StandardCharsets.UTF_8));
		Document doc = builder.parse(input);
		NodeList itemList = doc.getElementsByTagName("item");

		Element firstTerm = (Element) itemList.item(0);
		Element secondTerm = (Element) itemList.item(1);

		return new MonthTerms(
			firstTerm.getElementsByTagName("dateName").item(0).getTextContent(),
			firstTerm.getElementsByTagName("locdate").item(0).getTextContent(),
			secondTerm.getElementsByTagName("dateName").item(0).getTextContent(),
			secondTerm.getElementsByTagName("locdate").item(0).getTextContent());
	}

	private URL getUrl(String year, String month) throws MalformedURLException {
		StringBuilder urlBuilder = new StringBuilder();

		// API 주소
		urlBuilder.append(
			"http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/get24DivisionsInfo");

		// API 키
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "="
			+ API_KEY);

		// 조회 년도
		urlBuilder.append(
			"&" + URLEncoder.encode("solYear", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(
				year, StandardCharsets.UTF_8));

		// 조회 월
		urlBuilder.append(
			"&" + URLEncoder.encode("solMonth", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(
				month, StandardCharsets.UTF_8));

		// 고정값
		urlBuilder.append(
			"&" + URLEncoder.encode("kst", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("0000",
				StandardCharsets.UTF_8));
		urlBuilder.append("&" + URLEncoder.encode("sunLongitude", StandardCharsets.UTF_8) + "="
			+ URLEncoder.encode("285", StandardCharsets.UTF_8));
		urlBuilder.append(
			"&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(
				"10", StandardCharsets.UTF_8));
		urlBuilder.append(
			"&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1",
				StandardCharsets.UTF_8));
		urlBuilder.append(
			"&" + URLEncoder.encode("totalCount", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(
				"210114", StandardCharsets.UTF_8));

		return new URL(urlBuilder.toString());
	}

	/*
	 * 조회한 달의 첫번째 절기와 두번째 절기 정보
	 * term : 절기 한글 이름
	 * date : 절기 날짜
	 * */
	@Getter
	private class MonthTerms {

		private String firstTerm;
		private String secondTerm;

		private LocalDate firstDate;
		private LocalDate secondDate;

		public MonthTerms(String firstTerm, String firstDate, String secondTerm,
			String secondDate) {
			this.firstTerm = firstTerm;
			this.firstDate = LocalDate.parse(firstDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

			this.secondTerm = secondTerm;
			this.secondDate = LocalDate.parse(secondDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
		}
	}

}
