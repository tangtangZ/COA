package cpu.instr.all_instrs;

public class Hlt implements Instruction {
    @Override
    public int exec(int opcode) {
        toBinaryStr("11110100");
        return -1;
    }

    @Override
    public String fetchInstr(String eip, int opcode) {
        return null;
    }

    @Override
    public boolean isIndirectAddressing() {
        return false;
    }

    @Override
    public void fetchOperand() {

    }
}
