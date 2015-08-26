package peertest;

import java.util.ArrayList;
import java.util.HashMap;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;
import peersim.core.OverlayGraph;

public class GenerateClusteredNetwork implements Control {

	private static final String PAR_DEGREE="degree";
	private static final String PAR_CLUSTERING_COEFFICIENT="clustering_coefficient";
	private static final String PAR_CLUSTER_SIZE="cluster_size";
	private static final String NETWORK_OVERLAY_PROT="overlay";
	private static final String PAR_NETWORK_SIZE="size";
	private static final String PAR_NUM_CLUSTERS="num_clusters";
	
	private final int degree;
	private final double clusteringCO;
	private final int clusterSize;
	private final int overlayPID;
	private final int netSize;
	private final int numClusters;
	private OverlayGraph overlay;
	
	public GenerateClusteredNetwork(String prefix) {
		
		degree=Configuration.getInt(prefix + "." + PAR_DEGREE);
		clusterSize=Configuration.getInt(prefix+"."+PAR_CLUSTER_SIZE);
		clusteringCO=Configuration.getDouble(prefix+"."+PAR_CLUSTERING_COEFFICIENT);
		overlayPID=Configuration.getInt(prefix+"."+ NETWORK_OVERLAY_PROT);
		netSize=Configuration.getInt(prefix+"."+ PAR_NETWORK_SIZE);
		numClusters=Configuration.getInt(prefix+"."+PAR_NUM_CLUSTERS);
		
		overlay=new OverlayGraph(overlayPID,false);
		
	}
	
	public boolean execute() {
		int index;
		Node node;
		int clusterSize;
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Node> neighbours = new ArrayList<Node>();
		HashMap<Integer, ArrayList<Node>> neighborMap = new HashMap<Integer, ArrayList<Node>>();

		clusterSize = this.netSize / this.numClusters;

		for (int i = 0; i < this.netSize; i++)
			nodes.add(Network.get(i));
		
		for(int i=0;i<this.netSize;i++){
			node = Network.get(i);
			index= i/clusterSize;
			
			for(int j=index*clusterSize;j<(index+1)*clusterSize;j++){
				
				if(index!=i && (neighborMap.get(index)==null || !neighborMap.get(index).contains(node)))
					neighbours.add(nodes.get(index));
			}
			
			neighbours.remove(node);

		
			
			for (Node n : neighbours)
				this.overlay.setEdge(i, n.getIndex());
			NetworkOverlay no=(NetworkOverlay) node.getProtocol(overlayPID);
			neighborMap.put(i,no.neighbours);			
			neighbours.clear();
		}	
			return true;
	}

}
