package peertest;


import java.util.ArrayList;

import peersim.cdsim.CDProtocol;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.core.Protocol;

public class NetworkOverlay  implements Linkable,Protocol{

	ArrayList<Node> neighbours;
	private int clusterNumber;
	
	
	public NetworkOverlay(String prefix) {
		neighbours=new ArrayList<Node>();
	}

	public void onKill() {
		// TODO Auto-generated method stub
		
	}

	public boolean addNeighbor(Node neighbour) {
		
		if(!this.neighbours.contains(neighbour))
			this.neighbours.add(neighbour);
		return false;
	}

	public boolean contains(Node neighbor) {
		
		return this.neighbours.contains(neighbor);
	}

	public int degree() {
		return this.neighbours.size();
	}

	public Node getNeighbor(int i) {
		return this.neighbours.get(i);
	}
	
	@Override
	public Object clone() {
		NetworkOverlay no=null;
		try {
			no=(NetworkOverlay) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		no.neighbours=new ArrayList<Node>();
		return no;
	}
	
	public void pack() {
		// TODO Auto-generated method stub
		
	}
	
	public int getClusterNumber() {
		return clusterNumber;
	}
	
	public void setClusterNumber(int clusterNumber) {
		this.clusterNumber = clusterNumber;
	}

	public void nextCycle(Node node, int protocolID) {
		// TODO Auto-generated method stub
		
	}
}
