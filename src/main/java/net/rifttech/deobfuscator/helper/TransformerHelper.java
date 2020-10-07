package net.rifttech.deobfuscator.helper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.function.Predicate;

/**
 * This is to help us eliminate boilerplate code
 */
public interface TransformerHelper extends Opcodes {

    Predicate<AbstractInsnNode> IS_LONG = insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof Long;
    Predicate<AbstractInsnNode> IS_INTEGER = insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof Integer;
    Predicate<AbstractInsnNode> IS_DOUBLE = insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof Double;

    Predicate<AbstractInsnNode> IS_STRING = insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof String;

}
