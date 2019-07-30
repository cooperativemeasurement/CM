import java.util.*;


public class Decoder { 
    
    
    private static final double EPSILON = 1e-10;
    
    public Decoder(){}


    
    int[][] singleDecode(FRSwitch s){
        CountTable ct = s.getCountTable();
        return singleDecodeHelper(s,ct.getIncodedTable(),true);
    }
    
    
    private int[][] deepCopy(int[][] A) {
    int[][] B = new int[A.length][A[0].length];
    for (int x = 0; x < A.length; x++) {
      for (int y = 0; y < A[0].length; y++) {
          B[x][y] = A[x][y];
      }
    }
    return B;
  }
  
  
  
  
  
  
  
  

    
    
    
    
  
    int[][] singleDecodeHelper(FRSwitch s, int[][] inc,boolean ff){

        CountTable ct = s.getCountTable();
        
        Map<Integer,Integer> flowset = new HashMap<Integer,Integer>();
        boolean stuck = false;

        while(!stuck){
            stuck = true;
            for(int j=0;j<inc.length;j++){
                //search for a flow sitting alone in a cell
               // System.out.print("stuck");
                if(inc[j][1] == 1){

                    
                 
                       int id =(inc[j][0]);
                    
                    // delete this flow from the CountTable
                    int[] hashed =  ct.hashFlow(id);
                    boolean aaa=false;
                    for(int index : hashed){
                        if(index == j){
                            aaa=true;
                            stuck = false;
                        }
                    
                    }
                    if(aaa){
                        //System.out.println( "FOUND\n ");
                        int packets = inc[j][2];
                        inc[j][1]=0;
                        flowset.put(id,packets);
                    }
                    
                }
            }
            
            
            
        }
        int[][] result = new int[flowset.size()][2];
        int c=0;
        for(int fId: flowset.keySet()){
            result[c][0] = fId;
            result[c][1] = flowset.get(fId);
            c++;
        }

        if(result == null){return new int[0][2];}
        return result;
    }
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    Map<Integer,int[][]> flowDecode(FRSwitch[] switches,Integer[] switchesId){
                  
        Map<Integer,int[][]> S = new HashMap<Integer,int[][]>();
         Map<Integer,int[][]> incoded = new HashMap<Integer,int[][]>();
         
       // System.out.println(Arrays.deepToString(switches[0].getCountTable().getIncodedTable()));
         
        for(int i =0; i < switches.length; i++){
            int[][] a = singleDecode(switches[i]);
            S.put(switches[i].getId(),a);
            CountTable ctt = switches[i].getCountTable();
            int[][] inc = ctt.getIncodedTable();
            int [][] r =deepCopy(inc);
            
            

            for(int f=0;f<a.length;f++){
                int[] hashed =  ctt.hashFlow(a[f][0]);
                for(int index : hashed){
                    //System.out.println(index);
                    r[index][0] ^= a[f][0];
                    r[index][1] -= 1;
                    r[index][2] -= a[f][1];
                    switches[i].bf.remove(a[f][0]);
                }
            }
            incoded.put(switches[i].getId(),r);
             
        }
        //System.out.println(Arrays.deepToString(incoded.get(switches[0])));
            
    
            
            

        boolean finish = false;
        
        while(!finish){
            boolean change = false;
            finish = true;
            for(int i =0; i < switches.length; i++){
                int[][] Ai =  S.get(switches[i].getId());
               // System.out.println(i+ "\n");
                for(int j =0; j < switchesId.length; j++){
                   // System.out.print(j+" ");
                    if(i==j){
                        continue;
                    }
                 //   System.out.print(j+" ");
                    int[][] Aj = S.get(switchesId[j]);
                    for(int k=0;k<Ai.length;k++){
                            boolean bfDontExist = switches[j].bf.contains(Ai[k][0]);
                            CountTable ct = switches[j].getCountTable();
                            int[] hashed =  ct.hashFlow(Ai[k][0]);

                            if(bfDontExist){

                                 int[][] AjTable = new int[Aj.length + 1][2];

                            
                                int t=0;
                                while(t<Aj.length){
                                    AjTable[t][0] =  Aj[t][0];
                                    AjTable[t][1] =  Aj[t][1];
                                    t++;

                                    
                                }
                                
                                AjTable[t][0] = Ai[k][0];
                                AjTable[t][1] = 1;
                                
                                int [][] incodedTable =deepCopy(incoded.get(switches[j].getId()));
                                    
                                // delete this flow from the CountTable
                                for(int index : hashed){
                                    incodedTable[index][0]^=Ai[k][0];
                                    incodedTable[index][1] -= 1;
                                }
                                switches[j].bf.remove(Ai[k][0]);
                                incoded.replace(switches[j].getId(),incodedTable);
                                S.replace(switches[j].getId(), AjTable);
                               
                                change = true;
                            }

                    }
                }
                
                
            }

                
            for(int l =0; (l < switches.length )&& (change); l++){
                int[][] result = singleDecodeHelper(switches[l],incoded.get(switches[l].getId()),true);
                int[][] a =  S.get(switches[l].getId());
                if(result.length==0){
                    continue;
                }
                
                int[][] compined = new int[result.length+a.length][2];
                int mm=0;
                for(int kk=0;kk<compined.length;kk++){
                    if(kk < a.length){
                        compined[kk] = a[kk];
                    }
                    else{
                        finish = false;
                        compined[kk] = result[mm];
                        mm++;
                    }
                }
                
                S.replace(switches[l].getId(),compined);

                
                CountTable cct = switches[l].getCountTable();
                int[][] inc = cct.getIncodedTable();
                
                
                
                
                 int [][] r =deepCopy(inc);
                
                
                
                
                
                for(int f=0;f<result.length;f++){
                    int[] hashed =  cct.hashFlow(result[f][0]);
                    for(int index : hashed){
                        r[index][0] ^=result[f][0];
                        r[index][1] -= 1;
                    }
                    switches[l].bf.remove(result[f][0]);
                } 
                incoded.replace(switches[l].getId(),r);
            }
        
        }
             return S;
    
    }
    
    
    
    
    
    void ConLinearEqu(int [][] flows,FRSwitch s){
        int [][] table = singleDecodeHelper(s,s.getCountTable().getIncodedTable(),false);
        int [][] Fl =  singleDecode(s);
        int [] updateFlows = new int[flows.length - Fl.length ];
        
        if(Fl.length != flows.length){
            int t=0;
            for(int j=0;j<flows.length;j++){
                if(flows[j][1] ==0){
                    updateFlows[t] = flows[j][0];
                    t++;
                }
            }
        }
        else{
            return;
        }
        
        
        
        
        CountTable ct = s.getCountTable();
        int[][] M = new int[table.length][updateFlows.length];
        int[] b = new int[table.length];
        
            for(int j=0;j< updateFlows.length;j++){
            
                int[] hashed =  ct.hashFlow(updateFlows[j]);
                
                for(int i: hashed){
                    M[i][j] += 1;

            }
        }
        
        for(int y=0;y<table.length;y++){
            b[y] = table[y][2];
        }
    
    }
    
    
    
}



















