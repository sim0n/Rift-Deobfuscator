package net.rifttech.deobfuscator.manager.impl;

import lombok.Getter;
import net.rifttech.deobfuscator.Deobfuscator;
import net.rifttech.deobfuscator.config.Conf;
import net.rifttech.deobfuscator.manager.Manager;
import net.rifttech.deobfuscator.util.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

@Getter
public class ClassManager implements Manager {
    private final Deobfuscator deobfuscator = Deobfuscator.getInstance();

    private final List<ClassNode> classes = new ArrayList<>();
    private final Map<String, byte[]> resources = new HashMap<>();

    public void init() {
        Conf conf = deobfuscator.getConfig().getConf();

        File inputFile = new File(conf.getInputFile());

        try (JarInputStream in = new JarInputStream(new FileInputStream(inputFile))) {
            JarEntry entry;

            while ((entry = in.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                /*if (entry.getName().equals(conf.getMainClass() + ".class"))
                    continue;*/

                if (entryName.endsWith(".class")) {
                    try {
                        ClassReader reader = new ClassReader(in);
                        ClassNode classNode = new ClassNode();

                        reader.accept(classNode, ClassReader.SKIP_DEBUG);

                        classes.add(classNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (!entry.isDirectory()) {
                    resources.put(entryName, FileUtil.readBytes(in));
                }
            }
        } catch (IOException e) {
            deobfuscator.getLogger().warn(String.format("Unable to find input file \"%s\"", inputFile.getName()));
        }
    }

    /**
     * Exports the classes to a jar
     */
    public void exportClasses() {
        Conf conf = deobfuscator.getConfig().getConf();

        File inputFile = new File(conf.getInputFile());
        File outputFile = new File(inputFile.getName().replace(".jar", "-processed.jar"));

        try (JarOutputStream out = new JarOutputStream(new FileOutputStream(outputFile))) {
            classes.forEach(clazz -> {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

                clazz.accept(writer);

                try {
                    out.putNextEntry(new JarEntry(clazz.name + ".class"));
                    out.write(writer.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
