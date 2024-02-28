package transformer;

import java.math.BigDecimal;

public class Transformer {
    /**
     * Integer to binaryString
     *
     * @param numStr to be converted
     * @return result
     */
    public String intToBinary(String numStr) {
        int num,num2;
        String binary = "";
        num = Integer.valueOf(numStr);
        num2=Math.abs(num);
        for (int i = 0; i < 32; i++) {
            binary = num2 % 2 + binary;
            num2 = num2 / 2;
        }
        if (num<0) {
            char[] ch=binary.toCharArray();
            for(int k=0;k<32;k++){
                if (ch[k]=='1'){
                    ch[k]='0';
                }else{
                    ch[k]='1';
                }
            }
            /*if (ch[31]=='0'){
                ch[31]='1';
            }else{
                for (int j=30 ;j>0;j--){
                    if (ch[j]=='0'){
                        ch[j]='1';
                        for(int a=31;a>j;a--){
                            ch[a]='0';
                        }
                        break;
                    }
                }
            }*/
            Transformer t=new Transformer();
            char[] result=t.oneadder(ch);
            binary= String.valueOf(ch).substring(1);
        }

        return binary ;
    }

    /**
     * BinaryString to Integer
     *
     * @param binStr : Binary string in 2's complement
     * @return :result
     */
    public String binaryToInt(String binStr) {
        int[] ch=new int [binStr.length()];
        for (int j=0;j<binStr.length();j++){
            ch[j]=Integer.parseInt(binStr.substring(j,j+1));
        }
        int num=0;
        for (int i=31;i>0;i--){
            num+=ch[i]*(int)Math.pow(2,31-i);
        }
        if (ch[0]==1){
            num=num-(int)Math.pow(2,31)-1;
        }
        return String.valueOf(num);
    }
    /**
     * The decimal number to its NBCD code
     * */
    public String decimalToNBCD(String decimalStr) {
        int num=Integer.valueOf(decimalStr);
        int _num=Math.abs(num);
        String nbcd="";
        for (int k=0;k<7;k++){
            int i =_num%10;
            for (int j = 0; j<4; j++) {
                nbcd=i%2+nbcd;
                i=i/2;
            }
            _num=_num/10;
        }
        if (num>0|decimalStr=="0"){
            nbcd="1100"+nbcd;
        }else if (num<0|decimalStr=="-0"){
            nbcd="1101"+nbcd;
        }
        return nbcd;
    }

    /**
     * NBCD code to its decimal number
     * */
    public String NBCDToDecimal(String NBCDStr) {
        int result=0;
        String fournum;
        for(int i=7;i>0;i--){
            fournum=NBCDStr.substring(4*i,4*i+4);
            int num=0;
            for (int j=3 ;j>=0;j--){
                int a=fournum.charAt(j)-48;
                num+=a*((int)Math.pow(2,3-j));
            }
            result=num*(int)Math.pow(10,7-i)+result;
            System.out.println(num);
        }
        if (NBCDStr.charAt(3)=='1'){
            result=-result;
        }
        return String.valueOf(result);
    }

    /**
     * Float true value to binaryString
     * @param floatStr : The string of the float true value
     * */
    public String floatToBinary(String floatStr) {
        BigDecimal db = new BigDecimal(floatStr);
        String ii = db.toPlainString();
        int a=floatStr.indexOf(".");
        String x=ii.substring(0,a),y=ii.substring(a+1);
        char []arr=y.toCharArray();
        float decbin=0;
        int num=y.length();
        String dec="";
        if (floatStr.contains("E")) {
            int fo = floatStr.indexOf("E");
            String z = floatStr.substring(fo + 1);
            if ((db.doubleValue() < Math.pow(2, -126)) & (db.doubleValue() > Math.pow(2, -127))) {
                return "00000000011000000000000000000000";
            } else if (db.doubleValue() == Math.pow(2, -127)) {
                return "00000000010000000000000000000000";
            } else if (Integer.valueOf(z) > 100 & Integer.valueOf(x) < 0) {
                return "-Inf";
            } else if (Integer.valueOf(z) > 100 & Integer.valueOf(x) > 0) {
                return "+Inf";
            }
        }
        //小数部分;
        for (int j = 0; j < num; j++) {
            decbin += (Integer.valueOf(arr[j]) - 48) * Math.pow(10, -j - 1);
        }
        System.out.println(decbin);
        for (int i=0;i<64;i++) {
            decbin=decbin*2;
            if (decbin >= 1) {
                dec =   dec+"1";
                decbin -= 1;
            } else if(decbin<1){
                dec =  dec+"0";
            }
        }
        System.out.println(dec);
        // 整数部分
        String xbin="";
        int j=Math.abs(Integer.valueOf(x)),h=j;
        while (j!=0){
            xbin=j%2+xbin;
            j=j/2;
        }
        //指数部分
        String zbin="";
        System.out.println(h==0);
        if (h==0){
            int m=dec.indexOf("1");
            int k =-m+127-1;
            for (int i=8;i>0;i--){
                zbin=k%2+zbin;
                k=k/2;}

            if (ii.contains("-")){ return "1"+zbin+dec.substring(m+1,m+22)+"00"; }
            else{ return "0"+zbin+dec.substring(m+1,m+22)+"00" ;}
        }else/* if (j!=0)*/ {
            int n = xbin.length();
            int k = n - 1 + 127;
            for (int i = 8; i > 0; i--) {
                zbin = k % 2 + zbin;
                k = k / 2; }
            if (ii.contains("-")){ return "1"+zbin+xbin.substring(1,n)+dec.substring(0,22-n)+"00"; }
            else{ return "0"+zbin+xbin.substring(1,n)+dec.substring(0,22-n)+"00"; }
        }
    }

    /**
     * Binary code to its float true value
     * */
    public String binaryToFloat(String binStr) {
        String exp=binStr.substring(9);
        String index=binStr.substring(1,9);
        int  fh=Integer.parseInt(binStr.substring(0,1));
        int  numd=0;
        double nume=0;
        double result;
        int[] ch=new int [8];
        for (int j=0;j<8;j++){
            ch[j]=Integer.parseInt(index.substring(j,j+1));
        }

        for (int i=7;i>=0;i--){
            numd+=ch[i]*(int)Math.pow(2,7-i);
        }
        char []arr=exp.toCharArray();
        for (int i=1;i<=23;i++){
            if (arr[i-1]=='1'){
                nume+=Math.pow(2,-i);
            }
        }
        if (numd==0&nume!=0){
            result=nume*Math.pow(2,-126);
        }else if (numd==255&nume==0&fh==0){
            return "+Inf";
        }else if (numd==255&nume==0&fh==1){
            return "-Inf";
        }else if (numd==0&nume==0){
            return "0.0";
        } else {
            result=(1+nume)*Math.pow(2,numd-127);
        }
        if (fh==1){
            result=-result;
        }
        return String.valueOf(result);
    }
    public  char[] oneadder(char[] str){
            char bit='1',carry='0';
            int len=str.length;
            char[] result=new char[len+1];
            if(str[len-1]=='1'){
                carry='1';
                result[len]='0';
            }else{
                carry='0';
                result[len]='1'; }
            for(int i=len-2;i>=0;i--){
                if(str[i]+carry-'0'-'0'==2){
                    carry='1';
                    result[i+1]='0';
                }else{
                    carry='0';
                    result[i+1]=str[i];
                }
            }
            result[0]=carry;
        return result;
    }

}
