package cpu.instr;
import cpu.CPU_State;
import cpu.mmu.MMU;
import  util.Transformer;

public class Sub implements Instruction{
    private final MMU mmu = MMU.getMMU();
    private int len = 0;
    private String instr;
    @Override
    public int exec(int opcode) {
        if(opcode==0x2D){
            len=5;
            instr=String.valueOf(mmu.read(CPU_State.cs.read() + CPU_State.eip.read(), len));
            String dest=CPU_State.eax.read();
            String imme=MMU.ToBitStream(instr.substring(1,5));
            Transformer t=new Transformer();
            dest=t.intToBinary(String.valueOf(Integer.valueOf(dest,2)-Integer.parseInt(imme,2)));
            CPU_State.eax.write(dest);
        }

        return len;
    }

}
