package memory.cache.cacheReplacementStrategy;

import memory.Memory;
import memory.cache.Cache;

import static memory.cache.Cache.*;

/**
 * TODO 先进先出算法
 */
public class FIFOReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        //Cache.getCache().changeTimeStamp(rowNO,System.currentTimeMillis());
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        int change_line=start;
        for(int i=start;i<end;i++){

            if(Cache.getCache().getTimeStamp(i)>Cache.getCache().getTimeStamp(i+1)){
                change_line=i+1;
            }
        }

        if(Cache.getCache().isDirty(change_line)&&Cache.getCache().isValid(change_line)){
            String paddr=Cache.getCache().getpadd(change_line);
            Memory.getMemory().write(paddr,1024, Cache.getCache().getData(change_line));}
        Cache.getCache().changeTimeStamp(change_line,System.currentTimeMillis());
        return change_line;
    }

}
