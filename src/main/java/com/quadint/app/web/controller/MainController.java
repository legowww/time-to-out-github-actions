package com.quadint.app.web.controller;


import com.quadint.app.domain.route.TimeRoute;
import com.quadint.app.web.controller.request.LocationCoordinateRequest;
import com.quadint.app.web.controller.response.Response;
import com.quadint.app.web.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final RouteService routeService;

    @PostMapping("/route")
    @ResponseBody
    public Response<List<TimeRoute>> getRoutes(@RequestBody LocationCoordinateRequest lc) {
        List<TimeRoute> result = routeService.calculateRoute(lc);
        return Response.success(result);
    }

    @GetMapping("/test")
    @ResponseBody
    public Response<List<TimeRoute>> getRoutesDefault() {
        LocationCoordinateRequest lc = new LocationCoordinateRequest("126.6800897", "37.4078889", "126.6567643", "37.3818519"); // 스퀘어앞 한양1차->송현아
        List<TimeRoute> result = routeService.calculateRoute(lc);
        return Response.success(result);
    }
}

