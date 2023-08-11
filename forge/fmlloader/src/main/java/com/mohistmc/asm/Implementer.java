package com.mohistmc.asm;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.tree.ClassNode;

/**
 * Implementer
 *
 * @author Mainly by IzzelAliz
 * @originalClassName Implementer
 */
public interface Implementer {

    boolean processClass(ClassNode node, ILaunchPluginService.ITransformerLoader transformerLoader);
}
