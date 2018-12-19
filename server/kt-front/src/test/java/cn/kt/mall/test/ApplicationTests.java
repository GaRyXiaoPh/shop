package cn.kt.mall.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.kt.mall.common.util.RedUtil;
import cn.kt.mall.common.util.RedisUtil;
import cn.kt.mall.shop.city.entity.CityEntity;
import cn.kt.mall.shop.city.service.CityService;
import cn.kt.mall.shop.logistics.service.LogisticsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private CityService cityService;
	//@Autowired
	//private RedPaperService redPaperService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private LogisticsService logisticsService;
	
//	@Test
//	public void subscribeLogisticsInfoTest() {
//		LogisticsSubReqVO logisticsSubReqVO = new LogisticsSubReqVO();
//		logisticsSubReqVO.setCompany("yuantong");
//		logisticsSubReqVO.setNumber("889338340377025821");
//		System.out.println(logisticsService.subscribeLogisticsInfo(logisticsSubReqVO));
//	}

//	@Test
//	public void redisTest() {
//		long rows = redisUtil.incr(RedisConstants.RED_PAPER_KEY + "123", RedisConstants.EXPIRE);
//		System.out.println(rows);
//	}

//	@Test
//	public void sendRedTest() {
//		redPaperService.sendRedPackage("721075d6-0051-44cd-bb01-b36fa6762dfd", "721075d6-0051-44cd-bb01-b36fa6762dfd",
//				1000, 10, "大吉大利", (short) 2);
//	}
	
	@Test
	public void createJson() {
		List<CityEntity> list = cityService.getAllCityList();
		createCityList(list);
		createAreaList(list);
		createAndroidAreaList(list);
		createWebAreaList(list);
	}

	public void createCityList(List<CityEntity> list) {
		try {
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter("F:/city.plist", true);

			for (CityEntity proEntity : list) {
				String proStr = String.format(
						"<dict><key>code</key><integer>%d</integer><key>name</key><string>%s</string><key>alone</key>"
								+ "<integer>1</integer><key>cities</key><array>",
						proEntity.getSid(), proEntity.getName());
				fw.write(proStr);
				fw.flush();
				for (CityEntity cityEntity : proEntity.getCityList()) {
					String cityStr = String.format(
							"<dict><key>code</key><integer>%d</integer>" + "<key>name</key><string>%s</string>"
									+ "<key>province_code</key><integer>%d</integer></dict>",
							cityEntity.getSid(), cityEntity.getName(), proEntity.getSid());
					fw.write(cityStr);
					fw.flush();
				}
				String endStr = "</array></dict>";
				fw.write(endStr);
				fw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createAreaList(List<CityEntity> list) {
		try {
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter("F:/area.plist", true);
			for (CityEntity proEntity : list) {
				for (CityEntity cityEntity : proEntity.getCityList()) {
					String cityStr = String.format("<key>%d</key><array>", cityEntity.getSid());
					fw.write(cityStr);
					fw.flush();
					for (CityEntity countyEntity : cityEntity.getCountyList()) {
						String countyStr = String.format(
								"<dict><key>code</key><integer>%d</integer>" + "<key>name</key><string>%s</string>"
										+ "<key>city_code</key><integer>%d</integer></dict>",
								countyEntity.getSid(), countyEntity.getName(), cityEntity.getSid());
						fw.write(countyStr);
						fw.flush();
					}
					String endStr = "</array>";
					fw.write(endStr);
					fw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createAndroidAreaList(List<CityEntity> list) {
		try {
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter("F:/province_data.xml", true);
			for (CityEntity proEntity : list) {
				String proStr = String.format(
						"<dict><key>code</key><integer>%d</integer><key>name</key><string>%s</string><key>alone</key>"
								+ "<integer>1</integer><key>cities</key><array>",
						proEntity.getSid(), proEntity.getName());
				fw.write(proStr);
				fw.flush();

				for (CityEntity cityEntity : proEntity.getCityList()) {
					String cityStr = String.format("<key>%d</key><array>", cityEntity.getSid());
					fw.write(cityStr);
					fw.flush();
					for (CityEntity countyEntity : cityEntity.getCountyList()) {
						String countyStr = String.format(
								"<dict><key>code</key><integer>%d</integer>" + "<key>name</key><string>%s</string>"
										+ "<key>city_code</key><integer>%d</integer></dict>",
								countyEntity.getSid(), countyEntity.getName(), cityEntity.getSid());
						fw.write(countyStr);
						fw.flush();
					}
					String endStr = "</array>";
					fw.write(endStr);
					fw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createWebAreaList(List<CityEntity> list) {
		try {
			@SuppressWarnings("resource")
			FileWriter fw = new FileWriter("F:/web_data.json", true);
			fw.write("[");
			fw.flush();

			for (int p = 0; p < list.size(); p++) {
				String proStr = String.format("{value: %d, label: '%s', children: [", list.get(p).getSid(),
						list.get(p).getName());
				fw.write(proStr);
				fw.flush();

				for (int c = 0; c < list.get(p).getCityList().size(); c++) {
					String cityStr = String.format("{value: %d, label: '%s', children: [",
							list.get(p).getCityList().get(c).getSid(), list.get(p).getCityList().get(c).getName());
					fw.write(cityStr);
					fw.flush();

					for (int i = 0; i < list.get(p).getCityList().get(c).getCountyList().size(); i++) {

						String countyStr = String.format("{value: %d, label: '%s'}",
								list.get(p).getCityList().get(c).getCountyList().get(i).getSid(),
								list.get(p).getCityList().get(c).getCountyList().get(i).getName());
						fw.write(countyStr);
						fw.flush();
						if (i != list.get(p).getCityList().get(c).getCountyList().size() - 1) {
							fw.write(",");
							fw.flush();
						}
					}

					fw.write("]}");
					fw.flush();
					if (c != list.get(p).getCityList().size() - 1) {
						fw.write(",");
						fw.flush();
					}
				}

				fw.write("]}");
				fw.flush();
				if (p != list.size() - 1) {
					fw.write(",");
					fw.flush();
				}
			}
			fw.write("]");
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int[] amounts = RedUtil.generate(190000, 100);
		int total = 0;
		for (int l : amounts) {
			total += l;
			System.out.println(l);
		}
		System.out.println(total);
	}
}
