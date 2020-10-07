package net.rifttech.deobfuscator.transformer.impl.generic.number;

import net.rifttech.deobfuscator.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.function.Predicate;

/**
 * This simplifies Integer.reverse xor operations
 * Example: int var10000 = (int)((long)Integer.reverse(989407322) ^ (long)Integer.reverse(1996040282));
 */
public class IntReverseNumber extends Transformer {
    private static final Predicate<AbstractInsnNode> IS_INTEGER_REVERSE = insn -> {
        if (insn.getOpcode() == INVOKESTATIC) {
            MethodInsnNode method = (MethodInsnNode) insn;

            return method.owner.equals("java/lang/Integer") && method.name.equals("reverse") && method.desc.equals("(I)I");
        } else {
            return false;
        }
    };

    public IntReverseNumber() {
        super("Computed the value of {} Integer.reverse xor operations",
                IS_INTEGER,
                IS_INTEGER_REVERSE,
                insn -> insn.getOpcode() == I2L,
                IS_INTEGER,
                IS_INTEGER_REVERSE,
                insn -> insn.getOpcode() == I2L,
                insn -> insn.getOpcode() == LXOR,
                insn -> insn.getOpcode() == L2I);
    }

    @Override
    public AbstractInsnNode handle(AbstractInsnNode insn, AbstractInsnNode previous, InsnList instructions) {
        int index = instructions.indexOf(insn);

        int a = (int) ((LdcInsnNode) instructions.get(index - 7)).cst;
        int b = (int) ((LdcInsnNode) instructions.get(index - 4)).cst;

        removeInstructions(insn, instructions);

        instructions.set(insn, insn = new LdcInsnNode(Integer.reverse(a) ^ Integer.reverse(b)));

        return insn;
    }
}
