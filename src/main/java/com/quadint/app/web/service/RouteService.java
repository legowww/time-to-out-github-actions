package com.quadint.app.web.service;

import com.quadint.app.domain.route.TimeRoute;
import com.quadint.app.domain.time.BusTimeResponse;
import com.quadint.app.domain.Time;
import com.quadint.app.web.controller.request.LocationCoordinateRequest;
import com.quadint.app.domain.route.Route;
import com.quadint.app.domain.transportation.Bus;
import com.quadint.app.domain.transportation.TransportationType;
import com.quadint.app.domain.transportation.Walk;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteService {
    private static final String SERVICE_KEY = "Qmn6U2M5L3CCbVN8qFLeOCoE4m7xcYqwHz31rjcejo4";
    private static final String ROUTE_API_URL = "https://api.odsay.com/v1/api/searchPubTransPathT";
    private final BusArrivalService busArrivalService;

    public List<TimeRoute> addOutingTime(List<Route> routes) {
        List<TimeRoute> timeRoutes = new ArrayList<>();

        for (Route route : routes) {
            List<String> t = route.getFirstTransportation();
            String transportationType = t.get(0);

            /**
             * firstBstopId=출발지에서 처음만나는 정류장ID
             * firstRouteId=출발지에서 처음만는 정류장에서 타는 버스ID
             * walkTimeMin=출발지에서 처음만나는 정류장까지의 걷는시간(분)
             */
            if (transportationType == TransportationType.BUS.name()) {
                String firstBstopId = t.get(1);
                String firstRouteId = t.get(2);
                int walkTimeMinutes = Integer.parseInt(t.get(3));
                BusTimeResponse r = busArrivalService.getTimeResponse(firstBstopId, firstRouteId); //도착예정 시간 3개 도출
                for (int i = 0; i < r.getTimeSize(); i++) {
                    Time time = r.getTime(i);
                    LocalDateTime busArrivalTime = time.getTime(); //버스 도착시간
                    LocalDateTime timeToGo = busArrivalTime.minusMinutes(walkTimeMinutes + 2); //나갈시간 = 버스 도착시간 - (정류장까지 걷는시간 + 보정시간(2))
                    TimeRoute timeRoute = TimeRoute.createTimeRoute(timeToGo, route);
                    timeRoutes.add(timeRoute);
                }
            }
            //todo: 집에서 처음만나는 장소가 지하철인 경우 추가
            else if (transportationType == TransportationType.SUBWAY.name()) {

            }
            else if (transportationType == TransportationType.WALK.name()) {

            }
        }
        Collections.sort(timeRoutes); //나갈시간이 빠른 순서대로 정렬

        List<TimeRoute> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < timeRoutes.size(); ++i) {
            TimeRoute tr = timeRoutes.get(i);
            if (tr.getTime().compareTo(now) != -1) {
                result.add(tr); //현재 시간보다 이후인 시간대만 반환값에 추가
            }
        }
        return result;
    }

    public List<TimeRoute> calculateRoute(LocationCoordinateRequest lc) {
        List<Route> routes = new ArrayList<>();
        try {
            StringBuilder url = getRouteURL(lc);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(url.toString());
            JSONObject result = (JSONObject) json.get("result");
            JSONArray paths = (JSONArray) result.get("path");

            for (int i = 0; i < paths.size(); ++i) {
                JSONObject path = (JSONObject) paths.get(i);
                JSONObject info = (JSONObject) path.get("info");

                String firstStartStation = info.get("firstStartStation").toString();
                String lastEndStation = info.get("lastEndStation").toString();
                String busTransitCount = info.get("busTransitCount").toString();
                String subwayTransitCount = info.get("subwayTransitCount").toString();
                int totalTime = Integer.parseInt(info.get("totalTime").toString());

                Route route = new Route(busTransitCount, subwayTransitCount, firstStartStation, lastEndStation);
                JSONArray subPath = (JSONArray) path.get("subPath");
                for (int j = 0; j < subPath.size(); ++j) {
                    JSONObject sp = (JSONObject) subPath.get(j);
                    TransportationType transportationType = getTrafficType(Integer.parseInt(sp.get("trafficType").toString()));
                    int sectionTime = Integer.parseInt(sp.get("sectionTime").toString());

                    if (transportationType == TransportationType.WALK) {
                        if (sectionTime == 1) {
                            totalTime -= 1;
                            continue;
                        }
                        Walk walk = new Walk(sectionTime);
                        route.addTransportation(walk);
                    }
                    else if (transportationType == TransportationType.BUS) {
                        String startName = sp.get("startName").toString();
                        String endName = sp.get("endName").toString();
                        String startLocalStationID = sp.get("startLocalStationID").toString();
                        String endLocalStationID = sp.get("endLocalStationID").toString();
                        JSONArray lane = (JSONArray) sp.get("lane");
                        JSONObject lane_bus = (JSONObject) lane.get(0);
                        String busNum = lane_bus.get("busNo").toString();
                        String busId = lane_bus.get("busLocalBlID").toString();
                        Bus bus = new Bus(sectionTime, busId, busNum, startLocalStationID, startName, endLocalStationID, endName);
                        route.addTransportation(bus);
                    }
                    else if (transportationType == TransportationType.SUBWAY) {

                    }
                }

                route.setTotalTime(totalTime);
                routes.add(route);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(routes);

        List<Route> optimal = List.of(routes.get(0), routes.get(1), routes.get(2)); //목적지까지의 도착 소요시간이 적게 걸리는 상위 3개의 경로 추출
        List<TimeRoute> result = addOutingTime(optimal); //각 경로에 해당하는 첫 번째(정류장, 역)까지의 걷는 시간 추가
        return result;
    }

    private StringBuilder getRouteURL(LocationCoordinateRequest lc) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(ROUTE_API_URL);
        urlBuilder.append("?" + URLEncoder.encode("apiKey","UTF-8") + "=" + URLEncoder.encode(SERVICE_KEY, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("SX","UTF-8") + "=" + URLEncoder.encode(lc.getSx(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("SY","UTF-8") + "=" + URLEncoder.encode(lc.getSy(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("EX","UTF-8") + "=" + URLEncoder.encode(lc.getEx(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("EY","UTF-8") + "=" + URLEncoder.encode(lc.getEy(), "UTF-8"));
        return setRequest(urlBuilder);
    }

    private StringBuilder setRequest(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "text/xml");
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

    private TransportationType getTrafficType(int type) {
        if (type == 1) return TransportationType.SUBWAY;
        else if (type == 2) return TransportationType.BUS;
        else return TransportationType.WALK;
    }
}
