package net.rifttech.deobfuscator.transformer.impl.generic.number;

import net.rifttech.deobfuscator.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * This simplifies a multiplication with doubles
 * Example: 0.418274 * 1.247827
 */
public class DoubleMulNumber extends Transformer {
    public DoubleMulNumber() {
        super("Simplified {} multiplication operations",
                IS_DOUBLE,
                IS_DOUBLE,
                insn -> insn.getOpcode() == DMUL);
    }

    @Override
    public AbstractInsnNode handle(AbstractInsnNode insn, AbstractInsnNode previous, InsnList instructions) {
        double a = (double) ((LdcInsnNode) previous.getPrevious()).cst;
        double b = (double) ((LdcInsnNode) previous).cst;

        removeInstructions(insn, instructions);

        instructions.set(insn, insn = new LdcInsnNode(a * b));

        return insn;
    }
}
