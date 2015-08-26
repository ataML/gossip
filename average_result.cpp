#include <iostream>
#include <fstream>
using namespace std;

static int NUM_OF_NETWORKS=1;
static int NUM_OF_EXPERIMENT=100;
static int EXPERIMENT_STEPS=15;

int main(){
	ifstream fin("modular-random-vacc-5of10-clustres-active-removed.txt");
	ofstream fout("modular-random-vacc-5of10-clustres-active-removed-ave.txt");
	double res[15];
	for(int i=0;i<NUM_OF_NETWORKS;i++){
	
		for(int j=0;j<EXPERIMENT_STEPS;j++) res[j]=0;
		
		for(int j=0;j<NUM_OF_EXPERIMENT;j++){
			for(int k=0;k<EXPERIMENT_STEPS;k++){
				double tmp;
				fin>>tmp;
				res[k]+=tmp;
			}
		}
		for(int j=0;j<EXPERIMENT_STEPS;j++){
			res[j]/=NUM_OF_EXPERIMENT;
			fout<<res[j]<<endl;
		}
		fout<<"-----"<<endl;
	}
	fin.close();
	fout.close();
}