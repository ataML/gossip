package peertest;

import java.util.Random;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.transport.Transport;
import peersim.vector.SingleValueHolder;


public class NodeStateHolder  implements EDProtocol,CDProtocol{
	
	private int value;
	private int infectedDur=0;
	private final static String OVERLAY="overlay";
	private final static int MAX_DAYS=7;
	private final static double INFECTION_PROB=70;
	private final int overlayPID;
	public NodeStateHolder(String prefix) {
		overlayPID=Configuration.getPid(prefix+"."+OVERLAY);


	}

	/*
	 * 	value is either 0,1 or 2
	 *  0 for a susceptible node
	 *  1 for an infected node
	 *  2 for removed node
	*/
	

	

	public Object clone()  {
		NodeStateHolder mat=null;
		try {
			mat=(NodeStateHolder) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return mat;
	}

	public void nextCycle(Node node, int protocolID) {
		
		if(this.value==1)
			infectedDur++;
		if(infectedDur>= MAX_DAYS)
			value=2;
		
		NetworkOverlay linkable = (NetworkOverlay) node.getProtocol(FastConfig.getLinkable(protocolID));
	//	NetworkOverlay linkable = (NetworkOverlay) node.getProtocol(overlayPID);
	
		if(linkable.degree() > 0){
			
			Node peern = linkable.getNeighbor(CommonState.r.nextInt(linkable.degree()));
			if(!peern.isUp())
				return;
			((Transport) node.getProtocol(FastConfig.getTransport(protocolID)))
			.send(node, peern, new AverageMessege(value, node), protocolID);;
		
		}
		
	}

	public void processEvent(Node node, int pid, Object event) {
		Random infProb= new Random();
	
	    AverageMessege messege= (AverageMessege) event;
		
	    if(messege.val==1 && this.value!=2 && infProb.nextInt(100)<=INFECTION_PROB  )
	    	value=1;
	    else if(messege.val==2 && value!=1 && infProb.nextInt(100)<=70)
	    	value=2;
		if(messege.sender!=null)
			((Transport)node.getProtocol(FastConfig.getTransport(pid))).send(node, messege.sender,new AverageMessege(value, null) , pid);;	
			
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}


}
class AverageMessege{
	
	final double val;
	final Node sender;
	
	public AverageMessege(double value, Node sender){
		this.val=value;
		this.sender=sender;
	}
}
