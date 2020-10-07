package net.rifttech.deobfuscator.transformer.impl.generic.number;

import net.rifttech.deobfuscator.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

/**
 * This simplifies int xor operations
 * Example: 4127412 ^ 4819244
 */
public class IntXorNumber extends Transformer {
    public IntXorNumber() {
        super("Successfully simplified {} int xor operations.",
                IS_INTEGER,
                IS_INTEGER,
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
