package com.traffgun.acc.controller;

import com.traffgun.acc.dto.graph.*;
import com.traffgun.acc.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graph")
@RequiredArgsConstructor
public class GraphController {

    private final GraphService graphService;

    @GetMapping("/{projectId}/nodes")
    public List<NodeResponse> getNodes(@PathVariable Long projectId) {
        return graphService.getAllNodes(projectId).stream().map(it -> new NodeResponse(it.getId(), it.getType(), it.getName(), it.getRole(), it.getX(), it.getY())).toList();
    }

    @PostMapping("/{projectId}/nodes")
    public NodeResponse createNode(
            @PathVariable Long projectId,
            @RequestBody AddNodeRequest node
    ) {
        var created = graphService.createNode(node, projectId);
        return new NodeResponse(created.getId(), created.getType(), created.getName(), created.getRole(), created.getX(), created.getY());
    }

    @DeleteMapping("/nodes/{id}")
    public void deleteNode(@PathVariable Long id) {
        graphService.deleteNode(id);
    }

    @GetMapping("/{projectId}/edges")
    public List<EdgeResponse> getEdges(@PathVariable Long projectId) {
        return graphService.getAllEdges(projectId).stream().map(it -> new EdgeResponse(it.getId(), it.getSource(),it.getTarget())).toList();
    }

    @PutMapping("/nodes/{id}")
    public NodeResponse updateNodePosition(
            @PathVariable Long id,
            @RequestBody UpdateNodePositionRequest request
    ) {
        var updated = graphService.updateNodePosition(id, request);
        return new NodeResponse(
                updated.getId(),
                updated.getType(),
                updated.getName(),
                updated.getRole(),
                updated.getX(),
                updated.getY()
        );
    }

    @PostMapping("/{projectId}/edges")
    public EdgeResponse createEdge(
            @PathVariable Long projectId,
            @RequestBody AddEdgeRequest edge
    ) {
        var created = graphService.createEdge(edge, projectId);
        return new EdgeResponse(created.getId(), created.getSource(), created.getTarget());
    }

    @DeleteMapping("/edges/{id}")
    public void deleteEdge(@PathVariable Long id) {
        graphService.deleteEdge(id);
    }
}