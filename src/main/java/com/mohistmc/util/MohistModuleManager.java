/*
 * Mohist - MohistMC
 * Copyright (C) 2018-2022.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mohistmc.util;

import java.io.File;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sun.misc.Unsafe;

/**
 * @author Shawiiz_z
 * @version 0.1
 * @date 04/05/2022 22:57
 */
public class MohistModuleManager {
	List<Module> loadedModules = new ArrayList<>();
	private boolean moduleOptionAvailable = false;

	public MohistModuleManager() {
		try {
			/*
			This code allows to call methods in the jdk.internal.module.Modules class.
			If this fails, methods won't be able to be called, but modules can still be loaded.
			 */
			Unsafe unsafe = this.getUnsafe();
			unsafe.putObject(MohistModuleManager.class, unsafe.objectFieldOffset(Class.class.getDeclaredField("module")), Class.class.getModule());
			this.moduleOptionAvailable = true;

			this.addExportsToAllUnnamed("java.base", "sun.security.util");
			this.addOpensToAllUnnamed("java.base", "java.util.jar");
			this.addOpensToAllUnnamed("java.base", "java.lang");
		} catch (Exception e) {
			e.printStackTrace();
			/*
			Adding module options cannot be done dynamically, but we can still add modules dynamically.
			The other way to add module options is run the server jar again with the needed flags.
			 */
		}
	}

	public boolean isModuleOptionAvailable() {
		return this.moduleOptionAvailable;
	}

	/*
	Find and get a module by its name in the current module layer and the modules that are dynamically loaded.
	 */
	public Optional<Module> findModule(String name) {
		return Stream.concat(this.getDefaultModuleLayer().modules().stream(), this.loadedModules.stream()).filter(module -> module.getName().equals(name)).findAny();
	}

	public boolean addOpens(String moduleName, String packageName, String applyTo) {
		return this.addModuleOption("addOpens", moduleName, packageName, applyTo);
	}

	public boolean addOpensToAllUnnamed(String moduleName, String packageName) {
		return this.addModuleOption("addOpensToAllUnnamed", moduleName, packageName, null);
	}

	public boolean addExports(String moduleName, String packageName, String applyTo) {
		return this.addModuleOption("addOpens", moduleName, packageName, applyTo);
	}

	public boolean addExportsToAllUnnamed(String moduleName, String packageName) {
		return this.addModuleOption("addExportsToAllUnnamed", moduleName, packageName, null);
	}

	/*
	Methods that should be used: addExports, addExportsToAllUnnamed, addOpens, addOpensToAllUnnamed

	This method allows to dynamically add a module option
	 */
	private boolean addModuleOption(String methodName, String moduleFrom, String packageName, String moduleTo) {
		try {
			Optional<Module> moduleFrom_ = findModule(moduleFrom);
			Optional<Module> moduleTo_ = findModule(moduleTo);
			if(!moduleFrom_.isPresent()) return false; //The module hasn't been found, we can't add the module option.

			//The target module has been found
			if(moduleTo_.isPresent()) {
				Class.forName("jdk.internal.module.Modules").getMethod(methodName, Module.class, String.class, Module.class).invoke(null, moduleFrom_.get(), packageName, moduleTo_.get());
			} else if(methodName.endsWith("Unnamed")) {
				//The target module hasn't been found, the only option is to use allUnnamed methods.
				Class.forName("jdk.internal.module.Modules").getMethod(methodName, Module.class, String.class).invoke(null, moduleFrom_.get(), packageName);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	Load only one module dynamically (actually calling loadModules method but with one argument)
	 */
	public void loadModule(File moduleFile) {
		this.loadModules(moduleFile);
	}

	/*
	Load one or multiples module(s) dynamically.
	 */
	public void loadModules(File... moduleFiles) {
		ModuleLayer currentModuleLayer = this.getDefaultModuleLayer();
		final ModuleFinder moduleFinder = ModuleFinder.of(Arrays.stream(moduleFiles).map(File::toPath).toArray(Path[]::new));
		final Set<String> moduleNames = moduleFinder.findAll().stream().map(moduleRef -> moduleRef.descriptor().name()).collect(Collectors.toSet());
		final Configuration configuration = currentModuleLayer.configuration().resolveAndBind(moduleFinder, ModuleFinder.of(), moduleNames);
		final ModuleLayer moduleLayer = currentModuleLayer.defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());
		this.loadedModules.addAll(moduleLayer.modules());
	}

	/*
	Get the module layer (calling ModuleLayer.boot)
	 */
	private ModuleLayer getDefaultModuleLayer() {
		return ModuleLayer.boot();
	}

	/*
	Get the unsafe class instance
	 */
	public Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		return (Unsafe) f.get(null);
	}
}

