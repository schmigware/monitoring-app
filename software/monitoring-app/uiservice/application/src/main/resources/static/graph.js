findOrCreateGraphNode = function(graph, nodeName) {
    for(var i=0; i < graph.nodes.length; i++) {
        var node = graph.nodes[i];
        if (node.name === nodeName) {
            return node;
        }
    }
    var newNode = {isSource: false, isSink: false};
    newNode.name = nodeName;
    return newNode;
};

formatDate = function(date) {
    return [date.getDate(), date.getMonth() + 1, date.getFullYear()].join('/') + ' ' +
                  [date.getHours(), date.getMinutes(), date.getSeconds()].join(':');
};

renderAggregationWindow = function(aggregation) {
    var windowEnd = aggregation.windowEnd;
    var windowStart = windowEnd - aggregation.windowSize;

    var startDate = new Date(windowStart);
    var endDate = new Date(windowEnd);

    var windowStr = formatDate(startDate) + " - " + formatDate(endDate);
    var snapshotTimeDiv = document.getElementById("snapshotTimeDiv");
    snapshotTimeDiv.innerHTML = windowStr;
};

toGraph = function(topology) {

    var nodes = topology.nodes;
    //add flags to existing nodes
    for(var i=0; i < nodes.length; i++) {
        var node = nodes[i];
        node.isSource = false;
        node.isSink = false;
    }
    //process edges, add source/sink nodes where necessaey
    var topologyEdges = topology.edges;
    for(var j=0; j < topologyEdges.length; j++) {
        var edge = topologyEdges[j];
        if(!edge.hasOwnProperty("source")) {
            var sourceName = "((" + edge.label + "))";
            var sourceNode = findOrCreateGraphNode(topology, sourceName);
            sourceNode.isSource = true;
            topology.nodes.push(sourceNode);
            edge.source = sourceName;
        } else  if(!edge.hasOwnProperty("target")) {
            var sinkName = "((" + edge.label + "))";
            var sinkNode = findOrCreateGraphNode(topology, sinkName);
            sinkNode.isSink = true;
            topology.nodes.push(sinkNode);
            edge.target = sinkName;
        }
    }
    return topology;
};

renderAggregation = function(aggregation) {

    renderAggregationWindow(aggregation);

    console.log("Rendering aggregation");
    for(var i=0; i < aggregation.edgeSnapshots.length; i++) {
        var snapshot = aggregation.edgeSnapshots[i];
        var edgeLabel = snapshot.edgeLabel;
        var sourceNode = snapshot.sourceNode;
        if (sourceNode == null) {
            sourceNode = "((" + edgeLabel + "))";
        }
        var count = snapshot.messageCount;

        var edgeLabelId =  "edge-" + edgeLabel + "-" + sourceNode;
        var labelDiv = document.getElementById(edgeLabelId);
        if (labelDiv != null) {
            labelDiv.innerHTML = count + "/s";
        } else {
            console.log("Unable to find label element " + edgeLabelId);
        }
    }

    console.log(JSON.stringify(aggregation));
};

renderGraph = function(graph) {

    // Set up zoom support
    var svg = d3.select("svg"),
        inner = svg.select("g"),
        zoom = d3.zoom().on("zoom", function() {
            inner.attr("transform", d3.event.transform);
        });
    svg.call(zoom);

    var render = new dagreD3.render();

    // Left-to-right layout
    var g = new dagreD3.graphlib.Graph();
    g.setGraph({
                   nodesep: 70,
                   ranksep: 50,
                   rankdir: "LR",
                   marginx: 20,
                   marginy: 20,
                   ranker: "network-simplex",
                   acyclicer: undefined
               });

    function draw(isUpdate) {

        // add nodes
        for(var i=0; i < graph.nodes.length; i++) {

            var node = graph.nodes[i];
            var id = node.name;

            var nodeConfig = {
                rx: 5,
                ry: 5,
                label: "",
                shape: "rect"
            }

            if(node.isSource) {
                nodeConfig.shape = "circle"
            } else if(node.isSink) {
                nodeConfig.shape = "diamond";
            } else {
                var html = "<div>";
                html += id;
                html += "</div>";
                nodeConfig.labelType = "html";
                nodeConfig.label = html;
            }

            g.setNode(id, nodeConfig);

        }

        // add edges
        for(var j=0; j < graph.edges.length; j++) {
            var edge = graph.edges[j];
            var labelHtml = "<div class='edgeLabelContainer'><div class='edgeLabel'>";
            labelHtml += edge.label;
            labelHtml += "</div>";
            labelHtml += "<div class='edgeActivity' id=edge-" + edge.label + "-" + edge.source +">";
            labelHtml += "0/s";
            labelHtml += "</div></div>";
            g.setEdge(edge.source, edge.target, {
                labelType: "html",
                label: labelHtml,
                width: 65,
                minlen: 1
            });
        }


        inner.call(render, g);

        // Zoom and scale to fit
        var graphWidth = g.graph().width + 80;
        var graphHeight = g.graph().height + 40;
        var width = parseInt(svg.style("width").replace(/px/, ""));
        var height = parseInt(svg.style("height").replace(/px/, ""));
        var zoomScale = Math.min(width / graphWidth, height / graphHeight);
        var translateX = (width / 2) - ((graphWidth * zoomScale) / 2)
        var translateY = (height / 2) - ((graphHeight * zoomScale) / 2);
        var svgZoom = isUpdate ? svg.transition().duration(500) : svg;
        svgZoom.call(zoom.transform, d3.zoomIdentity.translate(translateX, translateY).scale(zoomScale));
    }

    document.addEventListener("DOMContentLoaded", draw);
    draw(true);
    return g;

};

