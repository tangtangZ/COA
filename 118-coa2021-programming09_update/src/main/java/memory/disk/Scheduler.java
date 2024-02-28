package memory.disk;

import java.util.Arrays;

public class Scheduler {

    /**
     * 先来先服务算法
     * @param start 磁头初始位置
     * @param request 请求访问的磁道号
     * @return 平均寻道长度
     */
    public double FCFS(int start, int[] request) {
        int len=0;
        for(int i=0;i< request.length;i++){
            len=len+Math.abs(start-request[i]);
            start=request[i];
        }
        return (double) len/(double) request.length;
    }

    /**
     * 最短寻道时间优先算法
     * @param start 磁头初始位置
     * @param request 请求访问的磁道号
     * @return 平均寻道长度
     */
    public double SSTF(int start, int[] request) {
        /*Arrays.sort(request);*/
        int len=request.length;
        int sum=0;
        /*
        int first=-1;
        for(int i=0;i<request.length;i++){
            if(start>request[i]){
                first=i;
            }
        }
        if(first==-1){
            sum=request[len-1]-start;
        }else if(first==len-1){
            sum=start-request[0];
        }else if(start-request[first]<request[first+1]-start){
            sum=start-request[0]+request[len-1]-request[0];}
        else{
            sum=request[len-1]-start+request[len-1]-request[0];
        }*/
        int[] visited=new int[len];
        for(int i=0;i<len;i++) visited[i]=0;
        for(int i=0;i<len;i++){
            int min=Disk.TRACK_NUM;
            int minindex=0;
            for(int j=0;j<len;j++){
                if(Math.abs(request[j]-start)<min && visited[j]==0){
                    min=Math.abs(request[j]-start);
                    minindex=j;
                }
            }
            sum+=min;
            start=request[minindex];
            visited[minindex]=1;
        }
        return (double) sum/(double) len;
    }

    /**
     * 扫描算法
     * @param start 磁头初始位置
     * @param request 请求访问的磁道号
     * @param direction 磁头初始移动方向，true表示磁道号增大的方向，false表示磁道号减小的方向
     * @return 平均寻道长度
     */
    public double SCAN(int start, int[] request, boolean direction) {
        Arrays.sort(request);
        int sum;
        int len =request.length;
        if(request[0]>=start & direction){
            sum=request[request.length-1]-start;
        }else if(request[len-1]<=start && !direction){
            sum=start-request[0];
        }
        else if(direction){
            sum=Disk.TRACK_NUM-start+Disk.TRACK_NUM-request[0]-2;
        }else{
            sum=start+request[len-1];
        }
        return (double) sum/(double) len;
    }

}
