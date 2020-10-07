package net.rifttech.deobfuscator.transformer.impl.radon;

import net.rifttech.deobfuscator.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class XorString extends Transformer {
    public XorString() {
        super("Successfully decrypted {} strings.",
                insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof String, // encrypted string
                insn -> insn.getOpcode() == LDC && ((LdcInsnNode) insn).cst instanceof Integer, // encryption key
                insn -> {
                    if (insn.getOpcode() == INVOKESTATIC) {
                        MethodInsnNode method = (MethodInsnNode) insn;

                        int asciiCode = method.name.toCharArray()[0];

                        // We check if the ascii code of the character is out of the English alphabet
                        return asciiCode > 122 && method.desc.equals("(Ljava/lang/String;I)Ljava/lang/String;");
                    } else {
                        return false;
                    }
                });
    }

    @Override
    public AbstractInsnNode handle(AbstractInsnNode insn, AbstractInsnNode previous, InsnList instructions) {
        String encryptedString = (String) ((LdcInsnNode) previous.getPrevious()).cst;

        int key = (int) ((LdcInsnNode) previous).cst;

        removeInstructions(insn, instructions);

        instructions.set(insn, insn = new LdcInsnNode(xor(encryptedString, key)));

        return insn;
    }

    public static String xor(String string, int key) {
        char[] charArray = string.toCharArray();
        char[] array = new char[charArray.length];

        for (int i = 0; i < array.length; ++i)
            array[i] = (char) (charArray[i] ^ key);

        return new String(array);
    }

}
