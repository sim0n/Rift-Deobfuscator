package net.rifttech.deobfuscator.transformer.impl.generic.number;

import net.rifttech.deobfuscator.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * This simplifies long xor operations
 * Example: 4127412L ^ 4819244L
 */
public class LongXorNumber extends Transformer {
    public LongXorNumber() {
        super("Successfully simplified {} long xor operations.",
                IS_LONG,
                IS_LONG,
                insn -> insn.getOpcode() == LXOR);
    }

    @Override
    public AbstractInsnNode handle(AbstractInsnNode insn, AbstractInsnNode previous, InsnList instructions) {
        long a = (long) ((LdcInsnNode) previous.getPrevious()).cst;
        long b = (long) ((LdcInsnNode) previous).cst;

        removeInstructions(insn, instructions);

        instructions.set(insn, insn = new LdcInsnNode(a ^ b));

        return insn;
    }
}

