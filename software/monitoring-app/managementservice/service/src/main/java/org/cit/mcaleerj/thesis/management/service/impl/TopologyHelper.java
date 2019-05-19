package org.cit.mcaleerj.thesis.management.service.impl;

import com.google.common.collect.Sets;
import lombok.NonNull;
import org.cit.mcaleerj.thesis.management.domain.Edge;
import org.cit.mcaleerj.thesis.management.domain.Node;
import org.cit.mcaleerj.thesis.management.domain.Topology;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Topology merge functionality.
 */
class TopologyHelper {

  /**
   * Merge topologies.
   * @param mergeTo {@link Topology} to merge from
   * @param mergeFrom  {@link Topology} to merge to
   */
  static void mergeTopologies(@NonNull final Topology mergeTo, @NonNull final Topology mergeFrom) {
    mergeNodes(mergeTo, mergeFrom);
    mergeEdges(mergeTo, mergeFrom);
  }

  /**
   * Merge topology nodes.
   * @param mergeTo {@link Topology} to merge from
   * @param mergeFrom  {@link Topology} to merge to
   */
  private static void mergeNodes(@NonNull final Topology mergeTo, @NonNull final Topology mergeFrom) {
    //merge nodes
    mergeFrom.getNodes().forEach(mergeFromNode -> {
      boolean merged = false;
      for (Node mergeToNode : mergeTo.getNodes()) {
        if (mergeToNode.equals(mergeFromNode)) {
          merged = true;
        }
      }
      if (!merged) {
        mergeTo.getNodes().add(mergeFromNode);
      }
    });

  }

  /**
   * Merge topology edges.
   * @param mergeTo {@link Topology} to merge from
   * @param mergeFrom  {@link Topology} to merge to
   */
  private static void mergeEdges(@NonNull final Topology mergeTo, @NonNull final Topology mergeFrom) {

    final Set<String> edgeLabels = mergeFrom.getEdgeLabels();
    edgeLabels.addAll(mergeTo.getEdgeLabels());

    for (String label : edgeLabels) {
      final List<Edge> edges = mergeFrom.getEdgesWithLabel(label);
      edges.addAll(mergeTo.getEdgesWithLabel(label));

      final Set<List<Node>> cartesianProduct = getCartesianProduct(edges);
      if (cartesianProduct.isEmpty()) {
        mergeTo.getEdges().addAll(edges);
      } else {
        for(List<Node> pair : cartesianProduct) {
          final Edge edge = new Edge();
          edge.setLabel(label);
          edge.setSourceNode(pair.get(0));
          edge.setTargetNode(pair.get(1));

          List<Edge> oldEdges = mergeTo.getEdgesWithLabel(label);
          mergeTo.getEdges().removeIf((Edge oldEdge) ->
                                              oldEdge.getLabel().equals(label) &&
                                              (oldEdge.getSourceNode() == null || oldEdge.getTargetNode() == null));

          mergeTo.getEdges().add(edge);
        }
      }
    }
  }

  /**
   * Returns Cartesian product of all edge source/target nodes
   * @param edges <code>List</code> of {@link Edge} instances
   * @return cartesian product of nodes
   */
  private static Set<List<Node>> getCartesianProduct(final List<Edge> edges) {

    final Set<Node> sourceSet = new HashSet<>();
    final Set<Node> targetSet = new HashSet<>();
    for (Edge edge : edges) {
      final Node sourceNode = edge.getSourceNode();
      final Node targetNode = edge.getTargetNode();
      if (sourceNode != null) {
        sourceSet.add(sourceNode);
      }
      if (targetNode != null) {
        targetSet.add(targetNode);
      }
    }
    return Sets.cartesianProduct(sourceSet, targetSet);


  }

}
