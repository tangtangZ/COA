package memory.cache.cacheReplacementStrategy;

import memory.cache.Cache;

/**
 * TODO 最近最少用算法
 */
public class LRUReplacement implements ReplacementStrategy {
    int count=0;
    @Override

    public void hit(int rowNO) {
        count++;
        Cache.getCache().changeTimeStamp(rowNO,count);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        int change_line=start;
        for(int i=start+1;i<=end;i++){
            if(Cache.getCache().getTimeStamp(i)<Cache.getCache().getTimeStamp(change_line)){
                change_line=i;
            }
        }
        count++;
        Cache.getCache().changeTimeStamp(change_line,count);
        return change_line;
    }


}





























