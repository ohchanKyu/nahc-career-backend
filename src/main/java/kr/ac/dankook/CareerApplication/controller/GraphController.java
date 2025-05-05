package kr.ac.dankook.CareerApplication.controller;

import kr.ac.dankook.CareerApplication.document.GraphData;
import kr.ac.dankook.CareerApplication.repository.GraphDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/graph")
public class GraphController {

    private final GraphDataRepository graphDataRepository;

    @GetMapping("/all")
    public List<GraphData> getAllGraphData(){
        return graphDataRepository.findAll();
    }

}
