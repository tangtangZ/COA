package cpu.fpu;

import util.DataType;
import util.IEEE754Float;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {
    public static void main(String[] args) {

    }

    private final String[][] mulCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_ZERO, IEEE754Float.NaN}
    };

    private final String[][] divCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.NaN},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
    };


    /**
     * compute the float mul of dest * src
     */
    public DataType mul(DataType src, DataType dest) {
        String result;
        result=cornerCheck(mulCorner,src.toString(),dest.toString());
        if(result!=null){
            return new DataType(result);
        }
        String src1=src.toString();
        String dest1=dest.toString();
        if (src1.matches(IEEE754Float.NaN_Regular) || dest1.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        System.out.println("src:"+src1);
        System.out.println("dest:"+dest1);
        int src_sign=src1.charAt(0)-'0';
        int dest_sign=dest1.charAt(0)-'0';
        String exsrc=src1.substring(1,9);
        String exdest=dest1.substring(1,9);
        String sig_src;
        String sig_dest;
        String sig;
        int sign=src_sign^dest_sign;
        if (Integer.parseInt(exsrc)==0){
            sig_src="0"+src1.substring(9)+"000";
            exsrc=addone(exsrc);
        }else{
            sig_src="1"+src1.substring(9)+"000";
        }if (Integer.parseInt(exdest)==0){
            sig_dest="0"+dest1.substring(9)+"000";
            exdest=addone(exdest);
        }else{
            sig_dest="1"+dest1.substring(9)+"000";
        }
        sig=mul(sig_src,sig_dest);
        System.out.println(exdest);
        System.out.println(exsrc);
        System.out.println(sig);
        int len=sig.length();
        int ex=Integer.valueOf(exsrc,2)+Integer.parseInt(exdest,2)-254+1;
        System.out.println(ex);
        //规格化;
        while (sig.charAt(0)=='0' && ex>-127){
            sig=sig.substring(1)+"0";
            ex-=1;
        }
        while(ex<-127 && Integer.parseInt(sig.substring(0,27),2)!=0){
            sig="0"+sig.substring(0,len-1);
            ex+=1;
        }
        System.out.println(sig);
        System.out.println(ex);

        if(ex>127&&sign==0){
            return new DataType("01111111100000000000000000000000");
        }else if(ex>127&&sign==1){
            return new DataType("11111111100000000000000000000000");
        }else if(ex<-127&&sign==0){
            return new DataType("00000000000000000000000000000000");
        }else if(ex<-127&&sign==1){
            return new DataType("10000000000000000000000000000000");
        }else if (ex==-127){
            sig="0"+sig.substring(0,len-1);
        }
        char _sign= sign==0 ? '0':'1';
        System.out.println(round(_sign,intToBin(ex+127),sig));
        return new DataType(round(_sign,intToBin(ex+127),sig));
    }


    /**
     * compute the float mul of dest / src
     */
    public DataType div(DataType src, DataType dest) {
        String result;
        result=cornerCheck(divCorner,src.toString(),dest.toString());
        if(result!=null){
            return new DataType(result);
        }
        String dest1=dest.toString();
        String src1=src.toString();
        if (src1.matches(IEEE754Float.NaN_Regular) || dest1.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        if(src1.equals("00000000000000000000000000000000") | "10000000000000000000000000000000".equals(src1)){
            throw new ArithmeticException();
        }else if(dest1.equals("00000000000000000000000000000000") | "10000000000000000000000000000000".equals(dest1)){
            return new DataType(dest1);
        }
        int src_sign=src1.charAt(0)-'0';
        int dest_sign=dest1.charAt(0)-'0';
        String exsrc=addone(src1.substring(1,9));
        String exdest=addone(dest1.substring(1,9));
        String sig_src="1"+src1.substring(9)+"000";
        String sig_dest="1"+dest1.substring(9)+"000";
        String sig;
        String exponend=add(exdest,negetive(exsrc));
        int sign=(dest_sign^src_sign);
        char sign_= sign==0 ? '0':'1';
        sig=div(sig_dest,sig_src);
        if (sig.charAt(0)=='0' && Integer.parseInt(exponend,2)>-126){
            exponend=add(exponend,"11111111");
            sig=sig.substring(0,sig.length()-1)+"0";
        }
        return new DataType(round(sign_,add(intToBin(127),exponend),sig.substring(0,27)));
    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) &&
                    oprB.equals(matrix[1])) {
                return matrix[2];
            }
        }
        return null;
    }

    /**
     * right shift a num without considering its sign using its string format
     *
     * @param operand to be moved
     * @param n       moving nums of bits
     * @return after moving
     */
    private String rightShift(String operand, int n) {
        StringBuilder result = new StringBuilder(operand);  //保证位数不变
        boolean sticky = false;
        for (int i = 0; i < n; i++) {
            sticky = sticky || result.toString().endsWith("1");
            result.insert(0, "0");
            result.deleteCharAt(result.length() - 1);
        }
        if (sticky) {
            result.replace(operand.length() - 1, operand.length(), "1");
        }
        return result.substring(0, operand.length());
    }

    /**
     * 对GRS保护位进行舍入
     *
     * @param sign    符号位
     * @param exp     阶码
     * @param sig_grs 带隐藏位和保护位的尾数
     * @return 舍入后的结果
     */
    private String round(char sign, String exp, String sig_grs) {
        int grs = Integer.parseInt(sig_grs.substring(24, 27), 2);
        if ((sig_grs.substring(27).contains("1")) && (grs % 2 == 0)) {
            grs++;
        }
        String sig = sig_grs.substring(0, 24); // 隐藏位+23位
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
        }
        if (exp.equals("11111111")) {
            return IEEE754Float.P_INF;
        }

        return sign + exp + sig.substring(sig.length() - 23);
    }

    /**
     * add one to the operand
     *
     * @param operand the operand
     * @return result after adding, the first position means overflow (not equal to the carray to the next) and the remains means the result
     */
    private String oneAdder(String operand) {
        int len = operand.length();
        StringBuffer temp = new StringBuffer(operand);
        temp = temp.reverse();
        int[] num = new int[len];
        for (int i = 0; i < len; i++) num[i] = temp.charAt(i) - '0';  //先转化为反转后对应的int数组
        int bit = 0x0;
        int carry = 0x1;
        char[] res = new char[len];
        for (int i = 0; i < len; i++) {
            bit = num[i] ^ carry;
            carry = num[i] & carry;
            res[i] = (char) ('0' + bit);  //显示转化为char
        }
        String result = new StringBuffer(new String(res)).reverse().toString();
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') + result;  //注意有进位不等于溢出，溢出要另外判断
    }

    /**
     * 无符号乘法；
     * @param src
     * @param dest
     * @return
     */
    private String mul(String src,String dest){
        String register;
        dest="0"+dest;
        int len =src.length();
        char[] zero=new char[len+1];
        int[] src_int=new int[len];
        int y2;
        for (int i=0;i<len;i++){
            src_int[i]=src.charAt(i)-'0';
            zero[i]='0';
        }zero[len]='0';
        register=String.valueOf(zero)+src;
        for (int i=0;i<len;i++){
            y2=src_int[len-1-i];
            if(y2==1){
                register=add(register.substring(0,len+1),dest)+register.substring(len+1);
            }register="0"+register.substring(0,len*2);
        }
        return register.substring(1);
    }
    private  String add(String src,String dest){
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
    private String negetive(String str){
        int len=str.length();
        char[] opp=new char[len];
        for (int i=0;i<len;i++){
            opp[i]=str.charAt(i)=='1'?'0':'1';
        }
        return addone(String.valueOf(opp));
    }
    private String addone(String str){
        int len =str.length();
        char [] one=new char[len];
        for (int i=0;i<len-1;i++){
            one [i]='0';
        }one[len-1]='1';
        return add(str,String.valueOf(one));
    }

    /**
     * 这是个什么除法啊aaaaa
     * @param dest
     * @param src
     * @return
     */
    private String div(String dest,String src){
        String register;
        int len =dest.length();
        char[] zero=new char[len];
        String reminder ;
        for (int i=0;i<len;i++){
            zero[i]='0';
        }
        register="0"+dest+String.valueOf(zero);
        src="0"+src;
        reminder=add(register.substring(0,len+1),negetive(src));
        register=reminder+register.substring(len+1);
        for (int i=0;i<len;i++){
            if(reminder.charAt(0)=='0'){
                register=register.substring(1)+"1";
                reminder=add(register.substring(0,len+1),negetive(src));
            }else{
                register=register.substring(1)+"0";
                reminder=add(register.substring(0,len+1),src);
            }
            register=reminder+register.substring(len+1);
        }

        if (reminder.charAt(0)=='0'){
            return register.substring(len+1)+"1";
        }else {return register.substring(len+1)+"0";}
    }


    private String intToBin(int num){
        char[]result=new char[8];
        for (int i=0;i<8;i++){
            if(num%2==0){
                result[7-i]='0';
            }else{result[7-i]='1';}
            num=num/2;
        }
        return String.valueOf(result);
    }

}
