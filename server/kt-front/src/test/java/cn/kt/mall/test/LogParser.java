package cn.kt.mall.test;

import cn.kt.mall.common.util.DateUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LogParser {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static void main(String[] args) throws IOException, ParseException {
        File file = new File("D:\\logs\\front\\logs");

        List<LineResult> lineResultList = new ArrayList<>();

        Map<String, Integer> url2Counts = new HashMap<>();
        Map<Long, Integer> second2Counts = new HashMap<>();

        for (File f : file.listFiles()) {

            Date requestStartTime = null;
            String requestUrl = null;
            String parameters = null;

            int requestStart = 0;
            for (String line : Files.lines(Paths.get(f.getPath())).collect(Collectors.toList())) {
                if (line.contains("http-nio-8000")) {
                    String[] times = line.split(" ");
                    requestStartTime = parseDate(times[0] +  " " +times[1]);
                    requestStart = 0;
                    requestUrl = null;
                } else {
                    ++ requestStart;
                }


                if ( requestStart == 2) {
                    requestUrl = line;
                } else if ( requestStart == 4) {
                    parameters = line;
                }

                if (line.contains("user times")) {
                    LineResult lineResult = new LineResult();

                    String elapsedTimeString = line.split(":")[1];
                    lineResult.requestUrl= requestUrl;
                    lineResult.startTime = requestStartTime;
                    lineResult.elapsedTime = Integer.valueOf(elapsedTimeString.substring(0, elapsedTimeString.length() - 2));
                    lineResult.parameters = parameters;

                    lineResultList.add(lineResult);
                    increase(url2Counts, requestUrl);
                    increase(second2Counts, lineResult.startTime.getTime()/ 1000l);
                }
            }
        }

        List<RequestStats> urlStats = new ArrayList<>();
        for (String url : url2Counts.keySet()) {
            urlStats.add(new RequestStats(url, null, url2Counts.get(url)));
        }

        urlStats.sort(new Comparator<RequestStats>() {
            @Override
            public int compare(RequestStats o1, RequestStats o2) {
                return o2.counts - o1.counts;
            }
        });

        List<RequestStats> secondStats = new ArrayList<>();
        for (Long second : second2Counts.keySet()) {
            secondStats.add(new RequestStats(null, second, second2Counts.get(second)));
        }

        secondStats.sort(new Comparator<RequestStats>() {
            @Override
            public int compare(RequestStats o1, RequestStats o2) {
                return o2.counts - o1.counts;
            }
        });

        System.out.println("top 10 QPS: ");
        for (int i = 0 ; i < 10; ++i) {
            System.out.println(DateUtil.getTime(new Date(secondStats.get(i).requestTime * 1000l)) + ": " + secondStats.get(i).counts);
        }

        System.out.println("top 10 request: ");
        for (int i = 0 ; i < 10; ++i) {
            System.out.println(urlStats.get(i).requestUrl + ": " + urlStats.get(i).counts);
        }
    }

    private static <T> void increase(Map<T, Integer> map, T url) {
        Integer counts = map.get(url);
        if (counts == null) {
            counts = new Integer(1);
        }

        map.put(url, ++counts);
    }

    private static class LineResult {
        public String requestUrl;
        public Integer elapsedTime;
        public Date startTime;
        public String parameters;
    }

    private static class RequestStats {
        public String requestUrl;
        public Long requestTime;
        public Integer counts;

        public RequestStats(String requestUrl, Long requestTime, Integer counts) {
            this.requestUrl = requestUrl;
            this.requestTime = requestTime;
            this.counts = counts;
        }
    }

    private static Date parseDate(String time) throws ParseException {
        return sdf.parse(time);
    }

}
