import java.util.*; 
public class CFSswitch { 

    public class Triple{
        public double first;
        public int second;
        public int third;
        public Triple(){
            first = 0.0;
            second = 0;
            third = 0;
        }
        public Triple(double a,int b,int c){
            first = a;
            second = b;
            third = c;
        }
    
    }


    int sid;
    PriorityQueue<Triple> min_heap;
    Set<Integer> aa ;
    int max;
    HashFunction h;
    double S=0.0;
    HyperLogLog hll;

    
    
    
    public CFSswitch(int Id, int cells,HashFunction H){
            h = H;
            aa = new  HashSet<Integer>();
            min_heap = new PriorityQueue<Triple>(cells, new Comparator<Triple>() {
            public int compare(Triple t1, Triple t2) {
                if ((t1.first - t2.first)>0){
                    return 1;
                }
                else if((t1.first - t2.first)<0){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        });
        sid = Id;
        max = cells;
        hll=new HyperLogLog(3000);
    }
    
    
    
    
    
        public void addFlow(int flowId, int packetId, int w,boolean oneSwitchLength){
            hll.add(flowId);
            if(aa.contains(flowId)){
                return;
            }
            aa.add(flowId);
            Random generator = new Random(Math.abs(h.h(flowId)));
            double rand = generator.nextDouble();
            double p = Math.pow(rand,1.0/(double)w); 
            p= 1 -  Math.min(Math.min(Math.abs(1.0-S)+Math.abs(p), Math.abs(1.0-p)+Math.abs(S)),Math.abs(S-p));
            int c=0;
            if(oneSwitchLength){
                p=1.1;
            }
            while(c<w  && ( min_heap.size()<max || p > min_heap.peek().first)){
                if(min_heap.size() == max){
                    min_heap.poll();
                }
                Triple t = new Triple(p, flowId, packetId);  
                min_heap.add(t);
                c++;
                rand = generator.nextDouble();
                p*=Math.pow(rand,1.0/(double)(w-c));
            }
    }

    
    public int getId(){
        return sid;
    }

    
    public int flowsNum(){
        return min_heap.size();
    }
    
       public double getN(){
        return hll.estimate();
    }



   
    

}
