package cpu.alu;

import util.DataType;


/**
 * Arithmetic Logic Unit
 * ALU封装类
 */
public class ALU {

    DataType remainderReg;

    /**
     * 返回两个二进制整数的和
     * dest + src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType add(DataType src, DataType dest) {
        ALU alu=new ALU();
        String srcstr=src.toString();
        String deststr=dest.toString();
        return new DataType(alu.addStr(srcstr,deststr));
    }


    /**
     * 返回两个二进制整数的差
     * dest - src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType sub(DataType src, DataType dest) {
        ALU example=new ALU();
        String dest1=dest.toString();
        String src1=example.oppNum(src.toString());
        String result=example.addStr(src1,dest1);
        return new DataType(result);
    }

    /**
     * 返回两个二进制整数的乘积(结果低位截取后32位)
     * dest * src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType mul(DataType src, DataType dest) {
        String srcstr = src.toString();
        String deststr = dest.toString();
        if(Integer.parseUnsignedInt(srcstr,2)==0|Integer.parseUnsignedInt(deststr,2)==0){
            return new DataType("00000000000000000000000000000000");
        }
        char[] dest1=deststr.toCharArray();
        int[] destint=new int[33];
        for (int i=0;i<32;i++){
            destint[i]=(int)dest1[i]-48; }
        destint[32]=0;
        ALU forth=new ALU();
        String register="00000000000000000000000000000000"+deststr;
        for (int i=32;i>0;i--) {
            if ((destint[i] - destint[i - 1]) == 0) {
                register = forth.moveStr(register, register.length(), 1);
            } else if ((destint[i] - destint[i - 1]) == 1) {
                register = forth.addStr(register.substring(0, 32), srcstr)+register.substring(32);
                register=forth.moveStr(register,64,1);
            } else if ((destint[i] - destint[i - 1]) == -1) {
                register = forth.addStr(register.substring(0, 32), forth.oppNum(srcstr))+register.substring(32);
                register=forth.moveStr(register,64,1);
            }
        }
        return new DataType(register.substring(32));
    }

    /**
     * 返回两个二进制整数的除法结果
     * 请注意使用不恢复余数除法方式实现
     * dest ÷ src
     *
     * @param src  32-bits
     * @param dest 32-bits
     * @return 32-bits
     */
    public DataType div(DataType src, DataType dest) {
        String srcstr = src.toString();
        String deststr = dest.toString();
        if (valueOf(srcstr,2)==0){
            throw new ArithmeticException();
        }
        ALU alu =new ALU();
        String reminder;
        String register;
        int len=srcstr.length();
        int y=Integer.parseInt(srcstr.substring(0,1));
        int x=Integer.parseInt(deststr.substring(0,1));
        //符号扩展；
        if (Integer.parseInt(deststr.substring(0,1))==1) {
            register = "11111111111111111111111111111111"+deststr;
        }else{ register="00000000000000000000000000000000"+deststr;}
        //加减移位操作；
        if(Integer.parseInt(deststr.substring(0,1))!=Integer.parseInt(srcstr.substring(0,1))){
            reminder=alu.addStr(register.substring(0,len),srcstr);
        }else{reminder=alu.addStr( alu.oppNum(srcstr),register.substring(0,len));}
        register=reminder+deststr;
        for (int i=0;i<len;i++){
            int r=Integer.parseInt(reminder.substring(0,1));
            if (r==y){
                register=alu.moveStr(register,len*2,0).substring(0,len*2-1)+"1";
            }else {register=alu.moveStr(register,len*2,0).substring(0,len*2-1)+"0";}
            if(y!=r){
                reminder=alu.addStr(register.substring(0,len),srcstr);
            }else{reminder=alu.addStr( alu.oppNum(srcstr),register.substring(0,len));}
            register=reminder+register.substring(len);
        }
        //修正商和余数;
        String quotient=alu.moveStr(register.substring(len),len,0);
        if(Integer.parseInt(reminder.substring(0,1))==y){
            quotient=quotient.substring(0,len-1)+"1";
        }else{
            quotient=quotient.substring(0,len-1)+"0";
        }
        if(Integer.parseInt(quotient.substring(0,1))==1){quotient= alu.addStr(quotient,"00000000000000000000000000000001");}
        if (Integer.parseInt(reminder.substring(0,1))!=x){
            if (x==y){
                reminder=alu.addStr(reminder,srcstr);
            }else {
                reminder=alu.addStr(alu.oppNum(srcstr),reminder);
            }
        }
        //修正除法本身bug;
        if(valueOf(reminder,2)==valueOf(srcstr,2)){
            quotient=alu.addStr(quotient,"00000000000000000000000000000001");
            reminder="00000000000000000000000000000000";
        }else if(valueOf(reminder,2)+valueOf(srcstr,2)==0){
            quotient=alu.addStr(quotient,"11111111111111111111111111111111");
            reminder="00000000000000000000000000000000";
        }
        remainderReg=new DataType(reminder);
        return new DataType(quotient);
    }

    //位移字符串;
    public String moveStr(String str,int len,int i){
        if(i==1) {
            if (Integer.parseInt(str.substring(0, 1)) == 0) {
                return "0" + str.substring(0, len-1);
            } else {
                return "1" + str.substring(0, len-1);
            }
        }else{
            return str.substring(1,len)+"0";
        }
    }

    //字符串相加;
    public String addStr(String str1,String str2){
        char[] addfirst=str1.toCharArray();
        char[] addsecound=str2.toCharArray();
        char[] resultpre=new char[32];
        int[] scrint=new int [32];
        int[] destint=new int [32];
        int Cn=0;
        for (int i=0;i<32;i++){
            scrint[i]=addfirst[i]-48; }
        for (int i=0;i<32;i++){
            destint[i]=(int)addsecound[i]-48; }
        for(int i=31;i>=0;i--){
            if (scrint[i]+destint[i]+Cn==3|scrint[i]+destint[i]+Cn==1){
                resultpre[i]='1';}
            else { resultpre[i]='0'; }
            if (scrint[i]+destint[i]+Cn==3|scrint[i]+destint[i]+Cn==2){
                Cn=1;
            }else{Cn=0;}
        }
        return String.valueOf(resultpre);
    }

    //取反加一操作;
    public String oppNum(String str) {
        char[] resultpre = new char[32];
        for (int i = 31; i >= 0; i--) {
            if (Integer.parseInt(str.substring(i, i + 1)) == 0) {
                resultpre[i] = '1';
            } else {
                resultpre[i] = '0';
            }
        }
        ALU example=new ALU();
        return example.addStr(String.valueOf(resultpre),"00000000000000000000000000000001");
    }

    //二进制转十进制;
    public int valueOf(String num, int radix) {
        int ans = 0;
        for (int i = 0; i < num.length(); i++) {
            int temp;
            if (num.charAt(i) <= '9' && num.charAt(i) >= '0') temp = num.charAt(i) - '0';
            else temp = num.charAt(i) - 'a' + 10;
            ans = ans * radix + temp;
        }
        return ans;
    }

}
