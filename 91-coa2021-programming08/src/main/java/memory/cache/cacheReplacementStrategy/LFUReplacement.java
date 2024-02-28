package memory.cache.cacheReplacementStrategy;

import memory.cache.Cache;

/**
 * TODO 最近不经常使用算法
 */
public class LFUReplacement implements ReplacementStrategy {

    @Override
    public void hit(int rowNO) {
        Cache.getCache().addVisited(rowNO);
    }

    @Override
    public int replace(int start, int end, char[] addrTag, char[] input) {
        System.out.println("need replace");
        int change_line=start;
        for (int i=start;i<end;i++){
            if(Cache.getCache().getVisited(i)>Cache.getCache().getVisited(i+1)){
                change_line=i+1;
                //System.out.println(Cache.getCache().getVisited(i));
            }
        }
        System.out.println("change line:"+change_line);
        //Cache.getCache().addVisited(change_line);
        //Cache.getCache().update(change_line,addrTag,input);
        return change_line;
    }

}
