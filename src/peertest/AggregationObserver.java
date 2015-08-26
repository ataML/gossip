package peertest;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;
import peersim.util.IncrementalStats;

public class AggregationObserver implements Control{
	private static final String PAR_PROT="protocol";
	private final int pid;
	public AggregationObserver(String prefix){
		pid=Configuration.getPid(prefix+"."+PAR_PROT);
	}
	public boolean execute() {
		int infectedCount=0;
		for(int i=0;i<Network.size();i++){
			NodeStateHolder ae=(NodeStateHolder)(Network.get(i).getProtocol(pid));
			double status= ae.getValue();
			if(status==1)
				infectedCount++;
		}
		try {
			Writer writer= new BufferedWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("modular-random-vacc-5of10-clustres-active-removed.txt",true))));
			String toWrite= Double.toString((double)infectedCount/Network.size());
			writer.write(toWrite);
			writer.write("\n");
			writer.close();
			System.out.println(toWrite);
		}
		catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		System.out.println("percentage of infected nodes:: " + (double)infectedCount/Network.size());
		return false;
	}

}
