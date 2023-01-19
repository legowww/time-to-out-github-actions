package com.quadint.app.web.service;

import com.quadint.app.domain.time.SubwayTimeDto;
import com.quadint.app.domain.time.SubwayTimeResponse;
import com.quadint.app.web.exception.TtoAppException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SubwayArrivalService {
    private static final String SERVICE_KEY = "Qmn6U2M5L3CCbVN8qFLeOCoE4m7xcYqwHz31rjcejo4";
    private static final String SUBWAY_API_URL = "https://api.odsay.com/v1/api/subwayTimeTable";

    public SubwayTimeResponse getTimeResponse(String stationId, String wayCode) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate nowDate = LocalDate.now();
        List<LocalDateTime> result = new ArrayList<>();
        SubwayTimeResponse subwayTimeResponse = SubwayTimeResponse.createSubwayTimeResponse(stationId, wayCode);
        try {
            SubwayTimeDto subwayTimeDto = getSubwayArrivalStationTime(stationId, wayCode);

            String idx = subwayTimeDto.getIdx();
            String[] list = subwayTimeDto.getList();

            for (int i = 0; i < list.length - 1; i++) {
                if (result.size() == 4)
                    break;
                if ((idx.compareTo("24") == 0) || (idx.compareTo("0") == 0)) {
                    idx = "0";
                    LocalDateTime cmp = LocalDateTime.of(nowDate, LocalTime.of(Integer.parseInt(idx), Integer.parseInt(list[i])));
                    if(result.size() != 4)
                        result.add(cmp);
                }
                if (list[i].compareTo(list[i + 1]) < 0) {
                    LocalDateTime cmp = LocalDateTime.of(nowDate, LocalTime.of(Integer.parseInt(idx), Integer.parseInt(list[i])));
                    if(now.compareTo(cmp) < 0 && (result.size() != 4))
                        result.add(cmp);
                }
                else if ((list[i].compareTo(list[i + 1]) > 0) && (i >= (list.length / 2 - 2))) {
                    LocalDateTime cmp = LocalDateTime.of(nowDate, LocalTime.of(Integer.parseInt(idx), Integer.parseInt(list[i])));
                    if(now.compareTo(cmp) < 0 && (result.size() != 4))
                        result.add(cmp);
                    idx = String.valueOf(Integer.parseInt(idx) + 1);
                }
            }
            if(result.size() != 4){
                LocalDateTime cmp = LocalDateTime.of(nowDate, LocalTime.of(Integer.parseInt(idx), Integer.parseInt(list[list.length - 1])));
                result.add(cmp);
            }
            subwayTimeResponse.setTimes(result);
            log.info("[info]subwayTimeResponse=" + subwayTimeResponse);
            return subwayTimeResponse;
        } catch (RuntimeException e) {
            throw new TtoAppException("[SubwayArrivalService.getTimeResponse]error");
        }
    }

    private SubwayTimeDto getSubwayArrivalStationTime(String stationId, String wayCode) {
        try {
            LocalDateTime now = LocalDateTime.now();
            StringBuilder url = getSubwayArrivalStationUrl(stationId, wayCode);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(url.toString());
            JSONObject result = (JSONObject) json.get("result");

            JSONObject time;
            String idx;
            String list;
            String[] replaceList1;
            JSONObject time2;
            String list2;
            String[] replaceList2;
            String[] resultList = new String[0];

            if (String.valueOf(now.getDayOfWeek()).equals("SATURDAY")) {
                JSONObject satList = (JSONObject) result.get("SatList");
                if (wayCode.equals("1")) {
                    JSONObject up = (JSONObject) satList.get("up");
                    JSONArray times = (JSONArray) up.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            replaceList1 = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            replaceList2 = splitList(list2);
                            resultList = combineList(replaceList1, replaceList2);
                        }
                        return new SubwayTimeDto(idx, resultList);
                    }
                }
                else {
                    JSONObject down = (JSONObject) satList.get("down");
                    JSONArray times = (JSONArray) down.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            replaceList1 = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            replaceList2 = splitList(list2);
                            resultList = combineList(replaceList1, replaceList2);
                        }
                        return new SubwayTimeDto(idx, resultList);
                    }
                }


            }
            else if (String.valueOf(now.getDayOfWeek()).equals("SUNDAY")) {
                JSONObject sunList = (JSONObject) result.get("SunList");
                if (wayCode.equals("1")) {
                    JSONObject up = (JSONObject) sunList.get("up");
                    JSONArray times = (JSONArray) up.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            replaceList1 = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            replaceList2 = splitList(list2);
                            resultList = combineList(replaceList1, replaceList2);
                        }
                        return new SubwayTimeDto(idx, resultList);
                    }
                }
                else {
                    JSONObject down = (JSONObject) sunList.get("down");
                    JSONArray times = (JSONArray) down.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            replaceList1 = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            replaceList2 = splitList(list2);
                            resultList = combineList(replaceList1, replaceList2);
                        }
                        return new SubwayTimeDto(idx, resultList);
                    }
                }
            }
            else {
                JSONObject ordList = (JSONObject) result.get("OrdList");
                if (wayCode.equals("1")) {
                    JSONObject up = (JSONObject) ordList.get("up");
                    JSONArray times = (JSONArray) up.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            replaceList1 = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            replaceList2 = splitList(list2);
                            resultList = combineList(replaceList1, replaceList2);
                        }
                        return new SubwayTimeDto(idx, resultList);
                    }
                }
                else {
                    JSONObject down = (JSONObject) ordList.get("down");
                    JSONArray times = (JSONArray) down.get ("time");
                    if(((now.getHour() >= 5) && (now.getHour() < 25)) || (now.getHour() == 0)) {
                        if (now.getHour() == 0) {
                            time = (JSONObject) times.get(19);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            resultList = splitList(list);
                        }
                        else {
                            time = (JSONObject) times.get(now.getHour() - 5);
                            idx = time.get("Idx").toString();
                            list = (String) time.get("list");
                            replaceList1 = splitList(list);

                            time2 = (JSONObject) times.get(now.getHour() - 5 + 1);
                            list2 = (String) time2.get("list");
                            replaceList2 = splitList(list2);
                            resultList = combineList(replaceList1, replaceList2);
                        }
                        return new SubwayTimeDto(idx, resultList);
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new TtoAppException("[SubwayArrivalService.getSubwayArrivalStationTime]IOException error");
        } catch (ParseException e) {
            throw new TtoAppException("[SubwayArrivalService.getSubwayArrivalStationTime]ParseException error");
        } catch (RuntimeException e) {
            throw new TtoAppException("[SubwayArrivalService.getSubwayArrivalStationTime]error");
        }
    }

    private StringBuilder getSubwayArrivalStationUrl(String stationId, String wayCode) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder(SUBWAY_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("apiKey", "UTF-8") + "=" + SERVICE_KEY);
        urlBuilder.append("&" + URLEncoder.encode("stationID", "UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("wayCode", "UTF-8") + "=" + URLEncoder.encode(wayCode, "UTF-8"));
        return setRequest(urlBuilder);
    }

    private StringBuilder setRequest(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb;
    }

    private static String[] splitList(String list) {
        String replaceList = list.replaceAll("[^0-9]", "");
        String[] result = new String[replaceList.length() / 2];

        for (int i = 0; i < replaceList.length() / 2; i++) {
            String a = String.valueOf(replaceList.charAt(i * 2));
            String b = String.valueOf(replaceList.charAt(i * 2 + 1));
            result[i] = a + b;
        }
        return result;
    }

    private String[] combineList(String[] list1, String[] list2) {
        List<String> l1 = new ArrayList(Arrays.asList(list1));
        List<String> l2 = new ArrayList(Arrays.asList(list2));
        l1.addAll(l2);

        String[] result = new String[l1.size()];
        result = l1.toArray(result);
        return result;
    }
}