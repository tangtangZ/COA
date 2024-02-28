package util;

import java.util.Arrays;



public class CRC {

    /**
     * CRC计算器
     *
     * @param data       数据流
     * @param polynomial 多项式
     * @return CheckCode
     */
    public static char[] Calculate(char[] data, String polynomial) {
        CRC c=new CRC();
        String re,data1;
        data1=String.copyValueOf(data);
        re=c.mod2div(data1,polynomial);
        return re.toCharArray();


        /*int k=polynomial.length();
        char [] p=polynomial.toCharArray();
        char[] mid=new char[k];

        mid= Arrays.copyOfRange(data,0,p.length);
        for (int i=0;i<data.length;i++){
            if (i<data.length-k+1){ mid[k-1]=data[i+k-1];}
            else{mid [k-1]='0';}
            System.out.println(mid[0]=='1');
            System.out.println(mid);
            if (mid[0]=='1'){
                for(int j=1;j<k;j++){
                    if ((int)mid[j]==(int)p[j]){
                        mid[j-1]='0';
                    }else {mid[j-1]='1';} }
            }else{ for(int j=1;j<k;j++){
                mid[j-1]=mid[j]; }
            }
        }
        return Arrays.copyOfRange(mid,0,k-1);*/
    }

    /**
     * CRC校验器
     *
     * @param data       接收方接受的数据流
     * @param polynomial 多项式
     * @param CheckCode  CheckCode
     * @return 余数
     */
    public static char[] Check(char[] data, String polynomial, char[] CheckCode) {
        String str1=new String(data);
        String str2=new String(CheckCode);
        char[] midcode=(str1+str2).toCharArray();
        char[] result=CRC.Calculate(midcode,polynomial);
        return result;


    }
    public String mod2div(String dest,String src){
        String reminder="";;
        int len=dest.length(),len2=src.length();
        dest=dest;
        for(int i=0;i<len-len2+1;i++){
            if(dest.charAt(0)=='1'){
                dest=xor(dest.substring(1,len2),src.substring(1))+dest.substring(len2);
            }else {
                dest=dest.substring(1);
            }
        }
        return dest;
    }
    public String xor(String a,String b){
        String result="";
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)==b.charAt(i)){
                result=result+"0";
            }else {result=result+"1";}
        }
        return  result;
    }


}
