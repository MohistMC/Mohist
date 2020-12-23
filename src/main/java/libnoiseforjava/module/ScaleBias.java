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

public class ScaleBias extends ModuleBase
{

   /// Noise module that applies a scaling factor and a bias to the output
   /// value from a source module.
   ///
   /// The getValue() method retrieves the output value from the source
   /// module, multiplies it with a scaling factor, adds a bias to it, then
   /// outputs the value.
   ///
   /// This noise module requires one source module.

   /// Default bias for the ScaleBias noise module.
   static final double DEFAULT_BIAS = 0.0;

   /// Default scale for the ScaleBias noise module.
   static final double DEFAULT_SCALE = 1.0;

   /// Bias to apply to the scaled output value from the source module.
   double bias;

   /// Scaling factor to apply to the output value from the source
   /// module.
   double scale;

   
   public ScaleBias (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);
      bias = DEFAULT_BIAS;
      scale = DEFAULT_SCALE;
   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);

      return sourceModules[0].getValue (x, y, z) * scale + bias;
   }

   /// Returns the bias to apply to the scaled output value from the
   /// source module.
   ///
   /// @returns The bias to apply.
   ///
   /// The getValue() method retrieves the output value from the source
   /// module, multiplies it with the scaling factor, adds the bias to
   /// it, then outputs the value.
   public double getBias ()
   {
      return bias;
   }

   /// Returns the scaling factor to apply to the output value from the
   /// source module.
   ///
   /// @returns The scaling factor to apply.
   ///
   /// The getValue() method retrieves the output value from the source
   /// module, multiplies it with the scaling factor, adds the bias to
   /// it, then outputs the value.
   public double getScale ()
   {
      return scale;
   }


   /// Sets the bias to apply to the scaled output value from the source
   /// module.
   ///
   /// @param bias The bias to apply.
   ///
   /// The getValue() method retrieves the output value from the source
   /// module, multiplies it with the scaling factor, adds the bias to
   /// it, then outputs the value.
   public void setBias (double bias)
   {
      this.bias = bias;
   }

   /// Sets the scaling factor to apply to the output value from the
   /// source module.
   ///
   /// @param scale The scaling factor to apply.
   ///
   /// The getValue() method retrieves the output value from the source
   /// module, multiplies it with the scaling factor, adds the bias to
   /// it, then outputs the value.
   public void setScale (double scale)
   {
      this.scale = scale;
   }

}
