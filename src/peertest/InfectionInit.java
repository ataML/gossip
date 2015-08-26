package peertest;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


import java.util.Vector;

import org.nfunk.jep.Node;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;

public class InfectionInit implements Control {

	private static final String PAR_PROT="protocol";
	private final int pid;
	private Vector<MyNode> nodes=new Vector<MyNode>();
	Random rnd= new Random();
	public InfectionInit(String prefix){
		pid=Configuration.getPid(prefix+"."+PAR_PROT);
	}
	public boolean execute() {
		int count=0;
		
		
	//	randomInfection();
	//	hubInfection();
		clusterRandomInfection();
		randomVaccination();
	//	distributedClusterVaccination();
	//	concentratedClusterVaccination();
	//	oneClusterInfection();

		return true;
	}
	
	private void randomInfection(){
		
		for(int i=0;i<Network.size();i++){
				
				NodeStateHolder ae;
				ae=(NodeStateHolder) Network.get(i).getProtocol(pid);
				int num=rnd.nextInt(100);
				if(num>90){
					ae.setValue(0);
					
				}
				else
					ae.setValue(1);
			}
	}
	private void hubInfection(){
		
		for(int i=0;i<Network.size();i++){
			Linkable link=(Linkable) Network.get(i).getProtocol(FastConfig.getLinkable(pid));
			nodes.add(new MyNode(i, link.degree()));
		}
		Collections.sort(nodes,Collections.reverseOrder());
		for(int i=0;i<1;i++){
			NodeStateHolder ae=(NodeStateHolder) Network.get(nodes.get(i).index).getProtocol(pid);
			ae.setValue(1);
		}
		for(int i=1;i<Network.size();i++){
			NodeStateHolder ae=(NodeStateHolder) Network.get(nodes.get(i).index).getProtocol(pid);
			ae.setValue(0);
		}
		
	}
	private void clusterRandomInfection(){
		Random r=new Random();
		int numInfected=0;
		int numClusters=10;
		int maxInfected=20;
		int clusterSize=100;
		for(int i=0;i<numClusters;i++){
			numInfected=0;
			while(numInfected<maxInfected){
				int index=r.nextInt(clusterSize)+i*clusterSize;
				NodeStateHolder ae=(NodeStateHolder) Network.get(index).getProtocol(pid);
				if(ae.getValue()==0){
					ae.setValue(1);
					numInfected++;
				}
			}
				
		}
		
		for(int i=0;i<Network.size();i++){
			NodeStateHolder ae=(NodeStateHolder) Network.get(i).getProtocol(pid);
			if(ae.getValue()!=1)
				ae.setValue(0);
		}

	}
	private void concentratedClusterVaccination(){
		int clusterSize=100;
		int numVaccinatedClusters=5;
		for(int j=0;j<numVaccinatedClusters;j++){
			for(int i=0;i<clusterSize;i++){
				NodeStateHolder ae=(NodeStateHolder) Network.get(i+j*clusterSize).getProtocol(pid);
				if(ae.getValue()==0)
					ae.setValue(2);
			
			}
		}
		int numRemoved=0;
		int numInfected=0;
		for(int i=0;i<Network.size();i++){
			NodeStateHolder ae=(NodeStateHolder) Network.get(i).getProtocol(pid);
			if(ae.getValue()==2)
				numRemoved++;
			else if(ae.getValue()==1)
				numInfected++;
			
		}
		System.out.println(numRemoved+" "+numInfected);
	}
	private void distributedClusterVaccination(){
		Random r=new Random();
		int numClusters=10;
		int clusterSize=100;
		int maxVaccinated=40;
		for(int clusterIndex=0;clusterIndex<numClusters;clusterIndex++){
			int numVaccinated=0;
		
			while(numVaccinated<maxVaccinated){
				int index=r.nextInt(clusterSize)+clusterIndex*clusterSize;
				NodeStateHolder ae=(NodeStateHolder) Network.get(index).getProtocol(pid);
				if(ae.getValue()==0){
					numVaccinated++;
					ae.setValue(2);
				}
			}
		}
		int numInfected=0;
		int numRemoved=0;
		for(int i=0;i<Network.size();i++){
			NodeStateHolder nsh= (NodeStateHolder) Network.get(i).getProtocol(pid);
			if(nsh.getValue()==1)
				numInfected++;
			else if(nsh.getValue()==2)
				numRemoved++;
		}
		System.out.println(numInfected+" "+numRemoved);
	}
	private void randomVaccination(){
		Random r= new Random();
		int maxVaccinated=400;
		int numVaccinated=0;
		int numNodes=Network.size();
		while(numVaccinated<maxVaccinated){
			int index=r.nextInt(numNodes);
			NodeStateHolder nsh=(NodeStateHolder) Network.get(index).getProtocol(pid);
			if(nsh.getValue()==0){
				nsh.setValue(2);
				numVaccinated++;
			}
			
		}
	}
	private void oneClusterInfection(){
		Random r=new Random();
		for(int j=0;j<100;j++){
			int index=r.nextInt(250);
			NodeStateHolder ae=(NodeStateHolder) Network.get(index).getProtocol(pid);
			ae.setValue(1);
		}
	
	for(int i=0;i<Network.size();i++){
		NodeStateHolder ae=(NodeStateHolder) Network.get(i).getProtocol(pid);
		if(ae.getValue()!=1)
			ae.setValue(0);
	}
	
	
	
}
}
class MyNode implements Comparable<MyNode>{


	int index;
	int degree;
	
	public MyNode(int index,int degree) {
		this.index=index;
		this.degree=degree;
	}



	public int compareTo(MyNode arg0) {
		
		return this.degree-arg0.degree;
	}
	
}


