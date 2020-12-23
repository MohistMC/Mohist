/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code)
 * Copyright © 2010 Thomas J. Hodge (java port of libnoise)
 * 
 * This file is part of libnoiseforjava.
 * 
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be found at 
 * http://libnoise.sourceforge.net/.  libnoise was developed by Jason Bevins, who may be 
 * contacted at jlbezigvins@gmzigail.com (for great email, take off every 'zig').
 * Porting to Java was done by Thomas Hodge, who may be contacted at
 * libnoisezagforjava@gzagmail.com (remove every 'zag').
 * 
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package libnoiseforjava.module;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.exception.ExceptionNoModule;

public class Displace extends ModuleBase
{
   /// Noise module that uses three source modules to displace each
   /// coordinate of the input value before returning the output value from
   /// a source module.
   ///
   /// Unlike most other noise modules, the index value assigned to a source
   /// module determines its role in the displacement operation:
   /// - Source module 0 (left in the diagram) outputs a value.
   /// - Source module 1 (lower left in the diagram) specifies the offset to
   ///   apply to the @a x coordinate of the input value.
   /// - Source module 2 (lower center in the diagram) specifies the
   ///   offset to apply to the @a y coordinate of the input value.
   /// - Source module 3 (lower right in the diagram) specifies the offset
   ///   to apply to the @a z coordinate of the input value.
   ///
   /// The getValue() method modifies the ( @a x, @a y, @a z ) coordinates of
   /// the input value using the output values from the three displacement
   /// modules before retrieving the output value from the source module.
   ///
   /// The Turbulence noise module is a special case of the
   /// Displace module; internally, there are three Perlin-noise modules
   /// that perform the displacement operation.
   ///
   /// This noise module requires four source modules.

   public Displace (ModuleBase sourceModuleOne, ModuleBase sourceModuleTwo,
         ModuleBase sourceModuleThree, ModuleBase sourceModuleFour) throws ExceptionInvalidParam
   {
      super(4);
      setSourceModule(0, sourceModuleOne);
      setSourceModule(1, sourceModuleTwo);
      setSourceModule(2, sourceModuleThree);
      setSourceModule(3, sourceModuleFour);
   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);
      assert (sourceModules[1] != null);
      assert (sourceModules[2] != null);
      assert (sourceModules[3] != null);

      // Get the output values from the three displacement modules.  Add each
      // value to the corresponding coordinate in the input value.
      double xDisplace = x + (sourceModules[1].getValue (x, y, z));
      double yDisplace = y + (sourceModules[2].getValue (x, y, z));
      double zDisplace = z + (sourceModules[3].getValue (x, y, z));

      // Retrieve the output value using the offset input value instead of
      // the original input value.
      return sourceModules[0].getValue (xDisplace, yDisplace, zDisplace);
   }

   public ModuleBase getXDisplaceModule() throws ExceptionNoModule
   {
      if (sourceModules == null || sourceModules[1] == null)  
         throw new ExceptionNoModule ("Could not retrieve a source module " +
         "from a noise module.");

      return sourceModules[1];
   }

   /// Returns the @a y displacement module.
   ///
   /// @returns A reference to the @a y displacement module.
   ///
   /// @pre This displacement module has been added to this noise module
   /// via a call to setSourceModule() or setYDisplaceModule().
   ///
   /// @throw ExceptionNoModule See the preconditions for more
   /// information.
   ///
   /// The getValue() method displaces the input value by adding the output
   /// value from this displacement module to the @a y coordinate of the
   /// input value before returning the output value from the source
   /// module.
   public ModuleBase getYDisplaceModule () throws ExceptionNoModule
   {
      if (sourceModules == null || sourceModules[2] == null)
         throw new ExceptionNoModule ("Could not retrieve a source module " +
         "from Displace noise module.");

      return sourceModules[2];
   }

   /// Returns the @a z displacement module.
   ///
   /// @returns A reference to the @a z displacement module.
   ///
   /// @pre This displacement module has been added to this noise module
   /// via a call to setSourceModule() or setZDisplaceModule().
   ///
   /// @throw ExceptionNoModule See the preconditions for more
   /// information.
   ///
   /// The getValue() method displaces the input value by adding the output
   /// value from this displacement module to the @a z coordinate of the
   /// input value before returning the output value from the source
   /// module.
   public ModuleBase getZDisplaceModule () throws ExceptionNoModule
   {
      if (sourceModules == null || sourceModules[3] == null)
         throw new ExceptionNoModule ("Could not retrieve a source module " +
         "from Displace noise module.");

      return sourceModules[3];
   }


   /// Sets the @a x, @a y, and @a z displacement modules.
   ///
   /// @param xDisplaceModule Displacement module that displaces the @a x
   /// coordinate of the input value.
   /// @param yDisplaceModule Displacement module that displaces the @a y
   /// coordinate of the input value.
   /// @param zDisplaceModule Displacement module that displaces the @a z
   /// coordinate of the input value.
   ///
   /// The getValue() method displaces the input value by adding the output
   /// value from each of the displacement modules to the corresponding
   /// coordinates of the input value before returning the output value
   /// from the source module.
   ///
   /// This method assigns an index value of 1 to the @a x displacement
   /// module, an index value of 2 to the @a y displacement module, and an
   /// index value of 3 to the @a z displacement module.
   ///
   /// These displacement modules must exist throughout the lifetime of
   /// this noise module unless another displacement module replaces it.
   public void setDisplaceModules (ModuleBase xDisplaceModule,
         ModuleBase yDisplaceModule, ModuleBase zDisplaceModule)
   {
      setXDisplaceModule (xDisplaceModule);
      setYDisplaceModule (yDisplaceModule);
      setZDisplaceModule (zDisplaceModule);
   }

   /// Sets the @a x displacement module.
   ///
   /// @param xDisplaceModule Displacement module that displaces the @a x
   /// coordinate.
   ///
   /// The getValue() method displaces the input value by adding the output
   /// value from this displacement module to the @a x coordinate of the
   /// input value before returning the output value from the source
   /// module.
   ///
   /// This method assigns an index value of 1 to the @a x displacement
   /// module.  Passing this displacement module to this method produces
   /// the same results as passing this displacement module to the
   /// setSourceModule() method while assigning it an index value of 1.
   ///
   /// This displacement module must exist throughout the lifetime of this
   /// noise module unless another displacement module replaces it.
   public void setXDisplaceModule (ModuleBase xDisplaceModule)
   {
      assert (sourceModules != null);
      sourceModules[1] = xDisplaceModule;
   }

   /// Sets the @a y displacement module.
   ///
   /// @param yDisplaceModule Displacement module that displaces the @a y
   /// coordinate.
   ///
   /// The getValue() method displaces the input value by adding the output
   /// value from this displacement module to the @a y coordinate of the
   /// input value before returning the output value from the source
   /// module.
   ///
   /// This method assigns an index value of 2 to the @a y displacement
   /// module.  Passing this displacement module to this method produces
   /// the same results as passing this displacement module to the
   /// setSourceModule() method while assigning it an index value of 2.
   ///
   /// This displacement module must exist throughout the lifetime of this
   /// noise module unless another displacement module replaces it.
   public void setYDisplaceModule (ModuleBase yDisplaceModule)
   {
      assert (sourceModules != null);
      sourceModules[2] = yDisplaceModule;
   }

   /// Sets the @a z displacement module.
   ///
   /// @param zDisplaceModule Displacement module that displaces the @a z
   /// coordinate.
   ///
   /// The getValue() method displaces the input value by adding the output
   /// value from this displacement module to the @a z coordinate of the
   /// input value before returning the output value from the source
   /// module.
   ///
   /// This method assigns an index value of 3 to the @a z displacement
   /// module.  Passing this displacement module to this method produces
   /// the same results as passing this displacement module to the
   /// setSourceModule() method while assigning it an index value of 3.
   ///
   /// This displacement module must exist throughout the lifetime of this
   /// noise module unless another displacement module replaces it.
   public void setZDisplaceModule (ModuleBase zDisplaceModule)
   {
      assert (sourceModules != null);
      sourceModules[3] = zDisplaceModule;
   }

}
