import java.util.*;

public class FRSwitch { 
    Set<Integer> bf;
    CountTable ct;
    int id;
    int Wcounter;
    int Fcounter;
    
    
    
    
    
    
    public FRSwitch(int sId, int ctSize){
        Wcounter=0;
        Fcounter=0;
        int ctHashFunctionsNum =3;
        id= sId;
        bf = new HashSet<Integer>();
        ct = new CountTable(ctSize,ctHashFunctionsNum);
    }
    
    
    
    
    
    public void addFlow(int flowId, int packetsNum){
        boolean bfAlreadyExist = false;
        if(bf.contains(flowId)){
            bfAlreadyExist=true;
        }
        else{
            bf.add(flowId);
            ct.addFlow(flowId,packetsNum,bfAlreadyExist);
            Fcounter++;
        }
       
       // boolean ctAlreadyExist = !ct.check(flowId);
        
        Wcounter++;
        
        
    }
    
    
    
    
    public void removeFlow(int flowId, int packetsNum){
        if(bf.contains(flowId)){
            ct.removeFlow(flowId,packetsNum);
            bf.remove(flowId);
        }
    }

    
    
    public int getId(){
        return id;
    }
    
    
    
    
    public CountTable getCountTable(){
        return ct;
    }

    
    public int flowsNum(){
        return Wcounter;
    }
    

}
