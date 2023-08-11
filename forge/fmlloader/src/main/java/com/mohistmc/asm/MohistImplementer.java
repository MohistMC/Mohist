package com.mohistmc.asm;

import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * MohistImplementer
 *
 * @author Mainly by IzzelAliz
 * @originalClassName ArclightImplementer
 */
public class MohistImplementer implements ILaunchPluginService {

    private static final EnumSet<Phase> OH_YES_SIR = EnumSet.of(Phase.AFTER);
    private static final EnumSet<Phase> NOT_TODAY = EnumSet.noneOf(Phase.class);
    private final Map<String, Implementer> implementers = new HashMap<>();
    private volatile Consumer<String[]> auditAcceptor;
    private ITransformerLoader transformerLoader;

    @Override
    public String name() {
        return "mohist_implementer";
    }

    @Override
    public void initializeLaunch(ITransformerLoader transformerLoader, NamedPath[] specialPaths) {
        this.transformerLoader = transformerLoader;
        this.implementers.put("switch", SwitchTableFixer.INSTANCE);
        this.implementers.put("enum", new EnumDefinalizer());
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty, String reason) {
        if ("mixin".equals(reason)) {
            return NOT_TODAY;
        }
        return isEmpty ? NOT_TODAY : OH_YES_SIR;
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        throw new IllegalStateException("Outdated ModLauncher");
    }

    @Override
    public void customAuditConsumer(String className, Consumer<String[]> auditDataAcceptor) {
        auditAcceptor = auditDataAcceptor;
    }


    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType, String reason) {
        if ("mixin".equals(reason)) {
            return false;
        }
        List<String> trails = new ArrayList<>();
        for (Map.Entry<String, Implementer> entry : implementers.entrySet()) {
            String key = entry.getKey();
            Implementer implementer = entry.getValue();
            if (implementer.processClass(classNode, transformerLoader)) {
                trails.add(key);
            }
        }
        if (this.auditAcceptor != null && !trails.isEmpty()) {
            this.auditAcceptor.accept(new String[]{String.join(",", trails)});
        }
        return !trails.isEmpty();
    }
}
