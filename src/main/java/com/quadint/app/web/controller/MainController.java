package com.quadint.app.web.controller;


import com.quadint.app.web.controller.request.LocationCoordinateRequest;
import com.quadint.app.domain.route.TimeRoute;
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
    public List<TimeRoute> getRoutes(@RequestBody LocationCoordinateRequest lc) {
        List<TimeRoute> result = routeService.calculateRoute(lc);
        return result;
    }

    @GetMapping("/test")
    @ResponseBody
    public List<TimeRoute> getRoutesDefault() {
        LocationCoordinateRequest lc = new LocationCoordinateRequest("126.6486573", "37.3908814", "126.63652", "37.37499041");
        List<TimeRoute> result = routeService.calculateRoute(lc);
        return result;
    }

    @GetMapping("/han")
    @ResponseBody
    public String han() {
        log.info("한글");
        return "한글";
    }
}
