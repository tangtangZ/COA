package cpu.nbcdu;

import util.DataType;


public class  NBCDU {
    public static void main(String[] args) {
        NBCDU test=new NBCDU();
        DataType src = new DataType("11000000000000000000000000000000");
        DataType dest = new DataType("11010000000000000000001000000000");
        DataType result = test.add(src, dest);
        System.out.println("输出："+result);
    }

    /**
     * @param src  A 32-bits NBCD String
     * @param dest A 32-bits NBCD String
     * @return dest + src
     */
    DataType add(DataType src, DataType dest) {
        String str_src=src.toString();
        String str_dest=dest.toString();
        String sign_src=str_src.substring(0,4);
        String sign_dest=str_dest.substring(0,4);
        String register;
        if(Integer.parseInt(str_src.substring(4),2)==0){
            return dest;
        }else if (Integer.parseInt(str_dest.substring(4),2)==0){
            return src;
        }

        //当减数为0时，程序有bug，此时够减但没有进位，故会取反加一
        /*if(sign_dest.equals(sign_src)){
            register=addnbcd(str_src.substring(4),str_dest.substring(4));
            System.out.println(register);
        }else{
            register=addnbcd("0000"+str_dest.substring(4),"0000"+negetive(str_src.substring(4)));
            System.out.println(register);
            if(register.charAt(3)=='0'){
                register=negetive(register.substring(4));
                sign_dest= sign_dest.equals("1100") ? "1101":"1100";
            }else{register=register.substring(4);}
            System.out.println(register);
        }
        if(Integer.parseInt(register,2)==0){
            sign_dest="1100";
        }
        return new DataType(sign_dest+register);*/
        String result=adder(str_src,str_dest);
        return new DataType(result);
    }

    /***
     *
     * @param src A 32-bits NBCD String
     * @param dest A 32-bits NBCD String
     * @return dest - src
     */
    DataType sub(DataType src, DataType dest) {
        String src1=src.toString();
        if(src1.charAt(3)=='1'){
            src1="1100"+src1.substring(4);
        }else {src1="1101"+src1.substring(4);}
        src=new DataType(src1);
        return add(src,dest);
    }
    private String addnbcd(String a,String b){
        int len=a.length();
        String re;
        String cn="00000";
        String result="";
        for(int i=len/4;i>0;i--){
            re=addstr("0"+a.substring(4*(i-1),4*i),"0"+b.substring(4*(i-1),4*i));
            re=addstr(re,cn);
            if(binToInt(re)>=10){
                re=addstr(re,"00110");
                cn="00001";
            }else{cn="00000";}
            result=re.substring(1)+result;
        }
        return result;
    }
    private  String addstr(String src,String dest){
        int len=src.length()-1;
        int [] src_int =new int[len+1];
        int [] dest_int=new int [len+1];
        char [] result=new  char [len+1];
        for (int i=len;i>=0;i--){
            src_int[i]=src.charAt(i)-'0';
            dest_int[i]=dest.charAt(i)-'0';
        }
        int cn=0;
        for (int i=len;i>=0;i--) {
            if(src_int[i]+dest_int[i]+cn==1|src_int[i]+dest_int[i]+cn==3){
                result[i]='1';
            }else {result[i]='0';}
            if(src_int[i]+dest_int[i]+cn==2|src_int[i]+dest_int[i]+cn==3){
                cn=1;
            }else{cn=0;}
        }
        return String.valueOf(result);
    }
    private  int binToInt(String str){
        int result=0;
        for (int i=4;i>=0;i--){
            result+=(str.charAt(i)-'0')*Math.pow(2,4-i);
        }
        return result;
    }
    private String negetive(String str){
        int len=str.length();
        char[] negetive=new char[str.length()];
        String result;
        String final_result="";
        for (int i=0;i<str.length();i++){
            if (str.charAt(i)=='0'){
                negetive[i]='1';
            }else negetive[i]='0';
        }
        char[] one=new char[str.length()];
        for (int i=0;i< one.length-1;i++){
            one[i]='0';
        }one[one.length-1]='1';
        result=addstr(String.valueOf(negetive),String.valueOf(one));
        for(int i=0;i<len/4;i++){
            String x=addstr(result.substring(i*4,i*4+4),"1010");
            if(Integer.parseInt(x,2)==10){x="0000";}//如果先取反加一再每位加1010，导致低位0000变成1010
            final_result=final_result+x;
        }
        return final_result;
    }


    public String adder(String str1,String str2){
        String sign1=str1.substring(0,4);
        String sign2=str2.substring(0,4);
        String mid_4,sig;
        String result="";
        int c;
        if(!sign1.equals(sign2)){
            if(Integer.parseInt(str1.substring(4),2)==Integer.parseInt(str2.substring(4),2)){
                return "11000000000000000000000000000000";
            }
            c=1;
            for(int i=7;i>0;i--){
                mid_4=add_4(str1.substring(i*4,i*4+4),negetive(str2.substring(i*4,i*4+4),0).substring(1),c);
                result=mid_4.substring(1)+result;
                c= mid_4.charAt(0)-'0';
            }
            sig=sign1;
            if(c==0){
                int t=1;
                String s="";
                sig= sign1.equals("1100") ? "1101":"1100";
                for(int i=6;i>=0;i--){
                    mid_4=negetive(result.substring(4*i,4*i+4),t);
                    t= mid_4.charAt(0)-'0';
                    s=mid_4.substring(1)+s;
                }
                result=s;
            }
        }else{
            c=0;
            sig=sign1;
            for(int i=7;i>0;i--){
                mid_4=add_4(str1.substring(i*4,i*4+4),str2.substring(i*4,i*4+4),c);
                result=mid_4.substring(1)+result;
                c= mid_4.charAt(0)-'0';
            }
        }
        return sig+result;
    }
    public String add_4(String s1,String s2,int c){//sign=1,减法
        char[] result=new char[5];
        for (int i=3;i>=0;i--){
            int x=c+s1.charAt(i)-'0'+s2.charAt(i)-'0';
            result[i+1]= x%2==0 ? '0':'1';
            c= x>=2 ? 1:0;
        }
        result[0]= (char) ('0'+c);
        if(Integer.valueOf(String.valueOf(result),2)>=10){
            return "1"+add6(String.valueOf(result).substring(1));
        }
        return  String.valueOf(result);
    }
    public String add6(String str){
        char[] result=new char[4];
        int []six={0,1,1,0};
        int c=0;
        for (int i=3;i>=0;i--){
            int x=six[i]+str.charAt(i)-'0'+c;
            result[i]= x%2==0 ? '0':'1';
            c= x/2;
        }
        return String.valueOf(result);
    }
    public String negetive(String str,int ch){
        if(str.equals("0000")&&ch==1){
            return "10000";
        }
        char [] result=new char[5];
        int [] six={1,0,1,0};
        for(int i=3;i>=0;i--){
            int x='1'-str.charAt(i)+six[i]+ch;
            result[i+1]= x%2==0 ? '0':'1';
            ch= x>=2 ? 1:0;
        }
        result[0]= '0';
        return String.valueOf(result);
    }

}

/*思路： 特殊情况：符号相反，尾数相同输出+0；其中一个数为0，直接输出另一个数；99-100；
        符号相反时，第二个操作数取反，进位设为1；
        四位取反（输出进位）：按位取反 并 加上1010 加上进位；
                特判：当操作数为“0000”并且进位为1时，输出进位为1，否则为0；//仅在最后结果取反中会用到
        尾数相加：减法时： 先四位取反，对应位置相加，进位初始值为1；如果四位相加的结果大于9，加上“0110”，高位为1；
                          28位的高位如果为0；取反加一；符号与第一个数相反；
                 加法时：对应位置相加，进位初始值为0；如果四位相加的结果大于9，加上“0110”，高位为1；（加法器实现此功能）
 */
