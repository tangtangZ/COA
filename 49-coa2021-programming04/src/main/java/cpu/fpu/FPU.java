package cpu.fpu;

import util.DataType;
import util.IEEE754Float;
import cpu.alu.ALU;

/**
 * floating point unit
 * 执行浮点运算的抽象单元
 * 浮点数精度：使用3位保护位进行计算
 */
public class FPU {

    private final String[][] addCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.N_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.P_INF, IEEE754Float.NaN}
    };

    private final String[][] subCorner = new String[][]{
            {IEEE754Float.P_ZERO, IEEE754Float.P_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.P_ZERO, IEEE754Float.N_ZERO},
            {IEEE754Float.P_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.N_ZERO, IEEE754Float.N_ZERO, IEEE754Float.P_ZERO},
            {IEEE754Float.P_INF, IEEE754Float.P_INF, IEEE754Float.NaN},
            {IEEE754Float.N_INF, IEEE754Float.N_INF, IEEE754Float.NaN}
    };

    /**
     * compute the float add of (dest + src)
     */
    public DataType add(DataType src, DataType dest) {

        ALU alu=new ALU();
        FPU eg=new FPU();

        String a = dest.toString();
        String b = src.toString();
        if(a.equals("00000000100000000000000000000000") && b.equals("00000000000000000000000000000001")){
            int i=0;
        }
        System.out.println("a="+a);
        System.out.println("b="+b);
        if (a.matches(IEEE754Float.NaN_Regular) || b.matches(IEEE754Float.NaN_Regular)) {
            return new DataType(IEEE754Float.NaN);
        }
        String corner = cornerCheck(addCorner, a, b);
        if (corner != null) {
            return new DataType(corner);
        }
        if (a == IEEE754Float.P_ZERO | a == IEEE754Float.N_ZERO) {
            return src;
        } else if (b == IEEE754Float.P_ZERO | b == IEEE754Float.N_ZERO) {
            return dest;
        }
        // 提取符号、阶码、尾数;
        int sign_a = Integer.parseInt(a.substring(0, 1));
        int sign_b = Integer.parseInt(b.substring(0, 1));
        int exponent_a = Integer.parseInt(a.substring(1, 9),2);
        int exponent_b = Integer.parseInt(b.substring(1, 9),2);
        String exponent="";
        String sig_a="";
        String sig_b="";
        String sign="";
        if(exponent_a-exponent_b>40){
            return dest;
        }else if(exponent_b-exponent_a>40){
            return src;
        }
        if(b.substring(1).equals(a.substring(1))&sign_b+sign_a==1){
            return new DataType("00000000000000000000000000000000");
        }
        if (exponent_a != 0) {
            sig_a = "1" + a.substring(9) + "000";
        } else {
            sig_a = "0" + a.substring(9) + "000";
        }
        if (exponent_b != 0) {
            sig_b = "1" + b.substring(9) + "000";
        } else {
            sig_b = "0" + b.substring(9) + "000";
        }
        //对阶；
       /* if((exponent_a==0&exponent_b==1)|(exponent_b==0&exponent_a==1)){
            exponent="00000001";

        } else if (exponent_a >=exponent_b) {
            sig_b = eg.rightShift(sig_b, exponent_a - exponent_b);
            exponent=a.substring(1,9);
        } else {
            sig_a = eg.rightShift(sig_a, exponent_b - exponent_a);
            exponent=b.substring(1,9);
        }
        System.out.println(exponent);
        //尾数相加；
        if (sign_a-sign_b==0){
            final_sig= eg.addStr(sig_a,sig_b);
            if (Integer.parseInt(exponent)==0& final_sig.substring(0, 2).equals("01")){
                exponent=eg.addOne(exponent);
                final_sig=final_sig.substring(1);
            }else if (final_sig.charAt(0)=='1'){
                exponent=eg.addOne(exponent);
                final_sig=final_sig.substring(0,27);
            }else{
                final_sig=final_sig.substring(1);
            }
            sign=String.valueOf(sign_b);
            if(Integer.parseInt(exponent,2)==255&sign_b==0){
                return new DataType("01111111100000000000000000000000");
            }else if(Integer.parseInt(exponent,2)==255&sign_b==1){
                return new DataType("11111111100000000000000000000000");
            }
        }else{
            sig_a=eg.negation(sig_a);
            final_sig=eg.addStr(sig_a,sig_b);
            if(final_sig.charAt(0)=='0'){
                final_sig=eg.negation(final_sig.substring(1));
                sign= sign_b==1 ? "0":"1";

            }else{
                final_sig=final_sig.substring(1);
                sign=String.valueOf(sign_b);
            }

            while(final_sig.charAt(0)=='0'&Integer.parseInt(exponent,2)>1){
                final_sig=final_sig.substring(1)+"0";
                exponent= eg.addStr(exponent,"11111111").substring(1);
            }
            if(exponent.equals("00000001") & final_sig.charAt(0)=='0'){
                exponent="00000000";
            }
        }

        System.out.println(eg.round(sign.charAt(0),exponent,final_sig));
        return new DataType(round(sign.charAt(0),exponent,final_sig));*/
        //对阶
        String e_a=a.substring(1,9),e_b=b.substring(1,9);
        if(exponent_a==0) {
            exponent_a = 1;
            e_a = "00000001";
        }
        if(exponent_b==0) {
            exponent_b = 1;
            e_b = "00000001";
        }
        if(exponent_a>exponent_b){
            sig_b=eg.rightShift(sig_b,exponent_a-exponent_b);
            exponent=e_a;
        }else{
            sig_a=eg.rightShift(sig_a,exponent_b-exponent_a);
            exponent=e_b;
        }
        //尾数相加；
        String sig="";
        if(sign_a==sign_b)  {
            sig=addStr(sig_a,sig_b);
            sign= String.valueOf(sign_b);
            if(sig.charAt(0)=='1'){
                sig=sig.substring(0,sig.length()-1);
                exponent=addOne(exponent);
            }else sig=sig.substring(1);
            if(exponent.equals("11111111")) return new DataType (sign+"1111111100000000000000000000000");
        }
        else {
            if(sign_a!=sign_b && sign_a==1) sig=addStr(sig_b,negation(sig_a));
            else sig=addStr(sig_a,negation(sig_b));
            if(sig.charAt(0)=='0') {
                sig = negation(sig).substring(1);
                sign = "1";
            }
            else{
                sig = sig.substring(1);
                sign = "0";
            }

        }
        while(sig.charAt(0)=='0' && Integer.parseInt(exponent,2)>1){
            sig=sig.substring(1)+"0";
            exponent=subOne(exponent);
        }
        if(sig.charAt(0)=='0' && Integer.parseInt(exponent,2)==1) exponent=subOne(exponent);

        return new DataType(round(sign.charAt(0),exponent,sig));
    }

    /*注意的问题：
        1.思路：提取（如果阶码为0，加一）、
                对阶、
                尾数相加：同符号时进位为1要左规，注意溢出条件判断；不同符号相加时加上负数的补码（27位），如果没有进位结果取反加一（后27位），符号为负；
                规格化：同符号时注意溢出和高位为一时要左移；符号不同时没有溢出问题，但是可能需要右规（右规的边界条件是阶码大于1；27位尾数高位为0），然后判断当阶码为1时如果尾数高位为0，阶码变为0、
       2. 出错的地方：同符号时的溢出；
                     对阶码为0的处理不当（对阶对了但是阶码取错）
     */
    /**
     * compute the float add of (dest - src)
     */
    public DataType sub(DataType src, DataType dest) {
        String a=src.toString();
        if(a.charAt(0)=='0'){
            a="1"+a.substring(1);
        }else{
            a="0"+a.substring(1);
        }
        src=new DataType(a);
        return add(src,dest);
    }


    private String cornerCheck(String[][] cornerMatrix, String oprA, String oprB) {
        for (String[] matrix : cornerMatrix) {
            if (oprA.equals(matrix[0]) && oprB.equals(matrix[1])) {
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
        int grs = Integer.parseInt(sig_grs.substring(24), 2);
        String sig = sig_grs.substring(0, 24);
        if (grs > 4) {
            sig = oneAdder(sig);
        } else if (grs == 4 && sig.endsWith("1")) {
            sig = oneAdder(sig);
        }

        if (Integer.parseInt(sig.substring(0, sig.length() - 23), 2) > 1) {
            sig = rightShift(sig, 1);
            exp = oneAdder(exp).substring(1);
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
        StringBuilder temp = new StringBuilder(operand);
        temp.reverse();
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
        return "" + (result.charAt(0) == operand.charAt(0) ? '0' : '1') +result;  //注意有进位不等于溢出，溢出要另外判断
    }

    private String addStr(String str1,String str2){
        int len=str1.length();
        char[] addfirst=str1.toCharArray();
        char[] addsecound=str2.toCharArray();
        char[] resultpre=new char[len];
        int[] scrint=new int [len];
        int[] destint=new int [len];
        int Cn=0;
        for (int i=0;i<len;i++){
            scrint[i]=addfirst[i]-48; }
        for (int i=0;i<len;i++){
            destint[i]=(int)addsecound[i]-48; }
        for(int i=len-1;i>=0;i--){
            if (scrint[i]+destint[i]+Cn==3|scrint[i]+destint[i]+Cn==1){
                resultpre[i]='1';}
            else { resultpre[i]='0'; }
            if (scrint[i]+destint[i]+Cn==3|scrint[i]+destint[i]+Cn==2){
                Cn=1;
            }else{Cn=0;}
        }
        return String.valueOf(Cn)+String.valueOf(resultpre);
    }

    private String negation(String operand) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < operand.length(); i++) {
            result = operand.charAt(i) == '1' ? result.append("0") : result.append("1");
        }
        String re=oneAdder(result.toString()).substring(1);
        return re;
    }
    private  int  binToInt(String str){
        int len=str.length();
        int result=0;
        for(int i=0;i<len;i++){
            result+=Math.pow(2,len-i-1)*(str.charAt(i)-'0');
        }
        return result;
    }
    private String subOne(String str){
        return addStr(str,"11111111").substring(1);
    }
    private String addOne(String str){
        return addStr(str,"00000001").substring(1);
    }

}
