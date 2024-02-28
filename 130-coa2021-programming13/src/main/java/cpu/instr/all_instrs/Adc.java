package cpu.instr.all_instrs;

import cpu.CPU_State;
import cpu.MMU;
import cpu.alu.ALU;
import cpu.instr.decode.Operand;
import cpu.instr.decode.OperandType;
import cpu.registers.CS;
import cpu.registers.EFlag;

import java.util.Arrays;

public class Adc implements Instruction {

    private MMU mmu = MMU.getMMU();
    private CS cs = (CS) CPU_State.cs;
    private EFlag eflag = (EFlag) CPU_State.eflag;
    private int len;
    private String instr;

    @Override
    public int exec(int opcode) {
        ALU alu = new ALU();
        if (opcode == 0x15) {
            Operand imm = new Operand();
            imm.setVal(instr.substring(8, 40));
            imm.setType(OperandType.OPR_IMM);

            char[] cf_imple = new char[32];
            Arrays.fill(cf_imple, '0');
            if (eflag.getCF()) {
                cf_imple[31] = '1';
            }

            String tmpRes = alu.add(imm.getVal(), CPU_State.eax.read());
            CPU_State.eax.write(alu.add(tmpRes, String.valueOf(cf_imple)));
        }
        return len;
    }


    @Override
    public String fetchInstr(String eip, int opcode) {
        len = 8 + 32;
        instr = String.valueOf(mmu.read(cs.read() + eip, len));
        return instr;
    }

    @Override
    public boolean isIndirectAddressing() {
        return false;
    }

    @Override
    public void fetchOperand() {

    }

}
