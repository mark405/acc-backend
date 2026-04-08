package com.traffgun.acc.service;

import com.traffgun.acc.dto.graph.AddEdgeRequest;
import com.traffgun.acc.dto.graph.AddNodeRequest;
import com.traffgun.acc.dto.graph.UpdateNodeRequest;
import com.traffgun.acc.entity.Edge;
import com.traffgun.acc.entity.Node;
import com.traffgun.acc.exception.EntityNotFoundException;
import com.traffgun.acc.repository.EdgeRepository;
import com.traffgun.acc.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphService {

    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;

    @Transactional(readOnly = true)
    public List<Node> getAllNodes(Long projectId) {
        return nodeRepository.findAllByProjectId(projectId);
    }

    @Transactional
    public Node createNode(AddNodeRequest node, Long projectId) {
        return nodeRepository.save(Node.builder()
                .type(node.getType())
                .name(node.getName())
                .role(node.getRole())
                .x(node.getX())
                .y(node.getY())
                .projectId(projectId)
                .color(node.getColor())
                .build());
    }

    @Transactional
    public void deleteNode(Long id) {
        nodeRepository.deleteById(id);
        edgeRepository.deleteAllBySourceOrTarget(id, id);
    }

    @Transactional(readOnly = true)
    public List<Edge> getAllEdges(Long projectId) {
        return edgeRepository.findAllByProjectId(projectId);
    }

    @Transactional
    public Edge createEdge(AddEdgeRequest edge, Long projectId) {
        return edgeRepository.save(Edge.builder().projectId(projectId).source(edge.getSource()).target(edge.getTarget()).sourceHandle(edge.getSourceHandle()).targetHandle(edge.getTargetHandle()).build());
    }

    @Transactional
    public void deleteEdge(Long id) {
        edgeRepository.deleteById(id);
    }

    @Transactional
    public Node updateNodePosition(Long id, UpdateNodeRequest request) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        node.setX(request.getX());
        node.setY(request.getY());
        node.setColor(request.getColor());

        return nodeRepository.save(node);
    }
}