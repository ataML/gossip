package peertest;

import java.util.Random;
import java.util.Vector;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.OverlayGraph;
import peersim.graph.Graph;
import peersim.reports.Clustering;

public class GenerateModularNetwork implements Control{

	private static final String PAR_BETA = "beta";
	private static final String PAR_DEGREE = "k";
	//private static final String PAR_INTER_MODULAR="p_inter";
	private static final String PAR_CLUSTER_SIZE="cluster_size";
	private static final String OVERLAY_PROTOCOL="overlay_protocol";
	//k is the degree of each node in watts-strogatz network
	private final int k;
	//beta is rewiring probability
	private final double beta;
	//private final double pInter;
	private final int overlayPID;
	private final int clusterSize;
	private OverlayGraph overlay;

	public GenerateModularNetwork(String prefix) {
		
		k=Configuration.getInt(prefix+"."+ PAR_DEGREE);
		beta=Configuration.getDouble(prefix+"."+PAR_BETA);
		//pInter=Configuration.getDouble(prefix+"."+PAR_INTER_MODULAR);
		overlayPID=Configuration.getPid(prefix+"."+OVERLAY_PROTOCOL);
		clusterSize=Configuration.getInt(prefix+"."+PAR_CLUSTER_SIZE);
		overlay= new OverlayGraph(overlayPID,false);
	}
	
	public boolean execute() {
		wireModularGraph();
		return true;
	}
	
	private void wireModularGraph(){
		Vector<Edge> edges=new Vector<Edge>();
		final int m = Network.size();
		Random r= new Random();
		int numClusters=m/clusterSize;
		final int n=clusterSize;
		for(int kk=0;kk<numClusters;kk++){
			for(int i=0; i<n; ++i){
				for(int j=-k/2; j<=k/2; ++j)
				{
					if( j==0 ) continue;
					int newedge = (i+j+n)%n;
					if( r.nextDouble() < beta )
					{
						newedge = r.nextInt(n-1);
						if( newedge >= i ) newedge++; // random _other_ node
					}
					edges.add(new Edge(i+kk*clusterSize,newedge+kk*clusterSize));
				}
			}
		}
		int numIntraLinks=20;
		for(int kk=0;kk<numClusters;kk++){
			for(int i=kk+1;i<numClusters;i++){
				for(int j=0;j<numIntraLinks;j++){
					//node selected from the current cluster
					int n1=r.nextInt(clusterSize);
					//node selected from the other clusters
					int n2=r.nextInt(clusterSize);
					edges.add(new Edge(n1+kk*clusterSize,n2+i*clusterSize));
				}
			}
		}
		for(int i=0;i<edges.size();i++){
			Edge e=edges.get(i);
			this.overlay.setEdge(e.u, e.v);
		}
	}

}
 class Edge{
	 int u,v;
	 public Edge(int u,int v) {
		this.u=u;
		this.v=v;
	}
	@Override
	public boolean equals(Object arg0) {
		Edge e= (Edge) arg0;
		if(e.v==this.v && e.u==this.u)
			return true;
		else 
			return false;
	}
 }

 
