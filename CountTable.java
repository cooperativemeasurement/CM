import java.util.*;

public class CountTable{
    int[][] incodedTable;
    HashFunction[] hashFunctions;

    
    public CountTable(int Size, int HashFunctionsNum){
        incodedTable = new int[Size][3];
        hashFunctions = new HashFunction[HashFunctionsNum];
        for(int i=0; i<HashFunctionsNum; i++){
            hashFunctions[i] = new HashFunction();
        }
    } 
    
    
     public void addFlow(int flowId, int packetsNum, boolean alreadyExist){
        for(int i=0;i< hashFunctions.length;i++){
            int cellNum = hashFunctions[i].hash(flowId,incodedTable.length);
            if(incodedTable[cellNum][1]==0){
                incodedTable[cellNum][0] = flowId;
                incodedTable[cellNum][1] = 1;
                incodedTable[cellNum][2] = packetsNum;
            }
            else{
                if(!alreadyExist){
                    incodedTable[cellNum][0] = incodedTable[cellNum][0]^flowId;
                    incodedTable[cellNum][1] += 1;
                }
                incodedTable[cellNum][2] += packetsNum;
            }
        }
    }
    
    
    
    public void removeFlow(int flowId, int packetsNum){
        for(int i=0;i< hashFunctions.length;i++){
            int cellNum = hashFunctions[i].hash(flowId,incodedTable.length);
                incodedTable[cellNum][0] = incodedTable[cellNum][0]^flowId;
                incodedTable[cellNum][1] -= 1;
                incodedTable[cellNum][2] -= packetsNum;
        }
    }
    
    public boolean check(int data){
        for(int i=0; i<hashFunctions.length; i++){
            
            int hashResult = hashFunctions[i].hash(data,incodedTable.length);
            
            if(incodedTable[hashResult][1] == 0){
                return true;
            }
            
        }
        return false;
    }
    
    
    public int[] hashFlow(int flowId){
        int [] res = new int[hashFunctions.length];
        for(int i=0;i< hashFunctions.length;i++){
            int cellNum = hashFunctions[i].hash(flowId,incodedTable.length);
            res[i] = cellNum;
        }
        return res;
    }
    
    
    
    
    public int[][] getIncodedTable(){
        return incodedTable;
    }
    
    public HashFunction[] getHashFunctions(){
        return hashFunctions;
    }
    
}
