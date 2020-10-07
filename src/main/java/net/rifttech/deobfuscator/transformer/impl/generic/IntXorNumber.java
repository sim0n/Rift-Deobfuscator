package net.rifttech.deobfuscator.transformer.impl.generic;

import net.rifttech.deobfuscator.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * This simplifies int xor operations
 */
public class IntXorNumber extends Transformer {
    public IntXorNumber() {
        super("Successfully simplified {} int xor operations.",
                insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof Integer,
                insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof Integer,
                insn -> insn.getOpcode() == IXOR);
    }

    @Override
    public AbstractInsnNode handle(AbstractInsnNode insn, AbstractInsnNode previous, InsnList instructions) {
        int a = (int) ((LdcInsnNode) previous.getPrevious()).cst;
        int b = (int) ((LdcInsnNode) previous).cst;

        removeInstructions(insn, instructions);

        instructions.set(insn, insn = new LdcInsnNode(a ^ b));

        return insn;
    }
}
