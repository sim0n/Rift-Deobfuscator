package net.rifttech.deobfuscator;

import lombok.Getter;
import net.rifttech.deobfuscator.config.Config;
import net.rifttech.deobfuscator.logger.Logger;
import net.rifttech.deobfuscator.manager.impl.ClassManager;
import net.rifttech.deobfuscator.manager.impl.TransformerManager;

import java.util.Optional;

@Getter
public class Deobfuscator {
    private static Optional<Deobfuscator> instance;

    private final Logger logger = new Logger();

    private TransformerManager transformerManager;
    private ClassManager classManager;

    private Config config;

    public Deobfuscator() {
        instance = Optional.of(this);
    }

    /**
     * This function starts the obfuscation process
     */
    public void start() {
        long startTime = System.currentTimeMillis();

        (config = new Config()).start();

        (transformerManager = new TransformerManager()).init();
        (classManager = new ClassManager()).init();


        logger.printLogo();

        transformerManager.getTransformers().forEach(transformer -> {
            classManager.getClasses().forEach(transformer::visit);

            transformer.handleOutput();
        });

        classManager.exportClasses();

        logger.emptyLine();
        logger.info(String.format("Finished deobfuscating target in %dms", System.currentTimeMillis() - startTime));
    }

    /**
     * Gets the Deobfuscator instance
     * @return The deobfuscator instance
     * @throws IllegalStateException if the instance is null
     */
    public static Deobfuscator getInstance() {
        return instance.orElseThrow(() -> new IllegalStateException("Deobfuscator instance is null."));
    }
}
