package net.rifttech.deobfuscator.manager.impl;

import lombok.Getter;
import net.rifttech.deobfuscator.manager.Manager;
import net.rifttech.deobfuscator.transformer.Transformer;
import net.rifttech.deobfuscator.transformer.impl.generic.IntXorNumber;
import net.rifttech.deobfuscator.transformer.impl.generic.LongXorNumber;
import net.rifttech.deobfuscator.transformer.impl.radon.XorString;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class TransformerManager implements Manager {
    private static boolean AUTOMATIC = false;

    private final List<Transformer> transformers = new ArrayList<>();

    @Override
    public void init() {
        if (AUTOMATIC) {
            ClassIndex.getSubclasses(Transformer.class, TransformerManager.class.getClassLoader())
                    .forEach(clazz -> {
                        if (Modifier.isAbstract(clazz.getModifiers()))
                            return;

                        try {
                            transformers.add(clazz.newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
            return;
        }

        transformers.addAll(Arrays.asList(new IntXorNumber(), new LongXorNumber(), new XorString()));
    }
}
