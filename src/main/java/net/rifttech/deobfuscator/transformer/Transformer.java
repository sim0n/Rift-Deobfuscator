package net.rifttech.deobfuscator.transformer;

import net.rifttech.deobfuscator.Deobfuscator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class Transformer implements Opcodes {
    protected final Deobfuscator deobfuscator = Deobfuscator.getInstance();

    private final String outputFormat;
    protected final List<Predicate<AbstractInsnNode>> pattern;

    protected int counter;

    @SafeVarargs
    public Transformer(String outputFormat, Predicate<AbstractInsnNode>... pattern) {
        this.outputFormat = outputFormat;
        this.pattern = Arrays.asList(pattern);
    }

    /**
     * Visits a class node
     * @param classNode The class node to visit
     */
    public void visit(ClassNode classNode) {
        classNode.methods.stream()
                .filter(methodNode -> methodNode.instructions.size() > 0)
                .forEach(methodNode -> {
                    InsnList instructions = methodNode.instructions;

                    AbstractInsnNode insn = instructions.getFirst();

                    int stage = 0;

                    // We use a do while loop because we want it to execute at least once
                    do {
                        if (pattern.get(stage).test(insn)) {
                            if (++stage == pattern.size()) { // We reached the final stage

                                // Handle the actual deobfuscation and replace the instruction with our own
                                insn = handle(insn, insn.getPrevious(), instructions);

                                ++counter;

                                // Reset the stage so we can deobfuscate more stuff
                                stage = 0;
                            }
                        } else {
                            stage = 0;
                        }
                    } while ((insn = insn.getNext()) != null);
                });
    }

    /**
     * Removes old instructions
     * @param insn The instruction
     * @param instructions The instruction list
     */
    public void removeInstructions(AbstractInsnNode insn, InsnList instructions) {
        for (int i = 0; i < pattern.size() - 1; ++i)
            instructions.remove(insn.getPrevious());
    }

    public abstract AbstractInsnNode handle(AbstractInsnNode insn, AbstractInsnNode previous, InsnList instructions);

    /**
     * Handles the output, uses the {@link Transformer#outputFormat}
     */
    public void handleOutput() {
        if (counter == 0) // We didn't change anything so we don't output it
            return;

        String outputMessage = outputFormat.replace("{}", "x" + counter);

        deobfuscator.getLogger().info(outputMessage);
    }

}
